package week4;

import common.ui.ConsoleColors;
import common.utils.JdbcUtils;
import week3.dao.ScoreDao;
import week3.dao.StudentDao;
import week3.dao.impl.ScoreDaoImpl;
import week3.dao.impl.StudentDaoImpl;
import week3.dto.StudentPageDTO;
import week3.dto.StudentQueryDTO;
import week3.entity.Score;
import week3.entity.Student;
import week3.enums.Gender;
import week3.services.ScoreService;
import week3.services.StudentService;
import week3.services.impl.ScoreServiceImpl;
import week3.services.impl.StudentServiceImpl;
import week3.ui.TablePrinter;
import week3.ui.Week3ConsoleMenu;
import week3.utils.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final Week3ConsoleMenu ui = new Week3ConsoleMenu();
    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S1" -> handleInitService();
                case "0" -> {
                    JdbcUtils.stopEmbeddedDB(ui);
                    ui.showMessage("程序已退出。", ConsoleColors.YELLOW);
                    return;
                }
                default -> ui.showMessage("错误：无效选择。", ConsoleColors.RED);
            }
        }
    }

    private static void handleInitService() {
        try {
            WebServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}