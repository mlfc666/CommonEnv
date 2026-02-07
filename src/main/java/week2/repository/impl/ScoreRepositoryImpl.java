package week2.repository.impl;

import week2.enums.ScoreRemark;
import week2.models.Score;
import week2.repository.ScoreRepository;
import week2.utils.DBExecutor;

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
                score.getRemark() != null ? score.getRemark().name() : null
        );
        return score;
    }

    @Override
    public void update(Score score) {
        String sql = """
                UPDATE scores SET score = ?, remark = ? WHERE score_id = ?
                """;
        DBExecutor.executeUpdate(
                "更新成绩-ID:" + score.getScoreId(),
                sql,
                score.getScore(),
                score.getRemark() != null ? score.getRemark().name() : null,
                score.getScoreId()
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
    public List<Score> findByCourseTeacherNameOrderByScoreDesc(String teacherName) {
        String sql = """
                SELECT s.* FROM scores s JOIN courses c ON s.course_id = c.course_id WHERE c.teacher = ?  ORDER BY s.score DESC
                """;

        return DBExecutor.executeQuery(
                "查询" + teacherName + "老师所教课程的成绩",
                sql,
                rs -> {
                    Score s = new Score();
                    s.setScoreId(rs.getInt("score_id"));
                    s.setStudentId(rs.getInt("student_id"));
                    s.setCourseId(rs.getInt("course_id"));
                    s.setScore(rs.getDouble("score"));
                    s.setExamTime(rs.getTimestamp("exam_time").toLocalDateTime());
                    String remarkStr = rs.getString("remark");
                    if (remarkStr != null) s.setRemark(ScoreRemark.valueOf(remarkStr));
                    return s;
                },
                teacherName
        );
    }

    @Override
    public List<Score> findByStudentNameContainingOrStudentNameContaining(String name1, String name2) {
        String sql = """
                SELECT sc.* FROM scores sc JOIN students st ON sc.student_id = st.student_id WHERE st.student_name LIKE ? OR st.student_name LIKE ?
                """;

        return DBExecutor.executeQuery(
                "搜索包含关键词的成绩记录: " + name1 + ", " + name2,
                sql,
                rs -> {
                    Score s = new Score();
                    s.setScoreId(rs.getInt("score_id"));
                    s.setStudentId(rs.getInt("student_id"));
                    s.setCourseId(rs.getInt("course_id"));
                    s.setScore(rs.getDouble("score"));
                    s.setExamTime(rs.getTimestamp("exam_time").toLocalDateTime());
                    String remarkStr = rs.getString("remark");
                    if (remarkStr != null) s.setRemark(ScoreRemark.valueOf(remarkStr));
                    return s;
                },
                "%" + name1 + "%",
                "%" + name2 + "%"
        );
    }
}