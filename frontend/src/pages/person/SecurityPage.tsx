import React from "react";
import { TrashIcon, ShieldCheckIcon, KeyIcon } from "@heroicons/react/24/outline";

// 修改密码组件
const PasswordForm: React.FC = () => {
    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body gap-8 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg"><KeyIcon className="w-6 h-6" /></div>
                    <div>
                        <h2 className="text-xl font-bold">修改密码</h2>
                        <p className="text-sm opacity-50">为了您的账号安全，建议定期更新密码</p>
                    </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <fieldset className="fieldset col-span-full md:col-span-1">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">原密码</legend>
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">新密码</legend>
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">确认新密码</legend>
                        <input type="password" placeholder="••••••••" className="input input-bordered w-full h-12 rounded-lg" />
                    </fieldset>
                </div>
                <div className="card-actions justify-start mt-2">
                    <button className="btn bg-base-content text-base-100 px-10 h-12 rounded-lg text-base font-bold hover:bg-black transition-all">
                        保存密码
                    </button>
                </div>
            </div>
        </div>
    );
};

// 危险操作区块
const DangerZone: React.FC = () => {
    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body gap-6 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg text-error"><TrashIcon className="w-6 h-6" /></div>
                    <div>
                        <h2 className="text-xl font-bold text-error">注销账号</h2>
                        <p className="text-sm opacity-50">该操作不可撤销，请谨慎处理</p>
                    </div>
                </div>
                <p className="text-sm opacity-60 leading-relaxed">注销后，所有与此账号关联的数据将被永久从服务器移除，无法找回。</p>
                <div className="card-actions justify-start">
                    <button className="btn btn-outline border-error text-error h-12 px-8 rounded-lg hover:bg-error hover:text-white transition-all">
                        永久注销账号
                    </button>
                </div>
            </div>
        </div>
    );
};

// 右侧侧边栏组件
const SecurityAside: React.FC = () => {
    return (
        <aside className="lg:col-span-1">
            <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none h-full">
                <div className="card-body gap-6 p-10">
                    <div className="flex items-center gap-3">
                        <ShieldCheckIcon className="w-6 h-6 text-success" />
                        <h3 className="text-lg font-bold">安全建议</h3>
                    </div>
                    <div className="text-sm opacity-70 space-y-6">
                        <p>为了保持高强度防御，建议您至少每 90 天更换一次主密码。</p>
                        <p>在共享设备上使用服务时，请记得在操作完成后手动执行退出登录。</p>
                    </div>
                </div>
            </div>
        </aside>
    );
};

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