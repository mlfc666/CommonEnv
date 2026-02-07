package week1;

import week1.enums.StoryEnum;
import week1.models.MonsterModel;
import week1.models.StoryModel;
import week1.services.MonsterService;
import week1.services.Statistics;
import week1.services.impl.MonsterServiceImpl;
import week1.services.impl.StatisticsImpl;
import week1.ui.ConsoleColors;
import week1.ui.ConsoleMenu;
import week1.ui.IllegalMonsterException;

public class Main {
    // 实例化 UI 组件和 Service 组件
    private static final ConsoleMenu ui = new ConsoleMenu();
    private static final MonsterService service = new MonsterServiceImpl();
    private static final Statistics statistics = new StatisticsImpl();

    public static void main(String[] args) {
        while (true) {
            String choice = ui.selectOption();

            // 使用 Java 17 增强型 switch 表达式
            switch (choice) {
                case "1" -> handleAddMonster();
                case "2" -> handleShowAll();
                case "3" -> handleAddStory();
                case "4" -> handleShowStories();
                case "5" -> handleStatistics();
                case "0" -> {
                    ui.showMessage("程序已退出。", ConsoleColors.YELLOW);
                    return;
                }
                default -> ui.showMessage("错误：无效的选择。", ConsoleColors.RED);
            }
        }
    }

    private static void handleAddMonster() {
        ui.printHeader("创建小怪兽");
        try {
            String name = ui.askForString("姓名");
            int age = ui.askForAge();
            String desc = ui.askForString("介绍");
            MonsterModel monster = new MonsterModel(name, age, desc);
            service.addMonster(monster);
            ui.showMessage("成功：已添加小怪兽。", ConsoleColors.GREEN);
        } catch (IllegalMonsterException e) {
            ui.showMessage("失败：" + e.getMessage(), ConsoleColors.RED);
        } catch (Exception e) {
            ui.showMessage("其他未知错误: " + e.getMessage(), ConsoleColors.RED);
        }
    }

    private static void handleShowAll() {
        ui.printHeader("所有小怪兽列表");
        var monsters = service.getAllMonsters();
        if (monsters.isEmpty()) {
            System.out.println("目前暂无怪兽数据。");
        } else {
            monsters.forEach(System.out::println);
        }
    }

    private static void handleAddStory() {
        ui.printHeader("为小怪兽添加故事");
        String name = ui.askForString("请输入要添加故事的怪兽姓名");
        String title = ui.askForString("故事标题");
        String content = ui.askForString("故事内容");
        StoryEnum type = ui.askForStoryType();

        StoryModel story = new StoryModel(title, content, type);
        if (service.addStoryToMonster(name, story)) {
            ui.showMessage("成功：故事已记录。", ConsoleColors.GREEN);
        } else {
            ui.showMessage("失败：未找到怪兽或该故事标题已存在。", ConsoleColors.RED);
        }
    }

    private static void handleShowStories() {
        String name = ui.askForString("输入怪兽姓名查看故事");
        service.findByName(name).ifPresentOrElse(
                m -> {
                    ui.showMessage("--- " + name + " 的故事列表 ---", ConsoleColors.PURPLE);
                    m.getStoryList().forEach(System.out::println);
                },
                () -> ui.showMessage("错误：未找到该怪兽。", ConsoleColors.RED)
        );
    }

    private static void handleStatistics() {
        ui.printHeader("系统统计数据");
        var monsters = service.getAllMonsters();
        System.out.println("当前怪兽总数: " + monsters.size());

        statistics.getMostPopular(monsters).ifPresentOrElse(
                s -> ui.showMessage("最受欢迎故事: " + s.getTitle() + " (" + s.getType().getDesc() + ")", ConsoleColors.YELLOW),
                () -> System.out.println("暂无流行故事统计数据。")
        );
    }
}