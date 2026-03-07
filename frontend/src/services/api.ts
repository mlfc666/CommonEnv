import type {PostMeta, SiteConfig} from "../types";

// 封装 API 调用
export const api = {
    getPosts: async (): Promise<(PostMeta & { filename: string })[]> => {
        const res = await fetch('/api/posts');
        return res.json();
    },

    getPost: async (filename: string): Promise<{ raw: string }> => {
        const res = await fetch(`/api/post?filename=${filename}`);
        return res.json();
    },

    savePost: async (filename: string, content: string, meta: PostMeta) => {
        const res = await fetch('/api/post', {
            method: 'POST',
            body: JSON.stringify({ filename, content, meta })
        });
        return res.json();
    },

    analyzeContent: async (content: string) => {
        const res = await fetch('/api/ai/analyze', {
            method: 'POST',
            body: JSON.stringify({ content })
        });
        return res.json();
    },

    getConfig: async (): Promise<SiteConfig> => {
        const res = await fetch('/api/config');
        return res.json();
    },

    saveConfig: async (config: SiteConfig) => {
        await fetch('/api/config', {
            method: 'POST',
            body: JSON.stringify(config)
        });
    }
};