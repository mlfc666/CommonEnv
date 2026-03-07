/**
 * 登录/注册请求 DTO
 */
export interface LoginDTO {
    username: string;
    password: string; // 前端哈希后的字符串
    cfToken: string;
}