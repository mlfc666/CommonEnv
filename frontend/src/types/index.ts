export interface PostMeta {
    title: string;
    date: string;
    tags?: string[];
    summary?: string;
    theme?: string; // 文章专属主题
    category?: string;
}

export interface Post {
    filename: string;
    content: string; // 纯 Markdown 内容
    meta: PostMeta;
}

export interface SiteConfig {
    title: string;
    description: string;
    defaultTheme: string;
    aiEnabled: boolean;
}