package week4.app.repository.impl;

import common.utils.DBExecutor;
import week4.app.models.User;
import week4.app.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Integer save(User user) {
        String sql = "INSERT INTO users (username, password, avatar, logout_time, create_time) VALUES (?, ?, ?, ?, ?)";
        return DBExecutor.executeUpdate(
                "注册新用户: " + user.getUsername(),
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getLogoutTime(), // 默认为 0 或 null
                System.currentTimeMillis()
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password, avatar, logout_time, create_time FROM users WHERE username = ?";
        List<User> results = DBExecutor.executeQuery(
                "根据用户名查询用户: " + username,
                sql,
                this::mapRowToUser,
                username
        );
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT id, username, password, avatar, logout_time, create_time FROM users WHERE id = ?";
        List<User> results = DBExecutor.executeQuery(
                "根据 ID 查询用户: " + id,
                sql,
                this::mapRowToUser,
                id
        );
        return results.stream().findFirst();
    }

    @Override
    public void updateAvatar(Integer userId, String avatarUrl) {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";
        DBExecutor.executeUpdate("更新用户头像 ID: " + userId, sql, avatarUrl, userId);
    }

    @Override
    public void updateLastLogoutTime(Integer userId, Long timestamp) {
        String sql = "UPDATE users SET logout_time = ? WHERE id = ?";
        DBExecutor.executeUpdate("更新用户最后登出时间 ID: " + userId, sql, timestamp, userId);
    }

    @Override
    public Long getLogoutTime(Integer userId) {
        String sql = "SELECT logout_time FROM users WHERE id = ?";
        List<Long> results = DBExecutor.executeQuery(
                "获取最后登出时间戳 ID: " + userId,
                sql,
                rs -> rs.getLong("logout_time"),
                userId
        );
        // 如果没有记录则返回 0，确保 JWT 校验逻辑（iat >= lastLogoutTime/1000）能通过
        return results.isEmpty() ? 0L : results.get(0);
    }

    /**
     * 将数据库结果集映射为 User 实体对象
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setAvatar(rs.getString("avatar"));
        user.setLogoutTime(rs.getLong("logout_time"));
        user.setCreateTime(rs.getLong("create_time"));
        return user;
    }
}