package week1.models;

import week1.enums.StoryEnum;
import week1.ui.ConsoleColors;
import week1.utils.DateUtil;

import java.util.Objects;

public class StoryModel {
    // 故事标题
    private String title;
    // 内容
    private String content;
    // 种类
    private StoryEnum type;
    // 创建时间（只读不改）
    private final String createTime;

    // 构造方法
    public StoryModel(String title, String content, StoryEnum type) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.createTime = DateUtil.now();
    }

    @Override
    public boolean equals(Object o) {
        return (this == o) || (o instanceof StoryModel that && Objects.equals(title, that.title));
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        // 标题、类型、创建时间
        return String.format(
                "%s《%s》%s  种类: %s[%s]%s  创建时间： %s%s%s",
                ConsoleColors.YELLOW, title, ConsoleColors.RESET,
                ConsoleColors.PURPLE, type.getDesc(), ConsoleColors.RESET,
                ConsoleColors.CYAN, createTime, ConsoleColors.RESET
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StoryEnum getType() {
        return type;
    }

    public void setType(StoryEnum type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }
}
