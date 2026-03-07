import type {MemoQueryDTO} from "../types/MemoQueryDTO.ts";
import type {ApiResponse} from "../types/ApiResponse.ts";
import type {Memo} from "../types/Memo.ts";

export const memoService = {
    // 根据组合查询条件分页获取当前用户的备忘录列表
    async getMemos(query: MemoQueryDTO): Promise<ApiResponse<Memo[]>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/memo/list", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(query)
        });
        return response.json();
    },

    // 提交新的备忘录数据并在成功后返回其唯一标识符
    async createMemo(memo: Memo): Promise<ApiResponse<number>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/memo/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    // 覆盖更新已有的备忘录记录并返回操作执行结果
    async updateMemo(memo: Memo): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/memo/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    // 通过唯一编号从系统中永久删除指定的备忘录记录
    async deleteMemo(id: number): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(`/api/memo/delete?id=${id}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return response.json();
    },

    // 获取当前用户在所有备忘录中创建的标签集合
    async getAllTags(): Promise<ApiResponse<string[]>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/memo/tags", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return response.json();
    }
};