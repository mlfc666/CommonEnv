package week3.dao.impl;

import week3.dao.ScoreDao;
import week3.entity.Score;

import java.util.List;

public class ScoreDaoImpl implements ScoreDao {
    @Override
    public int insert(Score score) {
        return 0;
    }

    @Override
    public int deleteByStudentId(Integer studentId) {
        return 0;
    }

    @Override
    public List<Score> findByStudentNo(String studentNo) {
        return List.of();
    }
}
