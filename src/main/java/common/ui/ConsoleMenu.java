package common.ui;

import java.util.Scanner;

public class ConsoleMenu {
    public final Scanner scanner = new Scanner(System.in);

    public String askForString(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    public int askForInt(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                showMessage("错误：请输入有效的整数！", ConsoleColors.RED);
            }
        }
    }

    public Double askForDouble(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                showMessage("错误：请输入有效的数字！", ConsoleColors.RED);
            }
        }
    }

    public void printHeader(String title) {
        System.out.println("\n" + ConsoleColors.BOLD + ConsoleColors.BLUE + "========== " + title + " ==========" + ConsoleColors.RESET);
    }

    public void showMessage(String msg, String color) {
        System.out.println(color + msg + ConsoleColors.RESET);
    }
}