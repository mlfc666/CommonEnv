package week4.app.services.impl;

import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.repository.MemoRepository;
import week4.app.services.MemoService;

import java.util.List;

public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;

    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Override
    public Integer addMemo(Memo memo, Integer userId) {
        return 0;
    }

    @Override
    public void removeMemo(Integer id, Integer userId) {

    }

    @Override
    public void updateMemo(Memo memo, Integer userId) {

    }

    @Override
    public List<Memo> getMemos(MemoQueryDTO query, Integer userId) {
        return List.of();
    }
}

