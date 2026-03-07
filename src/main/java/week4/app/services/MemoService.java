package week4.app.services;

import week4.app.dto.MemoInfoDTO;
import week4.app.models.Memo;
import week4.app.dto.MemoQueryDTO;
import java.util.List;

public interface MemoService {
    // 添加新的备忘录并记录创建时间
    Integer addMemo(Memo memo, Integer userId);

    // 验证归属权并删除指定备忘录
    void removeMemo(Integer id, Integer userId);

    // 覆盖更新现有备忘录的非空字段
    void updateMemo(Memo memo, Integer userId);

    // 执行组合条件过滤的分页查询
    List<Memo> getMemos(MemoQueryDTO query, Integer userId);

    MemoInfoDTO getMemoInfo(Integer userId);
}