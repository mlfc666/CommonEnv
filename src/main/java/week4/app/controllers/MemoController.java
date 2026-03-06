package week4.app.controllers;

import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.services.MemoService;
import week4.framework.annotations.*;

import java.util.List;

@RestController
@RequiresAuth // 该控制器下所有接口均需 JWT 鉴权
@SuppressWarnings("unused")
public class MemoController {

    @Inject
    private MemoService memoService;

    /**
     * 添加备忘录
     * @param memo 包含标题、内容、标签等信息
     * @param userId 自动从 Token 中提取当前登录用户 ID
     */
    @PostMapping("/api/memo/create")
    public Integer create(@RequestBody Memo memo, @AuthClaim("uid") Integer userId) {
        // 调用 Service，内部会自动关联 userId 并生成创建时间
        return memoService.addMemo(memo, userId);
    }

    /**
     * 更新备忘录
     * @param memo 必须包含 ID 字段
     */
    @PostMapping("/api/memo/update")
    public String update(@RequestBody Memo memo, @AuthClaim("uid") Integer userId) {
        memoService.updateMemo(memo, userId);
        return "更新成功";
    }

    /**
     * 删除备忘录
     * @param id 备忘录的主键 ID
     */
    @PostMapping("/api/memo/delete")
    public String delete(@RequestParam("id") Integer id, @AuthClaim("uid") Integer userId) {
        memoService.removeMemo(id, userId);
        return "已成功删除";
    }

    /**
     * 获取备忘录列表
     * @param query 组合查询 DTO，支持关键字搜索、标签筛选、时间筛选及分页
     */
    @PostMapping("/api/memo/list") // 改为 POST 以支持复杂的 MemoQueryDTO 查询对象
    public List<Memo> list(@RequestBody MemoQueryDTO query, @AuthClaim("uid") Integer userId) {
        // 仅查询属于当前用户的数据
        return memoService.getMemos(query, userId);
    }
}