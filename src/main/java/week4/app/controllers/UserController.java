package week4.app.controllers;

import week4.app.dto.LoginDTO;
import week4.app.dto.PasswordUpdateDTO;
import week4.app.dto.UserInfoDTO;
import week4.app.services.UserService;
import week4.framework.annotations.*;
import week4.framework.models.MultipartFile;

@RestController
@SuppressWarnings("unused")
public class UserController {

    @Inject
    private UserService userService;

    /**
     * 用户注册
     *
     * @param loginDTO 包含用户名、密码及 Cloudflare 人机验证令牌的请求体
     * @return 注册结果及初始化的身份凭证数据
     */
    @PostMapping("/api/user/register")
    public String register(@RequestBody LoginDTO loginDTO) {
        // 调用 Service 执行人机二次校验与加盐哈希存储
        return userService.register(loginDTO);
    }

    /**
     * 用户登录
     *
     * @param loginDTO 包含登录凭据与人机验证令牌的请求体
     * @return 认证成功后的 JWT 令牌及用户基本资料
     */
    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        // 校验身份后签发包含 uid 载荷的加密令牌
        return userService.login(loginDTO);
    }

    /**
     * 获取用户信息
     *
     * @param userId 自动从当前已校验的 JWT 令牌中解析并注入的用户编号
     * @return 包含昵称、头像、注册时间等信息的详细资料对象
     */
    @RequiresAuth
    @GetMapping("/api/user/info")
    public UserInfoDTO info(@AuthClaim("uid") Integer userId) {
        // 基于注入的可信 UID 从数据库检索最新资料
        return userService.getUserInfo(userId);
    }

    /**
     * 上传头像
     *
     * @param avatar 包含二进制流与原始文件名的分段上传对象
     * @param userId 自动注入的当前操作者唯一编号
     * @return 头像在服务器物理存储后映射的 Web 访问路径
     */
    @RequiresAuth
    @PostMapping("/api/user/upload-avatar")
    public String uploadAvatar(
            @RequestPart("avatar") MultipartFile avatar,
            @AuthClaim("uid") Integer userId
    ) {
        // 清理旧头像并持久化新头像文件至物理磁盘
        return userService.uploadAvatar(userId, avatar);
    }

    /**
     * 修改用户名
     *
     * @param username 新的显示名称
     * @return 修改成功的提示消息
     */
    @RequiresAuth
    @PostMapping("/api/user/update-username")
    public String updateUsername(@RequestParam("username") String username, @AuthClaim("uid") Integer userId) {
        // 调用 Service 执行用户名修改
        return userService.updateUsername(username, userId);
    }

    /**
     * 修改密码
     *
     * @param passwordUpdateDTO 密码修改请求体，包含旧密码与新密码
     * @return 修改成功执行的确认消息
     */
    @RequiresAuth
    @PostMapping("/api/user/update-password")
    public String updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO, @AuthClaim("uid") Integer userId) {
        // 调用 Service 执行密码修改
        return userService.updatePassword(passwordUpdateDTO, userId);
    }

    /**
     * 退出登录
     *
     * @param userId 自动注入的当前登录会话用户编号
     * @return 操作成功执行的确认消息
     */
    @RequiresAuth
    @PostMapping("/api/user/logout")
    public String logout(@AuthClaim("uid") Integer userId) {
        // 通过更新数据库退出时间戳，使当前所有在该时刻前签发的 Token 立即失效
        userService.logout(userId);
        return "退出登录成功";
    }

    /**
     * 删除账户
     *
     * @param userId 自动注入的当前登录会话用户编号
     * @return 删除成功执行的确认消息
     */
    @RequiresAuth
    @PostMapping("/api/user/delete")
    public String delete(@AuthClaim("uid") Integer userId) {
        return userService.deleteAccount(userId);
    }
}