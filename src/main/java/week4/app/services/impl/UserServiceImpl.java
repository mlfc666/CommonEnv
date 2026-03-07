package week4.app.services.impl;

import org.jspecify.annotations.NonNull;
import week4.app.dto.LoginDTO;
import week4.app.dto.PasswordUpdateDTO;
import week4.app.dto.UserInfoDTO;
import week4.app.models.User;
import week4.app.repository.MemoRepository;
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
    private final MemoRepository memoRepository;

    public UserServiceImpl(UserRepository userRepository, MemoRepository memoRepository) {
        this.userRepository = userRepository;
        this.memoRepository = memoRepository;
    }

    @Override
    public String register(LoginDTO dto) {
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
        Integer userId = userRepository.save(user);
        // 相当于自动登录
        // 签发 JWT 令牌，Payload 注入用户 ID (uid)
        return JwtUtils.createToken(user.getUsername(), 3600, Map.of("uid", userId));
    }

    @Override
    public String login(LoginDTO dto) {
        // 登录同样强制要求人机验证
        if (!CloudflareUtils.verify(dto.cfToken())) {
            throw new ForbiddenException("人机验证未通过");
        }
        // 验证用户身份
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UnauthorizedException("用户名或密码错误"));
        // 比对加盐后的密码密文
        if (!PasswordUtils.verify(dto.password(), user.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        // 签发 JWT 令牌，Payload 注入用户 ID (uid)
        return JwtUtils.createToken(user.getUsername(), 3600, Map.of("uid", user.getId()));
    }

    @Override
    public UserInfoDTO getUserInfo(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("用户不存在"));
        return new UserInfoDTO(user.getUsername(), user.getCreateTime(), user.getId(), user.getAvatar());
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
            fos.write(file.data());
        } catch (IOException e) {
            throw new InternalErrorException("磁盘写入异常: " + e.getMessage());
        }

        // 更新数据库记录
        String newUrl = "/uploads/" + fileName;
        userRepository.updateAvatar(userId, newUrl);

        return newUrl;
    }

    private static @NonNull String getString(MultipartFile file) {
        if (file == null || file.data().length == 0) {
            throw new BadRequestException("上传文件不能为空");
        }

        // 检查后缀名
        String originalName = file.fileName();
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

    @Override
    public String updatePassword(PasswordUpdateDTO dto, Integer userId) {
        // 首先根据唯一标识符检索账户的最新状态
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("找不到指定的账户信息"));

        // 比对前端传输的旧密码哈希值是否与库中密文一致
        if (!PasswordUtils.verify(dto.oldPassword(), user.getPassword())) {
            throw new UnauthorizedException("旧密码验证不通过");
        }

        // 执行更新操作并根据受影响行数判断执行结果
        int rows = userRepository.updatePassword(userId, PasswordUtils.hash(dto.newPassword()));
        if (rows == 0) {
            throw new InternalErrorException("服务器未能同步密码更改");
        }
        return "密码已修改";
    }

    @Override
    public String updateUsername(String username, Integer userId) {
        // 验证新名称长度
        if (username.length() < 3 || username.length() > 16) {
            throw new BadRequestException("用户名长度必须在 3 到 16 个字符之间");
        }
        // 排除自身后检查新名称是否与其他活跃账户冲突
        userRepository.findByUsername(username).ifPresent(u -> {
            if (!u.getId().equals(userId)) {
                throw new ConflictException("该名称已被其他用户占用");
            }
        });

        // 执行更名操作并校验数据库反馈的受影响行数
        int rows = userRepository.updateUsername(userId, username);
        if (rows == 0) {
            throw new InternalErrorException("服务器未能同步用户名更改");
        }
        return "用户名已更新";
    }

    @Override
    public String deleteAccount(Integer userId) {
        // 级联清理该用户在系统中产生的所有业务备忘录数据
        memoRepository.deleteByUserId(userId);

        // 执行账户注销并校验受影响行数确保删除成功
        int rows = userRepository.deleteById(userId);
        if (rows == 0) {
            throw new NotFoundException("注销失败账户可能已不存在");
        }
        return "账户注销成功";
    }
}