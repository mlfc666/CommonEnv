package week2.enums;

public enum Gender {
    MALE("男"),
    FEMALE("女");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public static Gender fromDesc(String desc) {
        for (Gender g : Gender.values()) {
            if (g.gender.equals(desc)) {
                return g;
            }
        }
        return MALE;
    }
}
