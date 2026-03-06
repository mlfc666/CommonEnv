package week4.app.models;


/**
 * 备忘录模型
 */
public class Memo {
    /**
     * 备忘录内容
     */
    private String content;
    /**
     * 创建时间
     */
    private long creatTime;
    /**
     * ID 编号
     */
    private Integer id;
    /**
     * 标签
     */
    private String[] tags;
    /**
     * 备忘录标题
     */
    private String title;
    /**
     * 用户ID
     */
    private Integer userId;

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long value) {
        this.creatTime = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] value) {
        this.tags = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}