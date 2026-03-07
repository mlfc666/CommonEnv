import React from "react";
import {useTranslation} from "react-i18next";
import {PencilSquareIcon, XMarkIcon, TagIcon, TrashIcon, CheckIcon} from "@heroicons/react/24/outline";
import {memoService} from "../../../services/memoService.ts";
import type {Memo} from "../../../types/Memo.ts";

interface MemoDrawerProps {
    memo: Memo | null;
    setMemo: React.Dispatch<React.SetStateAction<Memo | null>>;
    onSuccess: () => void;
}

// 备忘录侧边栏组件负责处理数据的创建、更新与删除操作逻辑
export const MemoDrawer: React.FC<MemoDrawerProps> = ({memo, setMemo, onSuccess}) => {
    const {t} = useTranslation();

    // 切换复选框状态以隐藏侧边栏交互界面
    const closeDrawer = () => {
        const toggle = document.getElementById("memo-drawer") as HTMLInputElement;
        if (toggle) toggle.checked = false;
    };

    // 执行保存任务并根据返回结果弹出国际化提示
    const handleSave = async () => {
        if (!memo) return;
        try {
            const res = memo.id
                ? await memoService.updateMemo(memo)
                : await memoService.createMemo(memo);

            if (res.code === 200) {
                alert(t('memo.drawer.save_success'));
                onSuccess();
                closeDrawer();
            } else {
                alert(res.message);
            }
        } catch (error) {
            console.error(t('common.error_network') + error);
        }
    };

    // 执行删除任务前弹出确认框并在成功后清理本地视图
    const handleDelete = async () => {
        if (!memo || !memo.id) return;
        if (!window.confirm(t('person.security.danger.confirm'))) return;

        try {
            const res = await memoService.deleteMemo(memo.id);
            if (res.code === 200) {
                alert(t('memo.drawer.delete_success'));
                onSuccess();
                closeDrawer();
            } else {
                alert(res.message);
            }
        } catch (error) {
            console.error(t('common.error_network') + error);
        }
    };

    return (
        <div className="drawer-side z-100 text-base-content">
            <label htmlFor="memo-drawer" className="drawer-overlay"></label>
            <div
                className="p-10 w-full max-w-xl min-h-full bg-base-100 border-l border-base-300 shadow-2xl flex flex-col">
                <div className="flex items-center justify-between mb-10 pb-6 border-b border-base-200">
                    <div className="flex items-center gap-3">
                        <div className="p-3 bg-base-200 rounded-lg">
                            <PencilSquareIcon className="w-6 h-6"/>
                        </div>
                        <div>
                            <h2 className="text-xl font-bold">{t('memo.drawer.title')}</h2>
                            <p className="text-xs opacity-50 uppercase mt-0.5 font-bold tracking-tighter">
                                {t('memo.drawer.subtitle')}
                            </p>
                        </div>
                    </div>
                    <label htmlFor="memo-drawer" className="btn btn-ghost btn-circle">
                        <XMarkIcon className="w-6 h-6"/>
                    </label>
                </div>
                <div className="space-y-10 grow">
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2">
                            {t('memo.drawer.label_title')}
                        </legend>
                        <input
                            type="text"
                            value={memo?.title || ""}
                            onChange={(e) => setMemo(prev => prev ? {...prev, title: e.target.value} : null)}
                            placeholder={t('memo.drawer.placeholder_title')}
                            className="input input-bordered w-full h-14 rounded-lg font-bold text-lg"
                        />
                    </fieldset>
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2">
                            {t('memo.drawer.label_content')}
                        </legend>
                        <textarea
                            value={memo?.content || ""}
                            onChange={(e) => setMemo(prev => prev ? {...prev, content: e.target.value} : null)}
                            className="textarea textarea-bordered w-full h-87.5 leading-relaxed rounded-lg text-base"
                            placeholder={t('memo.drawer.placeholder_content')}
                        ></textarea>
                    </fieldset>
                    <fieldset className="fieldset p-0">
                        <legend
                            className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2 flex items-center gap-1">
                            <TagIcon className="w-3 h-3"/> {t('memo.drawer.label_tags')}
                        </legend>
                        <input
                            type="text"
                            value={memo?.tags?.join(", ") || ""}
                            onChange={(e) => {
                                const val = e.target.value;
                                setMemo(prev => prev ? {
                                    ...prev,
                                    tags: val.split(',').map(t => t.trim()).filter(t => t !== "")
                                } : null)
                            }}
                            className="input input-bordered w-full h-12 rounded-lg text-sm"
                            placeholder={t('memo.drawer.placeholder_tags')}
                        />
                    </fieldset>
                </div>
                <div className="flex gap-4 pt-10 mt-6 border-t border-base-200">
                    <button
                        onClick={handleDelete}
                        className={`btn btn-ghost flex-1 h-12 text-error gap-2 hover:bg-error/10 ${!memo?.id ? 'btn-disabled opacity-30' : ''}`}
                    >
                        <TrashIcon className="w-5 h-5"/> {t('memo.drawer.btn_delete')}
                    </button>
                    <button
                        onClick={handleSave}
                        className="btn bg-base-content text-base-100 flex-2 h-12 rounded-lg shadow-xl shadow-base-content/10 font-bold hover:bg-black transition-all border-none"
                    >
                        <CheckIcon className="w-5 h-5"/> {t('memo.drawer.btn_save')}
                    </button>
                </div>
            </div>
        </div>
    );
};