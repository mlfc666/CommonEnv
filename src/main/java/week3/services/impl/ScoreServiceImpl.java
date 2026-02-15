package week3.services.impl;

import week2.exception.IllegalScoreException;
import week3.dao.ScoreDao;
import week3.entity.Score;
import week3.services.ScoreService;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    private final ScoreDao scoreDao;

    public ScoreServiceImpl(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    @Override
    public void addScore(Score score) {
        if (score.getScore() > 100 || score.getScore() < 0) {
            throw new IllegalScoreException("成绩必须在 0 到 100 之间");
        }
        scoreDao.insert(score);
    }

    @Override
    public List<Score> findScoresByStudentNo(String studentNo) {
        return scoreDao.findByStudentNo(studentNo);
    }
}
