package week4.app.repository;

import week4.app.models.Memo;
import week4.app.dto.MemoQueryDTO;
import java.util.List;
import java.util.Optional;

public interface MemoRepository {
    // 持久化单条备忘录数据
    Integer create(Memo memo);

    // 根据主键及所属用户ID物理删除记录
    void deleteById(Integer id, Integer userId);

    // 更新已存在的备忘录内容及标签
    void update(Memo memo, Integer userId);

    Optional<Memo> findById(Integer id, Integer userId);

    // 执行组合条件过滤的分页查询
    List<Memo> findByQuery(MemoQueryDTO query, Integer userId);

}