package common.ui;

public interface ConsoleMenu {
    void showMessage(String msg, String color);

    String askForString(String label);

    void printHeader(String title);
}