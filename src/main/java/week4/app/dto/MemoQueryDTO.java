package week4.app.dto;


/**
 * 备忘录搜索查询 Record
 *
 * @param keyword 搜索关键字 (标题或内容)
 * @param tag     标签分类
 * @param days    筛选天数 (3/7)
 * @param page    当前页码
 * @param size    每页条数
 */
public record MemoQueryDTO(
        String keyword,
        String tag,
        Integer days,
        Integer page,
        Integer size
) {
    public MemoQueryDTO {
        if (page == null) page = 1;
        if (size == null) size = 10;
    }
}