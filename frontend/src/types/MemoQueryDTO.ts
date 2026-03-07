/**
 * 备忘录查询 DTO
 */
export interface MemoQueryDTO {
    keyword?: string;
    tag?: string;
    days?: number;
    page: number;
    size: number;
}