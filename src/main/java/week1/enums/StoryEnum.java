package week1.enums;

public enum StoryEnum {
    FUNNY("搞笑类"),
    ADVENTURE("冒险类"),
    LEARNING("学习类");

    private final String desc;
    StoryEnum(String desc) { this.desc = desc; }

    public String getDesc() {
        return desc;
    }
}
