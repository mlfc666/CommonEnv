package week3;

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
    // 初始化持久层
    private static final StudentDao studentDao = new StudentDaoImpl();
    private static final ScoreDao scoreDao = new ScoreDaoImpl();

    // 初始化业务层
    private static final StudentService studentService = new StudentServiceImpl(studentDao, scoreDao);
    private static final ScoreService scoreService = new ScoreServiceImpl(scoreDao);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime defaultTime = LocalDateTime.parse("2026-02-15 08:00:00", formatter);

    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S1" -> handleInitService();
                case "S2" -> handleClearAllData();
                case "S3" -> handleInsertDefaultData();
                case "S4" -> handleBatchImportStudents();
                case "S5" -> handleAddSingleStudent();
                case "S6" -> handleAddSingleScore();
                case "S7" -> handleShowRawTables();
                case "1" -> handleDeleteStudentComplete();
                case "2" -> handleGetStudentsByPage();
                case "3" -> handleUpdateStudentInfo();
                case "4" -> handleGetStudentById();
                case "5" -> handleSearchStudentsByNameKeyword();
                case "6" -> handleFindScoresByStudentNo();
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
        JdbcUtils.startEmbeddedDB(ui);
        Database.init(ui);
    }

    private static void handleClearAllData() {
        ui.printHeader("S2. 彻底重置数据库");
        if (JdbcUtils.resetEmbeddedDB(ui)) {
            Database.init(ui);
        }
    }

    private static void handleInsertDefaultData() {
        ui.printHeader("S3. 插入初始数据");

        // 插入学生数据
        studentService.addStudent(new Student(null, "张三", Gender.MALE, 18, "SN001", defaultTime));
        studentService.addStudent(new Student(null, "李四", Gender.FEMALE, 19, "SN002", defaultTime));

        // 插入成绩数据
        scoreService.addScore(new Score(1, "Java程序设计", 92.5, defaultTime));
        scoreService.addScore(new Score(1, "数据库原理", 88.0, defaultTime));
        scoreService.addScore(new Score(2, "Java程序设计", 95.0, defaultTime));
        scoreService.addScore(new Score(2, "高等数学", 72.5, defaultTime));

        ui.showMessage("初始学生及成绩数据插入成功。", ConsoleColors.GREEN);
    }

    private static void handleBatchImportStudents() {
        ui.printHeader("S4. 批量新增学生");
        List<Student> list = new ArrayList<>();
        list.add(new Student(null, "王五", Gender.MALE, 20, "SN003", defaultTime));
        list.add(new Student(null, "赵六", Gender.FEMALE, 21, "SN004", defaultTime));
        studentService.batchImportStudents(list);
        ui.showMessage("批量导入完成。", ConsoleColors.GREEN);
    }

    private static void handleAddSingleStudent() {
        ui.printHeader("S5. 新增单条学生数据");
        String name = ui.askForString("姓名");
        Gender gender = ui.askForGender();
        int age = ui.askForInt("年龄");
        String no = ui.askForString("学号");
        studentService.addStudent(new Student(name, gender, age, no));
        ui.showMessage("学生录入成功。", ConsoleColors.GREEN);
    }

    private static void handleAddSingleScore() {
        ui.printHeader("S6. 新增单条学生成绩");
        int studentId = ui.askForInt("学生 ID");
        String subject = ui.askForString("科目名称");
        double val = Double.parseDouble(ui.askForString("分数"));
        scoreService.addScore(new Score(studentId, subject, val, LocalDateTime.now()));
    }

    private static void handleShowRawTables() {
        ui.printHeader("S7. 查看原始表数据");
        System.out.println("1. 学生表 (Student) | 2. 成绩表 (Score)");
        String sub = ui.askForString("请选择");
        switch (sub) {
            case "1" ->
                    TablePrinter.render("Student 原始表", studentService.findAll(), TablePrinter::printStudent, "暂无学生数据");
            case "2" ->
                    TablePrinter.render("Score 原始表", scoreService.findAll(), TablePrinter::printScore, "暂无成绩数据");
            default -> ui.showMessage("无效选择", ConsoleColors.RED);
        }
    }

    private static void handleDeleteStudentComplete() {
        ui.printHeader("1. 根据id删除学生数据及其所有成绩");
        int id = ui.askForInt("请输入要删除的学生ID");
        studentService.deleteStudentComplete(id);
    }

    private static void handleGetStudentsByPage() {
        ui.printHeader("2. 条件分页查询学生");

        String name = ui.askForString("姓名模糊条件 (直接回车忽略)");
        String nameKeyword = name.isEmpty() ? null : name;

        Gender g = ui.askForGender();
        String gender = (g == null) ? null : g.getGender();

        String minAgeStr = ui.askForString("最小年龄 (直接回车忽略)");
        Integer minAge = minAgeStr.isEmpty() ? null : Integer.parseInt(minAgeStr);

        String maxAgeStr = ui.askForString("最大年龄 (直接回车忽略)");
        Integer maxAge = maxAgeStr.isEmpty() ? null : Integer.parseInt(maxAgeStr);

        String no = ui.askForString("学号 (直接回车忽略)");
        String studentNo = no.isEmpty() ? null : no;

        int page = ui.askForInt("页码");
        int size = ui.askForInt("每页条数");

        StudentQueryDTO query = new StudentQueryDTO(nameKeyword, gender, minAge, maxAge, studentNo);
        StudentPageDTO dto = studentService.getStudentsByPage(query, page, size);

        System.out.println(ConsoleColors.BLUE + "--- 查询结果 (总记录数: " + dto.getTotal() + ") ---" + ConsoleColors.RESET);
        dto.getData().forEach(System.out::println);
    }

    private static void handleUpdateStudentInfo() {
        ui.printHeader("3. 根据id修改学生的姓名和年龄");
        int id = ui.askForInt("学生ID");
        String name = ui.askForString("新姓名");
        int age = ui.askForInt("新年龄");
        studentService.updateStudentInfo(id, name, age);
        ui.showMessage("修改成功。", ConsoleColors.GREEN);
    }

    private static void handleGetStudentById() {
        ui.printHeader("4. 根据id查询单个学生数据");
        int id = ui.askForInt("查询ID");
        Optional<Student> student = studentService.getStudentById(id);
        student.ifPresentOrElse(
                System.out::println,
                () -> ui.showMessage("未找到该ID的学生。", ConsoleColors.RED)
        );
    }

    private static void handleSearchStudentsByNameKeyword() {
        ui.printHeader("5. 根据姓名模糊查询学生数据");
        String keyword = ui.askForString("关键字");
        List<Student> list = studentService.searchStudentsByNameKeyword(keyword);
        list.forEach(System.out::println);
    }

    private static void handleFindScoresByStudentNo() {
        ui.printHeader("6. 根据学号查询指定学生的所有科目成绩");
        String no = ui.askForString("学生学号");
        List<Score> scores = scoreService.findScoresByStudentNo(no);
        if (scores.isEmpty()) {
            ui.showMessage("该学生暂无成绩记录。", ConsoleColors.YELLOW);
        } else {
            scores.forEach(System.out::println);
        }
    }
}