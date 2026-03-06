package week4.app.services.impl;

import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.repository.MemoRepository;
import week4.app.services.MemoService;
import week4.framework.exception.BadRequestException;
import week4.framework.exception.NotFoundException;

import java.util.List;

public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;

    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Override
    public Integer addMemo(Memo memo, Integer userId) {
        // 强制关联当前登录用户 ID，防止越权存储
        memo.setUserId(userId);
        // 统一使用服务器生成的 13 位毫秒级时间戳
        memo.setCreatTime(System.currentTimeMillis());
        return memoRepository.create(memo);
    }

    @Override
    public void removeMemo(Integer id, Integer userId) {
        // 先检查是否存在且属于该用户，否则抛出 404
        memoRepository.findById(id, userId)
                .orElseThrow(() -> new NotFoundException("备忘录不存在或无权操作"));
        memoRepository.deleteById(id, userId);
    }

    @Override
    public void updateMemo(Memo memo, Integer userId) {
        if (memo.getId() == null) {
            throw new BadRequestException("更新操作必须提供 ID");
        }
        // 确保用户只能修改自己的备忘录
        memoRepository.findById(memo.getId(), userId)
                .orElseThrow(() -> new NotFoundException("待更新的备忘录不存在"));
        memoRepository.update(memo, userId);
    }

    @Override
    public List<Memo> getMemos(MemoQueryDTO query, Integer userId) {
        // 调用仓库层的组合查询逻辑
        return memoRepository.findByQuery(query, userId);
    }
}