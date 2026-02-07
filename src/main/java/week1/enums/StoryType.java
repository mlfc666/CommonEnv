package week1.enums;

public enum StoryType {
    FUNNY("搞笑类"),
    ADVENTURE("冒险类"),
    LEARNING("学习类");

    private final String type;
    StoryType(String desc) { this.type = desc; }

    public String getType() {
        return type;
    }
}
