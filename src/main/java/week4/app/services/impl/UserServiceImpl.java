package week4.app.services.impl;

import org.jspecify.annotations.NonNull;
import week4.app.dto.LoginDTO;
import week4.app.models.User;
import week4.app.repository.UserRepository;
import week4.app.services.UserService;
import week4.app.utils.PasswordUtils;
import week4.framework.exception.*;
import week4.framework.models.MultipartFile;
import week4.framework.utils.CloudflareUtils;
import week4.framework.utils.JwtUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> register(LoginDTO dto) {
        // 校验 Cloudflare 人机验证令牌
        if (!CloudflareUtils.verify(dto.cfToken())) {
            throw new ForbiddenException("人机验证未通过");
        }
        // 检查用户名是否已被占用
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ConflictException("用户名已存在");
        }
        // 执行加盐哈希并保存用户信息
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(PasswordUtils.hash(dto.password()));
        user.setLogoutTime(0L);
        userRepository.save(user);

        return Map.of("status", "success", "message", "注册成功");
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        // 登录同样强制要求人机验证
        if (!CloudflareUtils.verify(dto.cfToken())) {
            throw new ForbiddenException("人机验证未通过");
        }
        // 验证用户身份
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UnauthorizedException("用户名或密码错误"));
        // 比对加盐后的密码密文
        if (!user.getPassword().equals(PasswordUtils.hash(dto.password()))) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        // 签发 JWT 令牌，Payload 注入用户 ID (uid)
        String token = JwtUtils.createToken(user.getUsername(), 3600, Map.of("uid", user.getId()));

        return Map.of("token", token, "username", user.getUsername(), "avatar",
                user.getAvatar() != null ? user.getAvatar() : "");
    }

    @Override
    public String uploadAvatar(Integer userId, MultipartFile file) {
        String ext = getString(file);

        // 确保目录可用
        String uploadDir = "/app/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new InternalErrorException("无法创建存储目录");
        }

        // 删除旧头像
        userRepository.findById(userId).ifPresent(user -> {
            String oldUrl = user.getAvatar();
            if (oldUrl != null && oldUrl.startsWith("/uploads/")) {
                File oldFile = new File("/app" + oldUrl); // 映射物理路径到 /app/uploads/...
                if (oldFile.exists() && !oldFile.delete()) {
                    System.err.println("警告: 旧头像删除失败: " + oldUrl);
                }
            }
        });

        // 生成新文件并写入磁盘
        String fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + ext;
        try (FileOutputStream fos = new FileOutputStream(uploadDir + fileName)) {
            fos.write(file.getData());
        } catch (IOException e) {
            throw new InternalErrorException("磁盘写入异常: " + e.getMessage());
        }

        // 更新数据库记录
        String newUrl = "/uploads/" + fileName;
        userRepository.updateAvatar(userId, newUrl);

        return newUrl;
    }

    private static @NonNull String getString(MultipartFile file) {
        if (file == null || file.getData().length == 0) {
            throw new BadRequestException("上传文件不能为空");
        }

        // 检查后缀名
        String originalName = file.getFileName();
        if (originalName == null || !originalName.contains(".")) {
            throw new BadRequestException("非法文件名");
        }
        String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".webp")) {
            throw new BadRequestException("仅支持 jpg, png, webp 格式");
        }
        return ext;
    }

    @Override
    public void logout(Integer userId) {
        // 更新数据库登出时间，使之前签发的所有 Token 立即失效
        userRepository.updateLastLogoutTime(userId, System.currentTimeMillis());
    }

    @Override
    public boolean isTokenValid(Integer userId, Long iat) {
        Long logoutTime = userRepository.getLogoutTime(userId);
        // 如果从未登出，或者 Token 签发时间（秒）晚于最后登出时间（毫秒转秒）
        return logoutTime == null || iat >= (logoutTime / 1000);
    }
}