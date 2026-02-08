package week2.repository.impl;

import week2.dto.ScoreDetailDTO;
import week2.enums.ScoreRemark;
import week2.models.Score;
import week2.repository.ScoreRepository;
import week2.utils.DBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ScoreRepositoryImpl implements ScoreRepository {

    @Override
    public Score insert(Score score) {
        String sql = """
                INSERT INTO scores (student_id, course_id, score, exam_time, remark) VALUES (?, ?, ?, ?, ?)
                """;

        DBExecutor.executeUpdate(
                "插入成绩记录",
                sql,
                score.getStudentId(),
                score.getCourseId(),
                score.getScore(),
                score.getExamTime(),
                score.getRemark().getRemark()
        );
        return score;
    }

    @Override
    public int updateScoreByStudentNameAndCourseName(String studentName, String courseName, Double score, ScoreRemark remark) {
        String sql = "UPDATE scores SET score = ?, remark = ? WHERE student_name = ? AND course_name = ?";
        return DBExecutor.executeUpdate(
                "修改成绩及备注",
                sql,
                score,
                remark.name(),
                studentName,
                courseName
        );
    }

    @Override
    public int deleteByScoreLessThan(Double limit, ScoreRemark remark) {
        String sql = """
                DELETE FROM scores WHERE score < ? AND remark = ?
                """;
        return DBExecutor.executeUpdate(
                "删除低分记录且符合条件:(" + limit + "|" + remark.getRemark() + ")",
                sql,
                limit,
                remark.getRemark()
        );
    }

    @Override
    public int deleteByCourseNameAndScoreLessThan(String courseName, Double limit) {
        String sql = """
                DELETE FROM scores WHERE score < ?  AND course_id = (SELECT course_id FROM courses WHERE course_name = ?)
                """;
        return DBExecutor.executeUpdate(
                "删除" + courseName + "不及格成绩",
                sql,
                limit,
                courseName
        );
    }

    @Override
    public int deleteByStudentPhoneStartingWith(String phonePrefix) {
        String sql = """
                DELETE FROM scores  WHERE student_id IN (SELECT student_id FROM students WHERE phone LIKE ?)
                """;
        return DBExecutor.executeUpdate(
                "删除手机号前缀为" + phonePrefix + "的学生成绩",
                sql,
                phonePrefix + "%"
        );
    }

    @Override
    public List<Score> findByTeacherNameOrderByScoreDesc(String teacherName) {
        String sql = """
                SELECT s.* FROM scores s JOIN courses c ON s.course_id = c.course_id WHERE c.teacher = ? ORDER BY s.score DESC
                """;

        return DBExecutor.executeQuery(
                "查询" + teacherName + "老师所教课程的成绩",
                sql,
                this::mapRowToScore,
                teacherName
        );
    }

    @Override
    public List<Score> findByStudentNameContainingOrStudentNameContaining(String name1, String name2) {
        String sql = """
                SELECT sc.* FROM scores sc JOIN students st ON sc.student_id = st.student_id  WHERE st.student_name LIKE ? OR st.student_name LIKE ?
                """;

        return DBExecutor.executeQuery(
                "搜索名字包含的成绩记录: " + name1 + ", " + name2,
                sql,
                this::mapRowToScore,
                "%" + name1 + "%",
                "%" + name2 + "%"
        );
    }

    private Score mapRowToScore(ResultSet rs) throws SQLException {
        Score s = new Score();
        s.setScoreId(rs.getInt("score_id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setCourseId(rs.getInt("course_id"));
        s.setScore(rs.getDouble("score"));

        Timestamp ts = rs.getTimestamp("exam_time");
        if (ts != null) s.setExamTime(ts.toLocalDateTime());

        String remarkStr = rs.getString("remark");
        if (remarkStr != null) {
            try {
                s.setRemark(ScoreRemark.valueOf(remarkStr));
            } catch (IllegalArgumentException e) {
                s.setRemark(null);
            }
        }
        return s;
    }

    @Override
    public List<ScoreDetailDTO> findScoreDetailsByTeacher(String teacherName) {
        String sql = """
                SELECT stu.student_name, c.course_name, s.score FROM scores s
                JOIN students stu ON s.student_id = stu.student_id
                JOIN courses c ON s.course_id = c.course_id
                WHERE c.teacher = ? ORDER BY s.score DESC
                """;

        return DBExecutor.executeQuery(
                "查询" + teacherName + "学生的成绩详情",
                sql,
                (rs) -> new ScoreDetailDTO(
                        rs.getString("student_name"),
                        rs.getString("course_name"),
                        rs.getDouble("score")
                ),
                teacherName
        );
    }

    @Override
    public List<ScoreDetailDTO> findScoreDetailsByStudentNameKeywords(String k1, String k2) {
        String sql = """
                SELECT stu.student_name, c.course_name, s.score FROM scores s JOIN students stu ON s.student_id = stu.student_id
                JOIN courses c ON s.course_id = c.course_id WHERE stu.student_name LIKE ? OR stu.student_name LIKE ?
                """;

        return DBExecutor.executeQuery(
                "模糊查询学生成绩",
                sql,
                (rs) -> new ScoreDetailDTO(
                        rs.getString("student_name"),
                        rs.getString("course_name"),
                        rs.getDouble("score")
                ),
                "%" + k1 + "%", // 拼接 SQL 模糊查询通配符 %
                "%" + k2 + "%"
        );
    }
}