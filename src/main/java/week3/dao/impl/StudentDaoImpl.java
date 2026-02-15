package week3.dao.impl;

import common.utils.DBExecutor;
import week3.dao.StudentDao;
import week3.dto.StudentQueryDTO;
import week3.entity.Student;
import week3.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {

    @Override
    public Student insert(Student student) {
        String sql = """
                INSERT INTO student (name, gender, age, student_no) VALUES (?, ?, ?, ?)
                """;
        DBExecutor.executeUpdate(
                "插入单条学生数据-" + student.getName(),
                sql,
                student.getName(),
                student.getGender().getGender(),
                student.getAge(),
                student.getStudentNo()
        );
        return student;
    }

    @Override
    public List<Student> findAll() {
        String sql = """
                SELECT id, name, gender, age, student_no, create_time FROM student
                """;
        return DBExecutor.executeQuery(
                "查询所有学生数据",
                sql,
                this::mapList
        );
    }

    @Override
    public int updateNameAndAge(Integer id, String name, Integer age) {
        String sql = """
                UPDATE student SET name = ?, age = ? WHERE id = ?
                """;
        return DBExecutor.executeUpdate(
                "根据ID修改学生信息-学生ID:" + id,
                sql,
                name, age, id
        );
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM student WHERE id = ?";
        return DBExecutor.executeUpdate("删除学生所有成绩-学生ID:" + id, sql, id);
    }

    @Override
    public Optional<Student> findById(Integer id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        return DBExecutor.executeQuery(
                "根据ID查询单个学生数据",
                sql,
                rs -> new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        Gender.fromDesc(rs.getString("gender")),
                        rs.getInt("age"),
                        rs.getString("student_no"),
                        rs.getTimestamp("create_time").toLocalDateTime()
                ),
                id
        ).stream().findFirst();
    }

    @Override
    public List<Student> findByNameKeyword(String keyword) {
        String sql = "SELECT * FROM student WHERE name LIKE ?";
        return DBExecutor.executeQuery(
                "姓名模糊查询-" + keyword,
                sql,
                this::mapList,
                "%" + keyword + "%"
        );
    }

    @Override
    public int batchInsert(List<Student> students) {
        String sql = "INSERT INTO student (name, gender, age, student_no) VALUES (?, ?, ?, ?)";

        List<Object[]> paramsList = new ArrayList<>();

        for (Student s : students) {
            // 将每个学生对象转为参数数组并添加到 List 中
            paramsList.add(new Object[]{
                    s.getName(),
                    s.getGender().getGender(),
                    s.getAge(),
                    s.getStudentNo()
            });
        }

        int[] results = DBExecutor.executeBatch("批量新增学生", sql, paramsList);

        return results.length; // 返回处理的记录总数
    }

    @Override
    public List<Student> findByPage(StudentQueryDTO query, int offset, int size) {
        // 支持自定义任何查询条件
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query.getNameKeyword() != null && !query.getNameKeyword().isBlank()) {
            sql.append("AND name LIKE ? ");
            params.add("%" + query.getNameKeyword() + "%");
        }
        if (query.getGender() != null && !query.getGender().isBlank()) {
            sql.append("AND gender = ? ");
            params.add(query.getGender());
        }

        sql.append("LIMIT ?, ?");
        params.add(offset);
        params.add(size);

        return DBExecutor.executeQuery(
                "条件分页查询",
                sql.toString(),
                this::mapList,
                params.toArray()
        );
    }

    @Override
    public long countByCondition(StudentQueryDTO query) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM student WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query.getNameKeyword() != null && !query.getNameKeyword().isBlank()) {
            sql.append("AND name LIKE ? ");
            params.add("%" + query.getNameKeyword() + "%");
        }
        if (query.getGender() != null) {
            sql.append("AND gender = ? ");
            params.add(query.getGender());
        }

        List<Long> result = DBExecutor.executeQuery(
                "统计学生总数",
                sql.toString(),
                rs -> rs.getLong(1),
                params.toArray()
        );

        return result.isEmpty() ? 0L : result.get(0);
    }

    private Student mapList(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                Gender.fromDesc(rs.getString("gender")),
                rs.getInt("age"),
                rs.getString("student_no"),
                rs.getTimestamp("create_time").toLocalDateTime()
        );
    }
}