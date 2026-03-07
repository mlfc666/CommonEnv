import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { IdentificationIcon, CalendarDaysIcon, UserIcon, FingerPrintIcon } from "@heroicons/react/24/outline";
import type { UserInfoDTO } from "../../types/UserInfoDTO.ts";

// 左侧头像预览组件负责展示用户形象与UID
const AvatarCard: React.FC<{ avatar: string; username: string; id: number; onUpload: (file: File) => void }> = ({ avatar, username, id, onUpload }) => {
    const { t } = useTranslation();

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) onUpload(e.target.files[0]);
    };

    return (
        <div className="card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body items-center text-center p-10">
                <div className="avatar mb-6">
                    <div className="w-36 rounded-full ring-2 ring-base-200 ring-offset-4 ring-offset-base-100">
                        <img src={avatar} alt="Avatar" />
                    </div>
                </div>
                <h2 className="text-2xl font-bold tracking-tight">{username}</h2>
                <p className="opacity-40 text-sm font-mono mt-1 uppercase tracking-widest">
                    {t('person.info.avatar.uid_prefix')}: {id}
                </p>
                <div className="w-full mt-10">
                    <input type="file" id="avatar-upload" className="hidden" onChange={handleFileChange} accept="image/*" />
                    <label htmlFor="avatar-upload" className="btn btn-outline border-base-300 btn-block h-12 font-bold hover:bg-base-200 cursor-pointer">
                        {t('person.info.avatar.change')}
                    </label>
                </div>
            </div>
        </div>
    );
};

// 右侧信息表单组件展示账户的核心详细数据
const InfoForm: React.FC<{ user: UserInfoDTO }> = ({ user }) => {
    const { t } = useTranslation();

    return (
        <div className="lg:col-span-2 card bg-base-100 border border-base-300 rounded-lg shadow-none">
            <div className="card-body gap-8 p-10">
                <div className="flex items-center gap-4 border-b border-base-200 pb-6">
                    <div className="p-3 bg-base-200 rounded-lg">
                        <IdentificationIcon className="w-6 h-6" />
                    </div>
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
                            <input type="text" value={user.username} className="grow text-base" readOnly />
                        </label>
                    </fieldset>
                    <fieldset className="fieldset">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.info.form.userid')}
                        </legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg bg-base-200">
                            <FingerPrintIcon className="w-5 h-5 opacity-40" />
                            <input type="text" value={user.id} className="grow text-base" readOnly />
                        </label>
                    </fieldset>
                    <fieldset className="fieldset col-span-full">
                        <legend className="fieldset-legend font-bold text-xs uppercase tracking-wider opacity-60">
                            {t('person.info.form.created_at')}
                        </legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <CalendarDaysIcon className="w-5 h-5 opacity-40" />
                            <input type="text" value={new Date(user.createTime).toLocaleDateString()} className="grow text-base" readOnly />
                        </label>
                    </fieldset>
                </div>
                <div className="card-actions justify-end mt-4">
                    <button className="btn bg-base-content text-base-100 px-10 h-12 rounded-lg text-base font-bold shadow-lg shadow-base-content/10 hover:bg-black transition-all">
                        {t('person.info.form.save')}
                    </button>
                </div>
            </div>
        </div>
    );
};

// 个人资料页面主容器组件
export default function UserInfoPage() {
    const [user] = useState<UserInfoDTO>({
        username: "小A",
        createTime: 1700000000000,
        id: 1,
        avatar: "/logo.webp"
    });

    const onAvatarUpload = (file: File) => {
        console.log("正在执行头像上传逻辑", file.name);
    };

    return (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <AvatarCard avatar={user.avatar} username={user.username} id={user.id} onUpload={onAvatarUpload} />
            <InfoForm user={user} />
        </div>
    );
}