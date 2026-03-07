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
        }
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
                save: "保存修改",
                success: "資料修改成功",
                invalid_length: "用戶名長度必須在 3 到 16 個字符之間",
            }
        },
        security: {
            password: {
                title: "修改密碼",
                subtitle: "為了您的賬號安全，建議定期更新密碼",
                old_label: "原密碼",
                new_label: "新密碼",
                confirm_label: "確認新密碼",
                submit: "保存密碼",
                mismatch: "兩次輸入的新密碼不一致",
                success: "密碼修改成功",
                invalid_length: "密碼長度必須在 6 到 20 個字符之間",
            },
            danger: {
                title: "註銷賬號",
                subtitle: "該操作不可撤銷，請謹慎處理",
                description: "註銷後，所有與此賬號關聯的數據（備忘錄、上傳的頭像等）將被永久從服務器移除，無法找回。",
                submit: "永久註銷賬號",
                confirm: "確定要永久註銷賬戶嗎？該操作將刪除所有數據且無法恢復。"
            },
            aside: {
                title: "安全建議",
                suggestion1: "為了保持高強度防禦，建議您至少每 90 天更換一次主密碼。",
                suggestion2: "在共享設備上使用服務時，請記得在操作完成後手動執行退出登錄。"
            }
        }
    },
    memo: {
        filter: {
            search_placeholder: "搜索關鍵詞...",
            all_tags: "全部標簽",
            all_time: "全部時間範圍",
            days_3: "最近 3 天",
            days_7: "最近 7 天",
            submit: "搜索"
        },
        card: {
            no_title: "無標題",
            no_content: "暫無內容..."
        },
        drawer: {
            title: "編輯備忘錄",
            subtitle: "Drafting Area",
            label_title: "備忘錄標題",
            placeholder_title: "輸入標題...",
            label_content: "詳細內容",
            placeholder_content: "在這裏開始記錄...",
            label_tags: "標簽 (用英文逗號分隔)",
            placeholder_tags: "例如: 工作, 緊急, 靈感",
            btn_delete: "刪除",
            btn_save: "保存修改",
            save_success: "保存成功",
            delete_success: "已成功移除"
        },
        page: {
            new_btn: "新建備忘錄",
            pagination_showing: "正在顯示",
            pagination_of: "條，共",
            pagination_memos: "條記錄",
            pagination_prev: "上一頁",
            pagination_next: "下一頁"
        }
    },
    common: {
        error_401: "身份驗證已過期，請重新登錄",
        error_network: "網絡請求發生異常，請檢查網絡連接"
    },
    language: "簡體中文",
    loading: "加載中..."
}
export default lang;