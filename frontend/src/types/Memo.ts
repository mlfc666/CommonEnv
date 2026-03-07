/**
 * 备忘录模型
 */
export interface Memo {
    id?: number;
    userId: number;
    title: string;
    content: string;
    tags?: string[];
    creatTime: number; // 对应后端 Memo.java 中的 creatTime
}