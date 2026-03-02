# 学生成绩管理系统 - 开发文档

## 1. 任务进度概览

### 1.1 核心数据库架构
- [x] **物理表结构自动同步**：程序启动时通过 `S1/S2` 选项自动执行 DDL，确保 `student` 和 `score` 表结构符合最新规范。

```java
private static final String createStudentTableSql = """
            CREATE TABLE student (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空（姓名）
                name VARCHAR(20) NOT NULL,
            
                -- 非空（男/女）
                gender CHAR(1) NOT NULL,
            
                -- 非空（学生年龄）
                age TINYINT NOT NULL,
            
                -- 唯一、非空（学号10位）
                student_no VARCHAR(10) NOT NULL UNIQUE,
            
                -- 非空（创建时间，默认当前时间）
                create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
            """;

    private static final String createScoreTableSql = """
            CREATE TABLE score (
                -- 主键、自增、非空
                id INT AUTO_INCREMENT PRIMARY KEY,
            
                -- 非空（与student表id对应）
                student_id INT NOT NULL,
            
                -- 非空（科目名称）
                subject VARCHAR(50) NOT NULL,
            
                -- 非空（成绩，满分100）
                score DECIMAL(5,2) NOT NULL,
            
                -- 非空（考试日期）
                exam_time DATE NOT NULL,
            
                -- 外键约束：级联删除
                CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
            );
            """;
```
- [x] **自动化时间戳管理**：学生记录包含 `create_time`，成绩记录包含 `exam_time`，均采用 `DATETIME` 类型以支持精确到秒的操作审计。
- [x] **数据完整性维护**：学生 ID 作为成绩表的外键（`student_id`），在业务层实现手动事务级联删除，确保逻辑完整性。

### 1.2 业务功能实现
- [x] **深度事务控制 (ACID)**：
    - 在“删除学生及其成绩”业务中，通过 `ThreadLocal` 绑定数据库连接，实现跨 DAO 方法的原子操作，支持异常自动回滚。
- [x] **动态条件分页查询**：
    - 核心逻辑支持：姓名模糊、性别精确、年龄区间、学号精确等多维度可选条件组合。
    - 实现 `LIMIT` 与 `OFFSET` 分页，并同步返回 `COUNT(*)` 总记录数以支撑 UI 分页组件。
- [x] **高性能批处理**：
    - 实现 `executeBatch` 引擎，支持千条以上学生数据的快速导入，开启手动提交模式以大幅提升 IO 性能。
- [x] **SQL 实时审计回显**：
    - 封装 `extractSql` 逻辑，在控制台实时打印 PreparedStatement 填充参数后的“真实执行 SQL”，便于开发调试。

---

## 2. 项目类层级说明

### 2.1 核心模型与传输对象 (Domain Models & DTOs)
| 类名称 | 职责说明 |
| :--- | :--- |
| `Student` | **学生实体**：增加 `studentNo` 唯一编号字段及 `LocalDateTime` 创建时间 |
| `Score` | **成绩实体**：映射成绩记录，管理与学生表的物理关联及科目分数信息 |
| `StudentQueryDTO` | **复杂查询对象**：封装多条件筛选参数（姓名、性别、年龄段、学号） |
| `StudentPageDTO` | **分页包装对象**：承载分页后的 `List<Student>` 数据及总条数 `total` |

### 2.2 持久化层 (DAO Layer)
| 类/接口名称 | 职责说明 |
| :--- | :--- |
| `StudentDao` | 定义学生表的 CRUD 规范，包含基于 DTO 的动态过滤分页接口 |
| `ScoreDao` | 定义成绩表的 SQL 逻辑，支持按学生 ID 级联删除及按学号跨表成绩检索 |

### 2.3 业务逻辑层 (Service Layer)
| 类/接口名称 | 职责说明 |
| :--- | :--- |
| `StudentService` | **事务协调中心**：通过控制连接的 `commit` 与 `rollback` 确保学生删除操作的安全性 |
| `ScoreService` | 封装成绩业务，处理学号到学生 ID 的逻辑转换及成绩录入 |

### 2.4 表现层与基础设施 (UI & Infrastructure)
| 类名称 | 职责说明 |
| :--- | :--- |
| `TablePrinter` | **标准化输出**：针对 Week3 实体优化，提供彩色网格化的学生与成绩列表打印 |
| `DBExecutor` | **增强型执行引擎**：**[核心]** 移除了自动关闭连接逻辑，支持事务穿透及 SQL 语句深度审计 |
| `JdbcUtils` | **事务连接管理器**：利用 `ThreadLocal` 容器确保同一线程内获取的是同一个 Connection 实例 |

---

## 3. 技术亮点说明

### 3.1 健壮的事务处理
```java
// 典型事务代码块实现
try {
    JdbcUtils.getConnection().setAutoCommit(false); // 开启事务
    scoreDao.deleteByStudentId(id);                // Step 1
    studentDao.deleteById(id);                     // Step 2
    JdbcUtils.getConnection().commit();            // 提交
} catch (Exception e) {
    JdbcUtils.getConnection().rollback();          // 失败自动回滚
    throw new RuntimeException(e);
} finally {
    JdbcUtils.closeConnection();                   // 清理线程绑定，归还连接
}
```

### 3.2 动态分页 SQL 逻辑
系统根据 `StudentQueryDTO` 中各字段是否为 `null` 动态拼接 `WHERE` 子句。
```sql
SELECT * FROM student 
WHERE 1=1 
  AND name LIKE ? 
  AND age >= ? 
  LIMIT 5 OFFSET 0;
```

### 3.3 交互式 SQL 审计
通过重写 `extractSql`，控制台会实时反馈执行细节：
```text
[描述]: 删除学生及其所有成绩
[SQL]: DELETE FROM score WHERE student_id = 1
[执行成功] 耗时: 2ms | 影响行数: 3
---------------------------------------------------------
```

---

## 4. 运行环境配置
* **Runtime**: JDK 17+
* **Dependencies**: MySQL Connector/J 8.0+
* **Database**: MySQL 8.0+ (需支持 DATETIME 与默认事务引擎 InnoDB)