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
    private String creatTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String value) {
        this.creatTime = value;
    }

    public long getid() {
        return id;
    }

    public void setid(Integer value) {
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
}