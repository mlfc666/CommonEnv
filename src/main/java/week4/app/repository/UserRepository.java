package week4.app.repository;

import week4.app.models.User;

import java.util.Optional;

public interface UserRepository {
    // 插入新用户并返回自增ID
    Integer save(User user);

    // 根据用户名精确查找用户信息
    Optional<User> findByUsername(String username);

    Optional<User> findById(Integer id);

    // 修改指定用户的头像资源路径
    int updateAvatar(Integer userId, String avatarUrl);

    // 更新用户最后一次登出的时间戳
    int updateLastLogoutTime(Integer userId, Long timestamp);

    // 获取用户记录中存储的最后登出时间戳
    Long getLogoutTime(Integer userId);

    // 覆盖更新指定用户的加密密码密文
    int updatePassword(Integer userId, String hashedPassword);

    // 覆盖更新指定用户的账户显示名称
    int updateUsername(Integer userId, String username);

    // 从系统中永久移除指定的用户账户记录
    int deleteById(Integer userId);
}