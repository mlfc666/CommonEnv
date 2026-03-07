import {useState, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate, useLocation} from "react-router-dom";
import {setTheme, type ThemeInfo, themeOptions} from "../manager/ThemeManger.ts";
import {type LanguageInfo, languageOptions} from "../manager/i18n.ts";
import type {i18n} from "i18next";
import {LanguageIcon, SparklesIcon, UserCircleIcon} from "@heroicons/react/24/outline";
import {userService} from "../services/userService.ts";

function ThemeDropdown() {
    const {t} = useTranslation();
    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <SparklesIcon className="w-6 h-6"/><span
                className="hidden sm:inline">{t("components.navbar.Theme")}</span>
            </button>
            <ul tabIndex={0}
                className="dropdown-content menu bg-base-200 rounded-box w-52 p-2 shadow-2xl border border-base-300 mt-2">
                {themeOptions.map((theme: ThemeInfo) => (
                    <li key={theme.value}>
                        <button onClick={() => setTheme(theme.value)}
                                className="btn btn-sm btn-ghost justify-start">{theme.label}</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

function LanguageDropdown({i18n}: { i18n: i18n }) {
    const {t} = useTranslation();
    return (
        <div className="dropdown dropdown-end z-100">
            <button tabIndex={0} role="button" className="btn btn-ghost">
                <LanguageIcon className="w-6 h-6"/><span className="hidden sm:inline">{t("language")}</span>
            </button>
            <ul tabIndex={0}
                className="dropdown-content menu bg-base-200 rounded-box w-40 p-2 shadow-2xl border border-base-300 mt-2">
                {languageOptions.map((lang: LanguageInfo) => (
                    <li key={lang.language}>
                        <button onClick={() => void i18n.changeLanguage(lang.language)}
                                className="btn btn-sm btn-ghost justify-start">{lang.label}</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export function Navbar() {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const location = useLocation();
    const [avatar, setAvatar] = useState<string | null>(null);

    // 监听路由变化以同步当前用户的头像状态
    useEffect(() => {
        let isMounted = true;
        const syncAvatar = async () => {
            const token = localStorage.getItem("jwt_token");
            if (!token) return;
            try {
                const res = await userService.getUserInfo();
                if (isMounted && res.code === 200) setAvatar(res.data.avatar);
            } catch {
                if (isMounted) setAvatar(null);
            }
        };
        void syncAvatar();
        return () => {
            isMounted = false;
        };
    }, [location.pathname]);

    return (
        <div className="navbar bg-base-100 shadow-sm text-base-content">
            <div className="navbar-start">
                <div className="avatar ml-4 mr-2">
                    <div className="w-12 rounded-lg"><img src="./logo.webp" alt="Logo"/></div>
                </div>
                <a className="btn btn-ghost text-xl">{t('components.navbar.name')}</a>
            </div>
            <div className="navbar-end gap-1 mr-2">
                <ThemeDropdown/>
                <LanguageDropdown i18n={i18n}/>
                <button onClick={() => navigate('/person/info')}
                        className="btn btn-ghost btn-circle ml-2 overflow-hidden">
                    {avatar ? (
                        <div className="avatar">
                            <div className="w-8 rounded-full border border-base-300"><img src={avatar} alt="Avatar"/>
                            </div>
                        </div>
                    ) : (
                        <UserCircleIcon className="w-7 h-7"/>
                    )}
                </button>
            </div>
        </div>
    );
}