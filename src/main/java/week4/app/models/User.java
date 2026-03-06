package week4.app.models;

/**
 * 用户模型
 */
public class User {
    /**
     * 创建时间戳
     */
    private long createTime;
    /**
     * ID 编号
     */
    private Integer id;
    /**
     * 密码，用户的密码
     */
    private String password;
    /**
     * 用户名，用户的账号
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 登出时间戳
     */
    private long logoutTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long value) {
        this.createTime = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(long logoutTime) {
        this.logoutTime = logoutTime;
    }
}