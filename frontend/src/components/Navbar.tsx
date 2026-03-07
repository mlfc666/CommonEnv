import {useTranslation} from "react-i18next";
import {setTheme, type ThemeInfo, themeOptions} from "../manager/ThemeManger.ts";
import {type LanguageInfo, languageOptions} from "../manager/i18n.ts";
import type {i18n} from "i18next";
import {HandRaisedIcon, LanguageIcon, SparklesIcon} from "@heroicons/react/24/outline";
import type {User} from "../types/User.ts";

// 主题下拉
function ThemeDropdown() {
    const {t} = useTranslation();
    return (
        // 移除了 dropdown-hover，改为普通 dropdown
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <SparklesIcon className="w-6 h-6"/>
                <span className="hidden sm:inline">{t("components.navbar.Theme")}</span>
            </button>
            <ul tabIndex={0}
                className="dropdown-content menu bg-base-200 rounded-box w-52 p-2 shadow-2xl border border-base-300 mt-2">
                {themeOptions.map((theme: ThemeInfo) => (
                    <li key={theme.value}>
                        <button onClick={() => setTheme(theme.value)} className="btn btn-sm btn-ghost justify-start">
                            {theme.label}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

// 语言下拉
function LanguageDropdown({i18n}: { i18n: i18n }) {
    const {t} = useTranslation();
    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <LanguageIcon className="w-6 h-6"/>
                <span className="hidden sm:inline">{t("language")}</span>
            </button>
            <ul tabIndex={0}
                className="dropdown-content menu bg-base-200 rounded-box w-40 p-2 shadow-2xl border border-base-300 mt-2">
                {languageOptions.map((lang: LanguageInfo) => (
                    <li key={lang.language}>
                        <button onClick={() => void i18n.changeLanguage(lang.language)}
                                className="btn btn-sm btn-ghost justify-start">
                            {lang.label}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

// 用户头像下拉
function UserDropdown({user}: { user: User }) {
    const formatDate = (ts: number) => new Date(ts).toLocaleDateString();

    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar ml-2 border border-base-300">
                <div className="w-9 rounded-full">
                    <img alt="User Avatar" src={user.avatar}/>
                </div>
            </button>

            <ul tabIndex={0}
                className="menu dropdown-content p-2 shadow-2xl bg-base-200 rounded-box w-64 border border-base-300 mt-2">
                <li className="menu-title px-4 pt-2 pb-1 border-b border-base-300">
                    <div className="flex flex-col gap-1">
                        <span className="text-lg font-bold text-base-content">{user.username}</span>
                        <div className="flex justify-between text-xs opacity-60 font-mono">
                            <span>UID: {user.id}</span>
                        </div>
                        <span className="text-xs opacity-60">注册时间: {formatDate(user.createTime)}</span>
                    </div>
                </li>
                <li className="mt-1">
                    <button className="text-error hover:bg-error/10">
                        <HandRaisedIcon className="w-5 h-5"/>
                        退出登录
                    </button>
                </li>
            </ul>
        </div>
    );
}

// 主组件
export function Navbar() {
    const {t, i18n} = useTranslation();

    const mockUser: User = {
        username: "小A",
        id: 1,
        avatar: "/logo.webp",
        createTime: 1700000000000
    };

    return (
        <div className="navbar bg-base-100 shadow-sm">
            <div className="navbar-start">
                <div className="avatar ml-4 mr-2">
                    <div className="w-12 rounded-lg"><img src="/logo.webp" alt="Logo"/></div>
                </div>
                <a className="btn btn-ghost text-xl">{t('components.navbar.name')}</a>
            </div>
            <div className="navbar-end gap-1 mr-2">
                <ThemeDropdown/>
                <LanguageDropdown i18n={i18n}/>
                <UserDropdown user={mockUser}/>
            </div>
        </div>
    );
}