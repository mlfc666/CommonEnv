package week2;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import common.utils.ConsoleColors;
import week2.enums.Gender;
import week2.enums.ScoreRemark;
import week2.repository.CourseRepository;
import week2.repository.ScoreRepository;
import week2.repository.StudentRepository;
import week2.repository.impl.*;
import week2.services.*;
import week2.services.impl.*;
import week2.ui.ConsoleMenu;
import week2.ui.TablePrinter;
import week2.utils.DBExecutor;
import week2.utils.DBInitializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final ConsoleMenu ui = new ConsoleMenu();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 初始化持久层
    private static final StudentRepository studentRepo = new StudentRepositoryImpl();
    private static final CourseRepository courseRepo = new CourseRepositoryImpl();
    private static final ScoreRepository scoreRepo = new ScoreRepositoryImpl();

    // 初始化业务层
    private static final StudentService studentService = new StudentServiceImpl(studentRepo);
    private static final CourseService courseService = new CourseServiceImpl(courseRepo);
    private static final ScoreService scoreService = new ScoreServiceImpl(scoreRepo);
    private static final StatisticsService statisticsService = new StatisticsServiceImpl(studentRepo);

    private static DB embeddedDB;

    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();
            switch (choice) {
                case "S1" -> handleInitService();
                case "S2" -> handleInsertDefaultData();
                case "S3" -> handleAddSingleStudent();
                case "S4" -> handleAddSingleCourse();
                case "S5" -> handleAddSingleScore();
                case "S6" -> handleShowRawTables();
                case "S7" -> handleExecuteCustomSQL();
                case "S8" -> handleClearAllData();
                case "1" -> handleUpdateModule();
                case "2" -> handleDeleteModule();
                case "3" -> handleBaseQueryModule();
                case "4" -> handleGroupingQuery();
                case "5" -> handleAdvancedQuery();
                case "0" -> {
                    stopDatabase();
                    ui.showMessage("程序已退出。", ConsoleColors.YELLOW);
                    return;
                }
                default -> ui.showMessage("错误：无效选择。", ConsoleColors.RED);
            }
        }
    }

    private static void handleInitService() {
        try {
            if (embeddedDB == null) {
                DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
                configBuilder.setPort(3306);
                configBuilder.addArg("--user=root");
                embeddedDB = DB.newEmbeddedDB(configBuilder.build());
                embeddedDB.start();
                DBInitializer.initializer(ui);
                ui.showMessage("MySQL 服务启动成功。", ConsoleColors.GREEN);
            }
        } catch (Exception e) {
            ui.showMessage("启动失败: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    private static void handleInsertDefaultData() {
        ui.printHeader("插入初始数据");
        // 插入学生数据
        studentService.addStudent("张三", Gender.MALE, "高一1班", 18, "13800138000");
        studentService.addStudent("李四", Gender.FEMALE, "高一2班", 17, "13900139000");
        studentService.addStudent("王五", Gender.MALE, "高一1班", 18, "13700137000");
        studentService.addStudent("赵六", Gender.FEMALE, "高一2班", 19, "13600136000");
        studentService.addStudent("孙七", Gender.MALE, "高一1班", 17, null);

        // 插入课程数据
        courseService.addCourse("数学", "张老师", 5.0);
        courseService.addCourse("语文", "李老师", 5.0);
        courseService.addCourse("英语", "王老师", 4.0);
        courseService.addCourse("物理", "赵老师", 4.0);

        // 插入成绩数据
        LocalDateTime defaultTime = LocalDateTime.parse("2025-10-01 08:00:00", formatter);

        scoreService.addScore(1, 1, 90.5, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(1, 2, 85.0, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(1, 3, 88.5, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(2, 1, 78.0, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(2, 2, 92.0, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(3, 1, 85.0, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(3, 3, 95.0, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(4, 4, 60.0, defaultTime, ScoreRemark.MAKEUP); // 补考
        scoreService.addScore(5, 1, 59.5, defaultTime, ScoreRemark.NORMAL);
        scoreService.addScore(5, 4, 80.0, defaultTime, ScoreRemark.NORMAL);
        ui.showMessage("提示：已准备好数据插入接口。", ConsoleColors.GREEN);
    }

    private static void handleAddSingleStudent() {
        ui.printHeader("S3. 录入新学生");
        String name = ui.askForString("学生姓名");
        Gender gender = ui.askForGender();
        String className = ui.askForString("所在班级");
        int age = ui.askForInt("学生年龄");
        String phone = ui.askForString("手机号 (无则直接回车)");
        studentService.addStudent(name, gender, className, age, phone.isEmpty() ? null : phone);
        ui.showMessage("学生录入成功。", ConsoleColors.GREEN);
    }

    private static void handleAddSingleCourse() {
        ui.printHeader("S4. 录入新课程");
        String name = ui.askForString("课程名称");
        String teacher = ui.askForString("授课老师");
        Double credit = ui.askForDouble("学分");
        courseService.addCourse(name, teacher, credit);
        ui.showMessage("课程录入成功。", ConsoleColors.GREEN);
    }

    private static void handleAddSingleScore() {
        ui.printHeader("S5. 录入新成绩");
        int sId = ui.askForInt("学生 ID (student_id)");
        int cId = ui.askForInt("课程 ID (course_id)");
        Double score = ui.askForDouble("分数");
        ScoreRemark remark = ui.askForRemark();
        scoreService.addScore(sId, cId, score, LocalDateTime.now(), remark);
        ui.showMessage("成绩录入成功。", ConsoleColors.GREEN);
    }

    private static void handleShowRawTables() {
        ui.printHeader("S6. 查看原始表数据");
        System.out.println("1. 学生表 | 2. 课程表 | 3. 成绩表");
        String sub = ui.askForString("请选择");
        switch (sub) {
            case "1" ->
                    TablePrinter.render("Students 原始表", studentService.findAll(), TablePrinter::printStudent, "空");
            case "2" -> TablePrinter.render("Courses 原始表", courseService.findAll(), TablePrinter::printCourse, "空");
            case "3" -> TablePrinter.render("Scores 原始表", scoreService.findAll(), TablePrinter::printRawScore, "空");
        }
    }

    private static void handleExecuteCustomSQL() {
        ui.printHeader("S7. 执行自定义 SQL");
        String sql = ui.askForString("请输入 SQL (UPDATE/DELETE/INSERT)");
        try {
            int rows = DBExecutor.executeSql(sql);
            ui.showMessage("执行成功，受影响行数: " + rows, ConsoleColors.GREEN);
        } catch (Exception e) {
            ui.showMessage("SQL 语法错误: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    private static void handleClearAllData() {
        ui.printHeader("S8. 彻底重置数据库");
        ui.showMessage("警告：这将删除整个数据库并重建表结构！", ConsoleColors.RED);
        boolean confirm = ui.askForString("确定执行吗？(y/n)").equalsIgnoreCase("y");

        if (!confirm) {
            ui.showMessage("操作已取消。", ConsoleColors.YELLOW);
            return;
        }

        try {
            DBExecutor.executeSql("DROP DATABASE IF EXISTS week2");
            DBExecutor.executeSql("CREATE DATABASE week2");
            DBExecutor.executeSql("USE week2");
            DBInitializer.initializer(ui);

            ui.showMessage("数据库已彻底重置，表结构已重建。", ConsoleColors.GREEN);
        } catch (Exception e) {
            ui.showMessage("重置失败: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    private static void handleUpdateModule() {
        ui.printHeader("3. 更新数据");
        System.out.println("1. 高一1班全员加1岁 | 2. 修改物理课(5分/钱老师) | 3. 修改孙七成绩(60分/补考通过)");
        String sub = ui.askForString("选择任务");
        switch (sub) {
            case "1" -> studentService.addAllStudentsAge("高一1班");
            case "2" -> courseService.updateCourseByCourseName("物理", "钱老师", 5.0);
            case "3" -> scoreService.updateScoreByStudentNameAndCourseName("孙七", "数学", 60.0, ScoreRemark.MAKEUP);
        }
        ui.showMessage("操作执行完毕。", ConsoleColors.GREEN);
    }

    private static void handleDeleteModule() {
        ui.printHeader("4. 删除数据");
        System.out.println("1. 删除<60正常考试 | 2. 删除英语<80成绩 | 3. 清理139手机号成绩");
        String sub = ui.askForString("选择任务");
        switch (sub) {
            case "1" -> scoreService.deleteScoreByScoreLessThan(60.0, ScoreRemark.NORMAL);
            case "2" -> scoreService.deleteScoreByCourseNameAndScoreLessThan("英语", 80.0);
            case "3" -> scoreService.deleteScoreByStudentPhoneStartingWith("139");
        }
        ui.showMessage("删除操作完成。", ConsoleColors.GREEN);
    }

    private static void handleBaseQueryModule() {
        ui.printHeader("5. 基础查询");
        System.out.println("1. 女生名单 | 2. 高学分课程 | 3. 特定男生");
        String sub = ui.askForString("选择任务");
        switch (sub) {
            case "1" ->
                    TablePrinter.render("女生名单(班级升序,年龄降序)", studentService.findFemaleStudentsOrderByClassAscAgeDesc(), TablePrinter::printStudent, "无数据");
            case "2" ->
                    TablePrinter.render("学分>4的课程", courseService.findDistinctByCreditGreaterThanOrderByCreditDesc(4.0), TablePrinter::printCourse, "无数据");
            case "3" ->
                    TablePrinter.render("高一1班17-18岁男生(前2条)", studentService.findFirst2ByClassNameAndAgeBetweenAndGender("高一1班", 17, 18, Gender.MALE), TablePrinter::printStudent, "无数据");
        }
    }

    private static void handleGroupingQuery() {
        TablePrinter.render("6. 班级男女人数统计", statisticsService.getClassGenderStatistics(), TablePrinter::printClassStat, "无统计记录");
    }

    private static void handleAdvancedQuery() {
        ui.printHeader("7. 进阶查询");
        System.out.println("1. 张老师学生成绩(降序) | 2. 姓名含'三'或'五'的记录");
        String sub = ui.askForString("选择任务");
        if ("1".equals(sub)) {
            TablePrinter.render("张老师授课成绩", scoreService.findScoresByTeacherNameOrderByScoreDesc("张老师"), TablePrinter::printScoreDetail, "无记录"); //
        } else if ("2".equals(sub)) {
            TablePrinter.render("关键字(三/五)查询结果", scoreService.findScoresByStudentNameKeywords("三", "五"), TablePrinter::printScoreDetail, "无记录"); //
        }
    }

    private static void stopDatabase() {
        try {
            if (embeddedDB != null) embeddedDB.stop();
        } catch (Exception ignored) {
        }
    }
}