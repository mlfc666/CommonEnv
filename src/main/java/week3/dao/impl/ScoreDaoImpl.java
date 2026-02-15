package week3.dao.impl;

import common.utils.DBExecutor;
import week3.dao.ScoreDao;
import week3.entity.Score;

import java.util.List;

public class ScoreDaoImpl implements ScoreDao {

    @Override
    public Score insert(Score score) {
        String sql = """
                INSERT INTO score (student_id, subject, score, exam_time)
                VALUES (?, ?, ?, ?)
                """;

        DBExecutor.executeUpdate(
                "插入单条学生成绩-学生ID:" + score.getStudentId(),
                sql,
                score.getStudentId(),
                score.getSubject(),
                score.getScore(),
                score.getExamTime()
        );
        return score;
    }

    @Override
    public int deleteByStudentId(Integer studentId) {
        String sql = """
                DELETE FROM score WHERE student_id = ?
                """;

        return DBExecutor.executeUpdate(
                "删除学生及其所有成绩-学生ID:" + studentId,
                sql,
                studentId
        );
    }

    @Override
    public List<Score> findByStudentNo(String studentNo) {
        String sql = """
                SELECT sc.* FROM score sc
                JOIN student st ON sc.student_id = st.id
                WHERE st.student_no = ?
                """;
        return DBExecutor.executeQuery(
                "根据学号查询成绩-学号:" + studentNo,
                sql,
                (rs) -> {
                    Score s = new Score();
                    s.setId(rs.getInt("id"));
                    s.setStudentId(rs.getInt("student_id"));
                    s.setSubject(rs.getString("subject"));
                    s.setScore(rs.getDouble("score"));
                    s.setExamTime(rs.getDate("exam_time").toLocalDate());
                    return s;
                },
                studentNo
        );
    }
}