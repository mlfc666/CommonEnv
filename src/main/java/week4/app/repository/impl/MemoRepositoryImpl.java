package week4.app.repository.impl;

import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.repository.MemoRepository;

import java.util.List;

public class MemoRepositoryImpl implements MemoRepository {
    @Override
    public Integer create(Memo memo) {
        return 0;
    }

    @Override
    public void deleteById(Integer id, Integer userId) {

    }

    @Override
    public void update(Memo memo, Integer userId) {

    }

    @Override
    public List<Memo> findByQuery(MemoQueryDTO query, Integer userId) {
        return List.of();
    }
}

