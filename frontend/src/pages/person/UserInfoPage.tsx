import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { IdentificationIcon, CalendarDaysIcon, UserIcon, FingerPrintIcon } from "@heroicons/react/24/outline";
import { userService } from "../../services/userService.ts";
import type { UserInfoDTO } from "../../types/UserInfoDTO.ts";

// 左侧头像预览组件负责展示用户标识并触发文件选择逻辑
const AvatarCard: React.FC<{ avatar: string; username: string; id: number; onUpload: (file: File) => void }> = ({ avatar, username, id, onUpload }) => {
    const { t } = useTranslation();

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) onUpload(e.target.files[0]);
    };

    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none text-base-content">
            <div className="card-body items-center text-center p-10">
                <div className="avatar mb-6">
                    <div className="w-36 rounded-full ring-2 ring-base-200 ring-offset-4 ring-offset-base-100">
                        <img src={avatar || "/logo.webp"} alt="Avatar" />
                    </div>
                </div>
                <h2 className="text-2xl font-bold tracking-tight">{username}</h2>
                <p className="opacity-40 text-sm font-mono mt-1 uppercase tracking-widest">
                    {t('person.info.avatar.uid_prefix')}: {id}
                </p>
                <div className="w-full mt-10">
                    <input type="file" id="avatar-upload" className="hidden" onChange={handleFileChange} accept="image/*" />
                    <label htmlFor="avatar-upload" className="btn btn-outline border-base-300 btn-block h-12 font-bold hover:bg-base-200 cursor-pointer text-base-content">
                        {t('person.info.avatar.change')}
                    </label>
                </div>
            </div>
        </div>
    );
};

// 右侧表单组件负责维护本地编辑状态并执行资料更新提交
const InfoForm: React.FC<{ user: UserInfoDTO; onUpdate: (name: string) => void }> = ({ user, onUpdate }) => {
    const { t } = useTranslation();

    // 初始化本地状态：当父组件传入不同的 key 时该状态会自动重置
    const [editName, setEditName] = useState<string>(user.username);

    return (
        <div className="lg:col-span-2 card bg-base-100 border border-base-300 rounded-lg shadow-none text-base-content">
            <div className="card-body gap-8 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg"><IdentificationIcon className="w-6 h-6" /></div>
                    <div>
                        <h2 className="text-xl font-bold">{t('person.info.form.title')}</h2>
                        <p className="text-sm opacity-50">{t('person.info.form.subtitle')}</p>
                    </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.info.form.username')}
                        </legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <UserIcon className="w-5 h-5 opacity-40" />
                            <input
                                type="text"
                                value={editName}
                                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEditName(e.target.value)}
                                className="grow text-base"
                            />
                        </label>
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.info.form.userid')}
                        </legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg bg-base-200 cursor-not-allowed">
                            <FingerPrintIcon className="w-5 h-5 opacity-40" />
                            <input type="text" value={user.id} className="grow text-base" readOnly />
                        </label>
                    </fieldset>
                    <fieldset className="fieldset col-span-full">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.info.form.created_at')}
                        </legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg bg-base-200 cursor-not-allowed">
                            <CalendarDaysIcon className="w-5 h-5 opacity-40" />
                            <input type="text" value={new Date(user.createTime).toLocaleDateString()} className="grow text-base" readOnly />
                        </label>
                    </fieldset>
                </div>
                <div className="card-actions justify-end mt-4">
                    <button
                        onClick={() => onUpdate(editName)}
                        className="btn bg-base-content text-base-100 px-10 h-12 rounded-lg text-base font-bold hover:bg-black transition-all border-none"
                    >
                        {t('person.info.form.save')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 页面容器组件负责执行初始身份校验与数据流分发
export default function UserInfoPage() {
    const navigate = useNavigate();
    const [user, setUser] = useState<UserInfoDTO | null>(null);

    // 在 Effect 内部直接定义并执行拉取逻辑以规避级联渲染警告
    useEffect(() => {
        let isMounted = true;
        const loadInitialData = async () => {
            try {
                const res = await userService.getUserInfo();
                if (isMounted) {
                    if (res.code === 200) {
                        setUser(res.data);
                    } else {
                        navigate("/login");
                    }
                }
            } catch (error) {
                console.error("身份校验任务失败" +  error);
                if (isMounted) navigate("/login");
            }
        };
        void loadInitialData();
        return () => { isMounted = false; };
    }, [navigate]);

    const onAvatarUpload = async (file: File) => {
        try {
            const res = await userService.uploadAvatar(file);
            if (res.code === 200 && user) {
                setUser({ ...user, avatar: res.data });
            }
        } catch (error) {
            console.error("头像上传任务失败" +  error);
        }
    };

    const onUpdateUsername = async (name: string) => {
        try {
            const res = await userService.updateUsername(name);
            if (res.code === 200 && user) {
                setUser({ ...user, username: name });
            } else {
                alert(res.message);
            }
        } catch (error) {
            console.error("更新资料任务失败" +  error);
        }
    };

    if (!user) {
        return (
            <div className="flex h-64 items-center justify-center text-base-content">
                <span className="loading loading-spinner loading-lg"></span>
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <AvatarCard
                avatar={user.avatar}
                username={user.username}
                id={user.id}
                onUpload={onAvatarUpload}
            />
            <InfoForm
                key={user.username} // 关键修改：利用 key 强制组件重置状态避免使用副作用同步
                user={user}
                onUpdate={onUpdateUsername}
            />
        </div>
    );
}