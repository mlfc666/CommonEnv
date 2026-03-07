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
        person: {
            main: "用户相关",
            info: "个人信息",
            security: "安全设置",
        }
    },
    auth: {
        register: {
            title: "创建新账户",
            subtitle: "加入我们，开始记录您的奇思妙想",
            username_label: "设置用户名",
            username_placeholder: "输入用户名",
            password_label: "设置密码",
            password_placeholder: "••••••••",
            submit: "确认注册",
            has_account: "已有账号?",
            login_link: "点此登录",
            cf_error: "请完成人机验证"
        }
    },
    login: {
        title: "欢迎回来",
        subtitle: "请输入您的凭据以访问控制面板",
        username_label: "用户名",
        username_placeholder: "输入您的用户名",
        password_label: "密码",
        password_placeholder: "••••••••",
        submit: "登录系统",
        no_account: "还没有账号?",
        register_link: "立即注册",
        cf_error: "请完成人机验证"
    },
    person: {
        info: {
            avatar: {
                change: "更换头像",
                uid_prefix: "UID"
            },
            form: {
                title: "账户详细信息",
                subtitle: "查看并管理您的个人账户核心数据",
                username: "用户名",
                userid: "用户 ID",
                created_at: "注册时间",
                save: "保存修改",
                success: "资料修改成功"
            }
        },
        security: {
            password: {
                title: "修改密码",
                subtitle: "为了您的账号安全，建议定期更新密码",
                old_label: "原密码",
                new_label: "新密码",
                confirm_label: "确认新密码",
                submit: "保存密码",
                mismatch: "两次输入的新密码不一致",
                success: "密码修改成功"
            },
            danger: {
                title: "注销账号",
                subtitle: "该操作不可撤销，请谨慎处理",
                description: "注销后，所有与此账号关联的数据（备忘录、上传的头像等）将被永久从服务器移除，无法找回。",
                submit: "永久注销账号",
                confirm: "确定要永久注销账户吗？该操作将删除所有数据且无法恢复。"
            },
            aside: {
                title: "安全建议",
                suggestion1: "为了保持高强度防御，建议您至少每 90 天更换一次主密码。",
                suggestion2: "在共享设备上使用服务时，请记得在操作完成后手动执行退出登录。"
            }
        }
    },
    memo: {
        filter: {
            search_placeholder: "搜索关键词...",
            all_tags: "全部标签",
            all_time: "全部时间范围",
            days_3: "最近 3 天",
            days_7: "最近 7 天",
            submit: "搜索"
        },
        card: {
            no_title: "无标题",
            no_content: "暂无内容..."
        },
        drawer: {
            title: "编辑备忘录",
            subtitle: "Drafting Area",
            label_title: "备忘录标题",
            placeholder_title: "输入标题...",
            label_content: "详细内容",
            placeholder_content: "在这里开始记录...",
            label_tags: "标签 (用英文逗号分隔)",
            placeholder_tags: "例如: 工作, 紧急, 灵感",
            btn_delete: "删除",
            btn_save: "保存修改",
            save_success: "保存成功",
            delete_success: "已成功移除"
        },
        page: {
            new_btn: "新建备忘录",
            pagination_showing: "正在显示",
            pagination_of: "条，共",
            pagination_memos: "条记录",
            pagination_prev: "上一页",
            pagination_next: "下一页"
        }
    },
    common: {
        error_401: "身份验证已过期，请重新登录",
        error_network: "网络请求发生异常，请检查网络连接"
    },
    language: "简体中文",
    loading: "加载中..."
}

export type LanguageInfo = typeof lang;
export default lang;