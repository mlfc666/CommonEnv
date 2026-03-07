import type {MemoQueryDTO} from "../types/MemoQueryDTO.ts";
import type {ApiResponse} from "../types/ApiResponse.ts";
import type {Memo} from "../types/Memo.ts";
import type {MemoInfoDTO} from "../types/MemoInfoDTO.ts";

// 动态计算 API 基础路径
const getApiUrl = (path: string) => {
    const pathname = window.location.pathname;
    // 匹配 /web- 开头跟随任意非斜杠字符的路径段
    const match = pathname.match(/\/web-[^/]+/);
    // 如果匹配到了前缀则使用前缀，否则（如在 localhost）使用根路径 /
    const base = match ? match[0] + '/' : '/';
    return base + path;
};

export const memoService = {
    async getMemos(query: MemoQueryDTO): Promise<ApiResponse<Memo[]>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/memo/list"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(query)
        });
        return response.json();
    },

    async createMemo(memo: Memo): Promise<ApiResponse<number>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/memo/create"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    async updateMemo(memo: Memo): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/memo/update"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(memo)
        });
        return response.json();
    },

    async deleteMemo(id: number): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        // 注意：Query 参数拼接在 URL 后面
        const response = await fetch(getApiUrl(`api/memo/delete?id=${id}`), {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return response.json();
    },

    async getMemoInfo(): Promise<ApiResponse<MemoInfoDTO>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/memo/info"), {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return response.json();
    }
};