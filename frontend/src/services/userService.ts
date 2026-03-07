import type {ApiResponse} from "../types/ApiResponse.ts";
import type {UserInfoDTO} from "../types/UserInfoDTO.ts";
import type {PasswordUpdateDTO} from "../types/PasswordUpdateDTO.ts";
import type {LoginDTO} from "../types/LoginDTO.ts";

// 动态计算 API 基础路径
const getApiUrl = (path: string) => {
    const pathname = window.location.pathname;
    // 匹配 /web- 开头跟随任意非斜杠字符的路径段
    const match = pathname.match(/\/web-[^/]+/);
    // 如果匹配到了前缀则使用前缀，否则（如在 localhost）使用根路径 /
    const base = match ? match[0] + '/' : '/';
    return base + path;
};

export const userService = {
    async hashPassword(password: string): Promise<string> {
        const encoder = new TextEncoder();
        const data = encoder.encode(password);
        const hashBuffer = await window.crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    },

    async login(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch(getApiUrl("api/user/login"), {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },

    async register(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch(getApiUrl("api/user/register"), {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },

    async getUserInfo(): Promise<ApiResponse<UserInfoDTO>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/user/info"), {
            headers: {"Authorization": `Bearer ${token}`}
        });
        return response.json();
    },

    async uploadAvatar(file: File): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const formData = new FormData();
        formData.append("avatar", file);
        const response = await fetch(getApiUrl("api/user/upload-avatar"), {
            method: "POST",
            headers: {"Authorization": `Bearer ${token}`},
            body: formData
        });
        return response.json();
    },

    async updateUsername(username: string): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/user/update-username"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({username})
        });
        return response.json();
    },

    async updatePassword(data: PasswordUpdateDTO): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/user/update-password"), {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        return response.json();
    },

    async deleteAccount(): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch(getApiUrl("api/user/delete"), {
            method: "POST",
            headers: {"Authorization": `Bearer ${token}`}
        });
        return response.json();
    }
};