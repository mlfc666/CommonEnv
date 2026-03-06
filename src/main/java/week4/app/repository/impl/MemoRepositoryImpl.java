package week4.app.repository.impl;

import common.utils.DBExecutor;
import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.repository.MemoRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoRepositoryImpl implements MemoRepository {

    @Override
    public Integer create(Memo memo) {
        String sql = "INSERT INTO memos (title, content, tags, create_time, user_id) VALUES (?, ?, ?, ?, ?)";
        // 将标签数组转换为逗号分隔的字符串存储
        String tagsStr = memo.getTags() != null ? String.join(",", memo.getTags()) : "";

        return DBExecutor.executeUpdate(
                "创建备忘录:" + memo.getTitle(),
                sql,
                memo.getTitle(),
                memo.getContent(),
                tagsStr,
                memo.getCreatTime(),
                memo.getUserId()
        );
    }

    @Override
    public void deleteById(Integer id, Integer userId) {
        String sql = "DELETE FROM memos WHERE id = ? AND user_id = ?";
        DBExecutor.executeUpdate("删除备忘录ID:" + id, sql, id, userId);
    }

    @Override
    public void update(Memo memo, Integer userId) {
        String sql = "UPDATE memos SET title = ?, content = ?, tags = ? WHERE id = ? AND user_id = ?";
        String tagsStr = memo.getTags() != null ? String.join(",", memo.getTags()) : "";

        DBExecutor.executeUpdate(
                "更新备忘录ID:" + memo.getId(),
                sql,
                memo.getTitle(),
                memo.getContent(),
                tagsStr,
                memo.getId(),
                userId
        );
    }

    @Override
    public List<Memo> findByQuery(MemoQueryDTO query, Integer userId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM memos WHERE user_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 标题或内容关键字模糊搜索
        if (query.keyword() != null && !query.keyword().isBlank()) {
            sql.append(" AND (title LIKE ? OR content LIKE ?)");
            String pattern = "%" + query.keyword() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        // 标签精确匹配 (使用 LIKE 处理逗号分隔字符串)
        if (query.tag() != null && !query.tag().isBlank()) {
            sql.append(" AND tags LIKE ?");
            params.add("%" + query.tag() + "%");
        }

        // 时间筛选逻辑
        if (query.days() != null) {
            long startTime = System.currentTimeMillis() - (query.days() * 24L * 60 * 60 * 1000);
            sql.append(" AND create_time >= ?");
            params.add(startTime);
        }

        // 分页逻辑
        sql.append(" ORDER BY create_time DESC LIMIT ? OFFSET ?");
        int offset = (query.page() - 1) * query.size();
        params.add(query.size());
        params.add(offset);

        return DBExecutor.executeQuery(
                "组合条件查询备忘录",
                sql.toString(),
                this::mapRowToMemo,
                params.toArray()
        );
    }

    // 将数据库结果集映射为实体对象
    private Memo mapRowToMemo(ResultSet rs) throws SQLException {
        Memo memo = new Memo();
        memo.setId(rs.getInt("id"));
        memo.setTitle(rs.getString("title"));
        memo.setContent(rs.getString("content"));
        memo.setCreatTime(rs.getLong("create_time"));
        memo.setUserId(rs.getInt("user_id"));

        // 将存储的逗号分隔字符串转回数组
        String tagsStr = rs.getString("tags");
        if (tagsStr != null && !tagsStr.isEmpty()) {
            memo.setTags(tagsStr.split(","));
        } else {
            memo.setTags(new String[0]);
        }
        return memo;
    }
}