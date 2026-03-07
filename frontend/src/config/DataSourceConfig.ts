// 资源映射表配置
export const DataSource = {
    // 资源基础路径
    RES_BASE: "https://res.mlfc.moe",
    // 新增：Markdown 文件基准路径
    MD_BASE: "https://md.mlfc.moe",
    FILE_SERVER: "file.mlfc.moe"
};

export function getQQImage(qq: string | number) {
    return `https://q1.qlogo.cn/g?b=qq&nk=${qq}&s=100`;
}