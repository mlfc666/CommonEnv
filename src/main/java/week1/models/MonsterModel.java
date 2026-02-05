package week1.models;

import week1.ui.ConsoleColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MonsterModel {
    // 名称
    private String name;
    // 年龄
    private int age;
    // 介绍
    private String desc;
    // 故事列表
    private List<StoryModel> storyList = new ArrayList<>();

    // 有的怪物生来就是传奇，自带故事
    public MonsterModel(String name, int age, String desc, List<StoryModel> storyList) {
        this.name = name;
        this.age = age;
        this.desc = desc;
        this.storyList = storyList;
    }

    // 重载一个不需要故事的构造方法
    public MonsterModel(String name, int age, String desc) {
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    // 为小怪兽添加故事
    public boolean addStory(StoryModel story) {
        if (story == null) {
            return false;
        }
        if (this.storyList.contains(story)) {
            return false;
        }
        return this.storyList.add(story);
    }

    // 显示小怪兽的故事数量
    public int getStoryCount() {
        return storyList.size();
    }

    // 显示小怪兽的故事
    public List<StoryModel> getStoryList() {
        return storyList;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // 标题加上颜色
        String coloredTitles = storyList.stream()
                .map(s -> ConsoleColors.YELLOW + "《" + s.getTitle() + "》" + ConsoleColors.RESET)
                .collect(java.util.stream.Collectors.joining(", "));

        return """
            %s-------------------------------------------%s
            | %s%s怪兽档案%s
            | 姓名: %s%s%s
            | 年龄: %s%d 岁%s
            | 介绍: %s%s%s
            | 故事记录: %s已拥有 %d 个故事%s
            | 故事列表： %s
            %s-------------------------------------------%s
            """.formatted(
                ConsoleColors.BLUE, ConsoleColors.RESET,           // 分割线
                ConsoleColors.BOLD, ConsoleColors.GREEN, ConsoleColors.RESET, // 标题
                ConsoleColors.CYAN, name, ConsoleColors.RESET,      // 姓名
                ConsoleColors.CYAN, age, ConsoleColors.RESET,       // 年龄
                ConsoleColors.GREEN, desc, ConsoleColors.RESET,     // 介绍
                ConsoleColors.PURPLE, storyList.size(), ConsoleColors.RESET, // 统计
                coloredTitles,                                      // 列表
                ConsoleColors.BLUE, ConsoleColors.RESET            // 分割线
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o) || (o instanceof MonsterModel that && Objects.equals(name, that.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
