import React from "react";
import { useTranslation } from "react-i18next";
import { TrashIcon, ShieldCheckIcon, KeyIcon } from "@heroicons/react/24/outline";

// 修改密码组件负责处理用户凭据更新
const PasswordForm: React.FC = () => {
    const { t } = useTranslation();
    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body gap-8 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg"><KeyIcon className="w-6 h-6" /></div>
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
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.security.password.new_label')}
                        </legend>
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.security.password.confirm_label')}
                        </legend>
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                </div>
                <div className="card-actions justify-start mt-2">
                    <button className="btn bg-base-content text-base-100 px-10 h-12 rounded-lg text-base font-bold hover:bg-black transition-all">
                        {t('person.security.password.submit')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 危险操作区块用于处理账户注销逻辑
const DangerZone: React.FC = () => {
    const { t } = useTranslation();
    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body gap-6 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg text-error"><TrashIcon className="w-6 h-6" /></div>
                    <div>
                        <h2 className="text-xl font-bold text-error">{t('person.security.danger.title')}</h2>
                        <p className="text-sm opacity-50">{t('person.security.danger.subtitle')}</p>
                    </div>
                </div>
                <p className="text-sm opacity-60 leading-relaxed">{t('person.security.danger.description')}</p>
                <div className="card-actions justify-start">
                    <button className="btn btn-outline border-error text-error h-12 px-8 rounded-lg hover:bg-error hover:text-white transition-all">
                        {t('person.security.danger.submit')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 辅助侧边栏展示系统安全建议
const SecurityAside: React.FC = () => {
    const { t } = useTranslation();
    return (
        <aside className="lg:col-span-1">
            <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none h-full">
                <div className="card-body gap-6 p-10">
                    <div className="flex items-center gap-3">
                        <ShieldCheckIcon className="w-6 h-6 text-success" />
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

// 安全设置主页面容器
export default function SecurityPage() {
    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2 space-y-8">
                <PasswordForm />
                <DangerZone />
            </div>
            <SecurityAside />
        </div>
    );
}