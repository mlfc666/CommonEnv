import type {MemoQueryDTO} from "../types/MemoQueryDTO.ts";
import type {ApiResponse} from "../types/ApiResponse.ts";
import type {Memo} from "../types/Memo.ts";


export const memoService = {
    // 分页获取备忘录列表
    async getMemos(query: MemoQueryDTO): Promise<ApiResponse<Memo[]>> {
        const response = await fetch("/api/memo/list", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(query)
        });
        return response.json();
    },

    // 创建新备忘录
    async createMemo(memo: Memo): Promise<ApiResponse<number>> {
        const response = await fetch("/api/memo/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    // 更新已有备忘录
    async updateMemo(memo: Memo): Promise<ApiResponse<string>> {
        const response = await fetch("/api/memo/update", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    // 删除备忘录
    async deleteMemo(id: number): Promise<ApiResponse<string>> {
        const response = await fetch(`/api/memo/delete?id=${id}`, {
            method: "POST"
        });
        return response.json();
    }
};