const lang = {
    components: {
        navbar: {
            name: "觉知备忘录",
            Theme: "主题列表"
        },
        CodeBlock: {
            copy: "复制",
            copied: "已复制"
        },
        RemoteMarkdown: {
            loading: "文档加载中...",
            error: "文档加载失败"
        },
        FileCard: {
            download: "• 点击下载"
        }
    },
    sidebar: {
        website: "网站介绍",
        memo: "备忘录",
        md: "编写Markdown",
        person:{
            main:"用户相关",
            info: "个人信息",
            security: "安全设置",
        }

    },
    language: "简体中文",
    loading: "加载中..."
}

export type LanguageInfo = typeof lang;
export default lang;