package week1.ui;

public class IllegalMonsterException extends RuntimeException {

    // 无参构造函数
    public IllegalMonsterException() {
        super();
    }

    // 接收错误信息的构造函数
    public IllegalMonsterException(String message) {
        super(message);
    }
}