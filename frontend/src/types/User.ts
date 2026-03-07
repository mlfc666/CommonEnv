/**
 * 用户模型
 */
export interface User {
    id: number;
    username: string;
    password?: string; // 通常后端不应返回密码，设为可选
    avatar?: string;
    createTime: number;
    logoutTime?: number;
}