import React, {useState} from "react";
import {useNavigate, Link} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {UserPlusIcon, UserIcon, KeyIcon} from "@heroicons/react/24/outline";
import {userService} from "../services/userService.ts";
import {useTurnstile} from "../hooks/useTurnstile.ts";

export default function RegisterPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const {turnstileRef, cfToken} = useTurnstile('0x4AAAAAACnWdLj7v2MmY7cI');

    const handleRegister = async (e: React.SyntheticEvent<HTMLFormElement, SubmitEvent>) => {
        e.preventDefault();
        if (!cfToken) return alert(t('auth.register.cf_error'));

        const hashedPassword = await userService.hashPassword(password);
        const res = await userService.register({username, password: hashedPassword, cfToken});

        if (res.code === 200) {
            localStorage.setItem("jwt_token", res.data);
            navigate("/website");
        } else {
            alert(res.message);
        }
    };
    return (
        <div className="min-h-[80vh] flex items-center justify-center">
            <div className="card w-full max-w-md bg-base-100 border border-base-300 rounded-xl shadow-none p-10">
                <div className="flex flex-col items-center mb-10 text-center">
                    <div className="p-4 bg-base-200 rounded-2xl mb-4">
                        <UserPlusIcon className="w-8 h-8"/>
                    </div>
                    <h1 className="text-2xl font-bold tracking-tight">{t('auth.register.title')}</h1>
                    <p className="text-sm opacity-50 mt-2">{t('auth.register.subtitle')}</p>
                </div>

                <form onSubmit={handleRegister} className="space-y-6">
                    <fieldset className="fieldset p-0">
                        <legend
                            className="fieldset-legend font-bold text-xs uppercase opacity-60">{t('auth.register.username_label')}</legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <UserIcon className="w-5 h-5 opacity-40"/>
                            <input type="text" value={username} onChange={e => setUsername(e.target.value)}
                                   className="grow" placeholder={t('auth.register.username_placeholder')} required/>
                        </label>
                    </fieldset>

                    <fieldset className="fieldset p-0">
                        <legend
                            className="fieldset-legend font-bold text-xs uppercase opacity-60">{t('auth.register.password_label')}</legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <KeyIcon className="w-5 h-5 opacity-40"/>
                            <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                                   className="grow" placeholder={t('auth.register.password_placeholder')} required/>
                        </label>
                    </fieldset>

                    <div ref={turnstileRef} className="flex min-h-16.25"></div>

                    <button type="submit"
                            className="btn bg-base-content text-base-100 btn-block h-12 rounded-lg font-bold hover:bg-black transition-all">
                        {t('auth.register.submit')}
                    </button>
                </form>

                <div className="mt-8 text-center text-sm opacity-60">
                    {t('auth.register.has_account')} <Link to="/login"
                                                           className="font-bold text-base-content hover:underline">{t('auth.register.login_link')}</Link>
                </div>
            </div>
        </div>
    );
}