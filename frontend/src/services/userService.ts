import type {ApiResponse} from "../types/ApiResponse.ts";
import type {UserInfoDTO} from "../types/UserInfoDTO.ts";
import type {PasswordUpdateDTO} from "../types/PasswordUpdateDTO.ts";
import type {LoginDTO} from "../types/LoginDTO.ts";

export const userService = {
    // 使用 Web Crypto API 执行生产级 SHA-256 哈希算法
    async hashPassword(password: string): Promise<string> {
        const encoder = new TextEncoder();
        const data = encoder.encode(password);
        const hashBuffer = await window.crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    },

    // 提交登录凭据并返回身份验证令牌
    async login(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },

    // 提交注册信息并返回身份验证令牌
    async register(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/register", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },

    // 获取当前通过身份验证的完整用户资料
    async getUserInfo(): Promise<ApiResponse<UserInfoDTO>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/user/info", {
            headers: {"Authorization": `Bearer ${token}`}
        });
        return response.json();
    },

    // 执行头像文件上传并在成功后返回访问路径
    async uploadAvatar(file: File): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const formData = new FormData();
        formData.append("avatar", file);
        const response = await fetch("/api/user/upload-avatar", {
            method: "POST",
            headers: {"Authorization": `Bearer ${token}`},
            body: formData
        });
        return response.json();
    },

    // 更新当前用户的显示名称
    async updateUsername(username: string): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/user/update-username", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({username})
        });
        return response.json();
    },

    // 校验旧密码并设置新的账户登录密码
    async updatePassword(data: PasswordUpdateDTO): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/user/update-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        return response.json();
    },

    // 永久从系统中移除当前用户的账户与所有相关数据
    async deleteAccount(): Promise<ApiResponse<string>> {
        const token = localStorage.getItem("jwt_token");
        const response = await fetch("/api/user/delete", {
            method: "POST",
            headers: {"Authorization": `Bearer ${token}`}
        });
        return response.json();
    }
};