package week4.app.repository.impl;

import common.utils.DBExecutor;
import week4.app.dto.MemoQueryDTO;
import week4.app.models.Memo;
import week4.app.repository.MemoRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoRepositoryImpl implements MemoRepository {

    @Override
    public List<Memo> findAll() {
        String sql = "SELECT * FROM memos";
        return DBExecutor.executeQuery(
                "获取全量备忘录数据",
                sql,
                this::mapRowToMemo
        );
    }

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
    public int deleteById(Integer id, Integer userId) {
        String sql = "DELETE FROM memos WHERE id = ? AND user_id = ?";
        return DBExecutor.executeUpdate("删除备忘录ID:" + id, sql, id, userId);
    }

    @Override
    public int update(Memo memo, Integer userId) {
        String sql = "UPDATE memos SET title = ?, content = ?, tags = ? WHERE id = ? AND user_id = ?";
        String tagsStr = memo.getTags() != null ? String.join(",", memo.getTags()) : "";

        return DBExecutor.executeUpdate(
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
    public Optional<Memo> findById(Integer id, Integer userId) {
        String sql = "SELECT * FROM memos WHERE id = ? AND user_id = ?";
        List<Memo> results = DBExecutor.executeQuery(
                "按ID查询备忘录",
                sql,
                this::mapRowToMemo,
                id,
                userId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
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

    @Override
    public int countByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM memos WHERE user_id = ?";
        List<Integer> results = DBExecutor.executeQuery(
                "统计用户备忘录总数",
                sql,
                rs -> rs.getInt(1),
                userId
        );
        return results.isEmpty() ? 0 : results.get(0);
    }

    @Override
    public List<String> findTagsByUserId(Integer userId) {
        String sql = "SELECT tags FROM memos WHERE user_id = ?";
        return DBExecutor.executeQuery(
                "查询用户原始标签列表",
                sql,
                rs -> rs.getString("tags"),
                userId
        );
    }

    @Override
    public int deleteByUserId(Integer userId) {
        String sql = "DELETE FROM memos WHERE user_id = ?";
        return DBExecutor.executeUpdate("清理注销用户的备忘录记录", sql, userId);
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