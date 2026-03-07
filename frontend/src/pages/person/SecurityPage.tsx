import React, {useState, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {TrashIcon, ShieldCheckIcon, KeyIcon} from "@heroicons/react/24/outline";
import {userService} from "../../services/userService.ts";

// 修改密码组件负责用户输入收集与本地一致性校验逻辑
const PasswordForm: React.FC = () => {
    const {t} = useTranslation();
    const [oldPassword, setOldPassword] = useState<string>("");
    const [newPassword, setNewPassword] = useState<string>("");
    const [confirmPassword, setConfirmPassword] = useState<string>("");

    // 处理密码提交并仅对核心凭据执行哈希与上传
    const handleSubmit = async () => {
        if (newPassword !== confirmPassword) {
            alert(t('person.security.password.mismatch'));
            return;
        }

        try {
            const hashedOld = await userService.hashPassword(oldPassword);
            const hashedNew = await userService.hashPassword(newPassword);

            const res = await userService.updatePassword({
                oldPassword: hashedOld,
                newPassword: hashedNew
            });

            if (res.code === 200) {
                alert(t('person.security.password.success'));
                setOldPassword("");
                setNewPassword("");
                setConfirmPassword("");
            } else {
                alert(res.message);
            }
        } catch (error) {
            console.error(t('common.error_network') + error);
        }
    };

    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none text-base-content">
            <div className="card-body gap-8 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg"><KeyIcon className="w-6 h-6"/></div>
                    <div>
                        <h2 className="text-xl font-bold">{t('person.security.password.title')}</h2>
                        <p className="text-sm opacity-50">{t('person.security.password.subtitle')}</p>
                    </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <fieldset className="fieldset col-span-full md:col-span-1">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.security.password.old_label')}
                        </legend>
                        <input
                            type="password"
                            value={oldPassword}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setOldPassword(e.target.value)}
                            placeholder="••••••••"
                            className="input input-bordered w-full h-12 rounded-lg"
                        />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.security.password.new_label')}
                        </legend>
                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setNewPassword(e.target.value)}
                            placeholder="••••••••"
                            className="input input-bordered w-full h-12 rounded-lg"
                        />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.security.password.confirm_label')}
                        </legend>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setConfirmPassword(e.target.value)}
                            placeholder="••••••••"
                            className="input input-bordered w-full h-12 rounded-lg"
                        />
                    </fieldset>
                </div>
                <div className="card-actions justify-start mt-2">
                    <button
                        onClick={handleSubmit}
                        className="btn bg-base-content text-base-100 px-10 h-12 rounded-lg text-base font-bold hover:bg-black transition-all border-none"
                    >
                        {t('person.security.password.submit')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 危险操作区块处理账户的注销流程与状态清理逻辑
const DangerZone: React.FC = () => {
    const {t} = useTranslation();
    const navigate = useNavigate();

    // 弹出二次确认框并在用户同意后执行销户操作
    const handleDeleteAccount = async () => {
        if (!window.confirm(t('person.security.danger.confirm'))) return;
        try {
            const res = await userService.deleteAccount();
            if (res.code === 200) {
                localStorage.removeItem("jwt_token");
                navigate("/login");
            } else {
                alert(res.message);
            }
        } catch (error) {
            console.error(t('common.error_network') + error);
        }
    };

    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none text-base-content">
            <div className="card-body gap-6 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg text-error"><TrashIcon className="w-6 h-6"/></div>
                    <div>
                        <h2 className="text-xl font-bold text-error">{t('person.security.danger.title')}</h2>
                        <p className="text-sm opacity-50">{t('person.security.danger.subtitle')}</p>
                    </div>
                </div>
                <p className="text-sm opacity-60 leading-relaxed">{t('person.security.danger.description')}</p>
                <div className="card-actions justify-start">
                    <button
                        onClick={handleDeleteAccount}
                        className="btn btn-outline border-error text-error h-12 px-8 rounded-lg hover:bg-error hover:text-white transition-all"
                    >
                        {t('person.security.danger.submit')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 安全建议组件展示账号保护的最佳实践方案
const SecurityAside: React.FC = () => {
    const {t} = useTranslation();
    return (
        <aside className="lg:col-span-1">
            <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none h-full text-base-content">
                <div className="card-body gap-6 p-10">
                    <div className="flex items-center gap-3">
                        <ShieldCheckIcon className="w-6 h-6 text-success"/>
                        <h3 className="text-lg font-bold">{t('person.security.aside.title')}</h3>
                    </div>
                    <div className="text-sm opacity-70 space-y-6">
                        <p>{t('person.security.aside.suggestion1')}</p>
                        <p>{t('person.security.aside.suggestion2')}</p>
                    </div>
                </div>
            </div>
        </aside>
    );
};

// 安全设置主页面容器负责执行初始身份校验与数据流分发
export default function SecurityPage() {
    const navigate = useNavigate();

    // 组件挂载时触发身份令牌有效性核验流程
    useEffect(() => {
        let isMounted = true;
        const verifyAuth = async () => {
            try {
                const res = await userService.getUserInfo();
                if (isMounted && res.code !== 200) {
                    navigate("/login");
                }
            } catch {
                if (isMounted) navigate("/login");
            }
        };
        void verifyAuth();
        return () => {
            isMounted = false;
        };
    }, [navigate]);

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2 space-y-8">
                <PasswordForm/>
                <DangerZone/>
            </div>
            <SecurityAside/>
        </div>
    );
}