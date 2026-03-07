package week4.app.repository;

import week4.app.models.Memo;
import week4.app.dto.MemoQueryDTO;
import java.util.List;
import java.util.Optional;

public interface MemoRepository {
    List<Memo> findAll();

    // 持久化单条备忘录数据
    Integer create(Memo memo);

    // 根据主键及所属用户ID物理删除记录
    int deleteById(Integer id, Integer userId);

    // 更新已存在的备忘录内容及标签
    int update(Memo memo, Integer userId);

    Optional<Memo> findById(Integer id, Integer userId);

    // 执行组合条件过滤的分页查询
    List<Memo> findByQuery(MemoQueryDTO query, Integer userId);

    // 获取指定用户的备忘录记录总数
    int countByUserId(Integer userId);

    // 获取指定用户所有备忘录的原始标签字符串列表
    List<String> findTagsByUserId(Integer userId);

    // 删除指定用户所有备忘录
    int deleteByUserId(Integer userId);
}