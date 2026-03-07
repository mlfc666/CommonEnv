import type {LanguageInfo} from "./zh.ts";

const lang: LanguageInfo = {
    components: {
        navbar: {
            name: "覺知備忘錄",
            Theme: "主題列表"
        },
        CodeBlock: {
            copy: "復製",
            copied: "已復製"
        },
        RemoteMarkdown: {
            loading: "文檔加載中...",
            error: "文檔加載失敗"
        },
        FileCard: {
            download: "• 點擊下載"
        }
    },
    sidebar: {
        website: "網站介紹",
        memo: "備忘錄",
        md: "編寫Markdown",
        person: {
            main: "用戶相關",
            info: "個人信息",
            security: "安全設置",
        }

    },
    auth: {
        register: {
            title: "創建新賬戶",
            subtitle: "加入我們，開始記錄您的奇思妙想",
            username_label: "設置用戶名",
            username_placeholder: "輸入用戶名",
            password_label: "設置密碼",
            password_placeholder: "••••••••",
            submit: "確認註冊",
            has_account: "已有賬號?",
            login_link: "點此登錄",
            cf_error: "請完成人機驗證"
        }
    },
    login: {
        title: "歡迎回來",
        subtitle: "請輸入您的憑據以訪問控製面板",
        username_label: "用戶名",
        username_placeholder: "輸入您的用戶名",
        password_label: "密碼",
        password_placeholder: "••••••••",
        submit: "登錄系統",
        no_account: "還沒有賬號?",
        register_link: "立即註冊",
        cf_error: "請完成人機驗證"
    },
    person: {
        info: {
            avatar: {
                change: "更換頭像",
                uid_prefix: "UID"
            },
            form: {
                title: "賬戶詳細信息",
                subtitle: "查看並管理您的個人賬戶核心數據",
                username: "用戶名",
                userid: "用戶 ID",
                created_at: "註冊時間",
                save: "保存修改"
            }
        },
        security: {
            password: {
                title: "修改密碼",
                subtitle: "為了您的賬號安全，建議定期更新密碼",
                old_label: "原密碼",
                new_label: "新密碼",
                confirm_label: "確認新密碼",
                submit: "保存密碼"
            },
            danger: {
                title: "註銷賬號",
                subtitle: "該操作不可撤銷，請謹慎處理",
                description: "註銷後，所有與此賬號關聯的數據（備忘錄、上傳的頭像等）將被永久從服務器移除，無法找回。",
                submit: "永久註銷賬號"
            },
            aside: {
                title: "安全建議",
                suggestion1: "為了保持高強度防禦，建議您至少每 90 天更換一次主密碼。",
                suggestion2: "在共享設備上使用服務時，請記得在操作完成後手動執行退出登錄。"
            }
        }
    },
    language: "繁體中文",
    loading: "加載中..."
}
export default lang;