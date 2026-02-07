package week2.enums;

public enum ScoreRemark {
    NORMAL("正常考试"),
    MAKEUP("缺考");
    private final String remark;

    ScoreRemark(String remark) {this.remark = remark;}
    public String getRemark() {return remark;}
}
