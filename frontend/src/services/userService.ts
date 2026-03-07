import type {ApiResponse} from "../types/ApiResponse.ts";
import type {UserInfoDTO} from "../types/UserInfoDTO.ts";
import type {PasswordUpdateDTO} from "../types/PasswordUpdateDTO.ts";
import type {LoginDTO} from "../types/LoginDTO.ts";


export const userService = {
    // 生产级 SHA-256 哈希算法
    async hashPassword(password: string): Promise<string> {
        const encoder = new TextEncoder();
        const data = encoder.encode(password);
        const hashBuffer = await window.crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    },

    // 登录请求
    async login(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },

    // 注册请求
    async register(payload: LoginDTO): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/register", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });
        return response.json();
    },
    // 获取当前登录用户详细信息
    async getUserInfo(): Promise<ApiResponse<UserInfoDTO>> {
        const response = await fetch("/api/user/info");
        return response.json();
    },

    // 上传头像文件并返回新地址
    async uploadAvatar(file: File): Promise<ApiResponse<string>> {
        const formData = new FormData();
        formData.append("avatar", file);
        const response = await fetch("/api/user/upload-avatar", {
            method: "POST",
            body: formData
        });
        return response.json();
    },

    // 更新用户密码
    async updatePassword(data: PasswordUpdateDTO): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/update-password", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        });
        return response.json();
    },

    // 注销当前账户
    async deleteAccount(): Promise<ApiResponse<string>> {
        const response = await fetch("/api/user/delete", {method: "POST"});
        return response.json();
    }
};