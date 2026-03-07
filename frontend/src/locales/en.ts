import type {LanguageInfo} from "./zh";

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
            username_placeholder: "Choose a username",
            password_label: "Password",
            password_placeholder: "••••••••",
            submit: "Register",
            has_account: "Already have an account?",
            login_link: "Login here",
            cf_error: "Please complete the human verification"
        }
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
                save: "Save Changes"
            }
        },
        security: {
            password: {
                title: "Change Password",
                subtitle: "Update your password regularly to keep your account safe",
                old_label: "Current Password",
                new_label: "New Password",
                confirm_label: "Confirm New Password",
                submit: "Update Password"
            },
            danger: {
                title: "Delete Account",
                subtitle: "Irreversible action, please proceed with caution",
                description: "Once deleted, all associated data (memos, avatars, etc.) will be permanently removed from the server and cannot be recovered.",
                submit: "Delete Account Permanently"
            },
            aside: {
                title: "Security Tips",
                suggestion1: "To maintain high security, we recommend changing your password at least every 90 days.",
                suggestion2: "When using shared devices, always remember to log out manually after your session."
            }
        }
    },
    language: "English",
    loading: "Loading..."
}

export default lang;