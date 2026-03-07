import type {LanguageInfo} from "./zh.ts";

const lang: LanguageInfo = {
    components: {
        navbar: {
            name: "Mindful Memo",
            Theme: "Themes"
        },
        CodeBlock: {
            copy: "Copy",
            copied: "Copied"
        },
        RemoteMarkdown: {
            loading: "Loading document...",
            error: "Failed to load document"
        },
        FileCard: {
            download: "• Click to download"
        }
    },
    sidebar: {
        website: "Introduction",
        memo: "Memos",
        md: "Markdown Lab",
        person: {
            main: "Account",
            info: "Profile",
            security: "Security",
        }
    },
    auth: {
        register: {
            title: "Create Account",
            subtitle: "Join us and start recording your ideas",
            username_label: "Username",
            username_placeholder: "Enter username",
            password_label: "Password",
            password_placeholder: "••••••••",
            submit: "Register",
            has_account: "Already have an account?",
            login_link: "Login here",
            cf_error: "Please complete the human verification"
        },
        login: {
            title: "Welcome Back",
            subtitle: "Please enter your credentials to access the panel",
            username_label: "Username",
            username_placeholder: "Enter your username",
            password_label: "Password",
            password_placeholder: "••••••••",
            submit: "Sign In",
            no_account: "Don't have an account?",
            register_link: "Register now",
            cf_error: "Please complete the human verification"
        }
    },
    person: {
        info: {
            avatar: {
                change: "Change Avatar",
                uid_prefix: "UID"
            },
            form: {
                title: "Account Details",
                subtitle: "View and manage your core account data",
                username: "Username",
                userid: "User ID",
                created_at: "Joined At",
                save: "Save Changes",
                success: "Profile updated successfully",
                invalid_length: "Username length must be between 3 and 16 characters",
            }
        },
        security: {
            password: {
                title: "Change Password",
                subtitle: "Update your password regularly to keep your account safe",
                old_label: "Current Password",
                new_label: "New Password",
                confirm_label: "Confirm New Password",
                submit: "Update Password",
                mismatch: "The new passwords do not match",
                success: "Password updated successfully",
                invalid_length: "Password length must be between 6 and 20 characters",
            },
            danger: {
                title: "Delete Account",
                subtitle: "Irreversible action, please proceed with caution",
                description: "Once deleted, all associated data (memos, avatars, etc.) will be permanently removed from the server and cannot be recovered.",
                submit: "Delete Account Permanently",
                confirm: "Are you sure you want to permanently delete your account? This action cannot be undone."
            },
            aside: {
                title: "Security Tips",
                suggestion1: "To maintain high security, we recommend changing your password at least every 90 days.",
                suggestion2: "When using shared devices, always remember to log out manually after your session."
            }
        }
    },
    memo: {
        filter: {
            search_placeholder: "Search keywords...",
            all_tags: "All Tags",
            all_time: "All Time",
            days_3: "Last 3 days",
            days_7: "Last 7 days",
            submit: "Search"
        },
        card: {
            no_title: "Untitled",
            no_content: "No content available..."
        },
        drawer: {
            title: "Edit Memo",
            subtitle: "Drafting Area",
            label_title: "Memo Title",
            placeholder_title: "Enter title...",
            label_content: "Content",
            placeholder_content: "Start writing your thoughts here...",
            label_tags: "Tags (Comma separated)",
            placeholder_tags: "e.g. Work, Urgent, Ideas",
            btn_delete: "Delete",
            btn_save: "Save Changes",
            save_success: "Saved successfully",
            delete_success: "Memo removed"
        },
        page: {
            new_btn: "New Memo",
            pagination_showing: "Showing",
            pagination_of: "of",
            pagination_memos: "memos",
            pagination_prev: "Previous",
            pagination_next: "Next"
        }
    },
    common: {
        error_401: "Session expired, please login again",
        error_network: "Network request failed, please check your connection"
    },
    language: "English",
    loading: "Loading..."
}
export default lang;