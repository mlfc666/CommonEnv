import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { setTheme, type ThemeInfo, themeOptions } from "../manager/ThemeManger.ts";
import { type LanguageInfo, languageOptions } from "../manager/i18n.ts";
import type { i18n } from "i18next";
import { LanguageIcon, SparklesIcon, UserCircleIcon } from "@heroicons/react/24/outline";

// 主题下拉
function ThemeDropdown() {
    const { t } = useTranslation();
    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <SparklesIcon className="w-6 h-6" />
                <span className="hidden sm:inline">{t("components.navbar.Theme")}</span>
            </button>
            <ul tabIndex={0} className="dropdown-content menu bg-base-200 rounded-box w-52 p-2 shadow-2xl border border-base-300 mt-2">
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
function LanguageDropdown({ i18n }: { i18n: i18n }) {
    const { t } = useTranslation();
    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <LanguageIcon className="w-6 h-6" />
                <span className="hidden sm:inline">{t("language")}</span>
            </button>
            <ul tabIndex={0} className="dropdown-content menu bg-base-200 rounded-box w-40 p-2 shadow-2xl border border-base-300 mt-2">
                {languageOptions.map((lang: LanguageInfo) => (
                    <li key={lang.language}>
                        <button onClick={() => void i18n.changeLanguage(lang.language)} className="btn btn-sm btn-ghost justify-start">
                            {lang.label}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

// 主组件
export function Navbar() {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();

    return (
        <div className="navbar bg-base-100 shadow-sm">
            <div className="navbar-start">
                <div className="avatar ml-4 mr-2">
                    <div className="w-12 rounded-lg"><img src="/logo.webp" alt="Logo" /></div>
                </div>
                <a className="btn btn-ghost text-xl">{t('components.navbar.name')}</a>
            </div>

            <div className="navbar-end gap-1 mr-2">
                <ThemeDropdown />
                <LanguageDropdown i18n={i18n} />

                {/* 个人中心按钮，点击跳转至 /person/info */}
                <button
                    onClick={() => navigate('/person/info')}
                    className="btn btn-ghost btn-circle ml-2"
                    title="个人中心"
                >
                    <UserCircleIcon className="w-7 h-7" />
                </button>
            </div>
        </div>
    );
}