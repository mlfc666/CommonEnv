import React from "react";
import { PencilSquareIcon, XMarkIcon, TagIcon, TrashIcon, CheckIcon } from "@heroicons/react/24/outline";
import type {Memo} from "../../../types/Memo.ts";

interface MemoDrawerProps {
    memo: Memo | null;
    setMemo: React.Dispatch<React.SetStateAction<Memo | null>>;
}

export const MemoDrawer: React.FC<MemoDrawerProps> = ({ memo, setMemo }) => {
    return (
        <div className="drawer-side z-100">
            <label htmlFor="memo-drawer" className="drawer-overlay"></label>
            <div className="p-10 w-full max-w-xl min-h-full bg-base-100 border-l border-base-300 shadow-2xl flex flex-col">
                <div className="flex items-center justify-between mb-10 pb-6 border-b border-base-200">
                    <div className="flex items-center gap-3">
                        <div className="p-3 bg-base-200 rounded-lg"><PencilSquareIcon className="w-6 h-6" /></div>
                        <div><h2 className="text-xl font-bold">编辑备忘录</h2><p className="text-xs opacity-50 uppercase mt-0.5 font-bold tracking-tighter">Drafting Area</p></div>
                    </div>
                    <label htmlFor="memo-drawer" className="btn btn-ghost btn-circle"><XMarkIcon className="w-6 h-6" /></label>
                </div>
                <div className="space-y-10 grow">
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2">备忘录标题</legend>
                        <input type="text" value={memo?.title || ""} onChange={(e) => setMemo(prev => prev ? { ...prev, title: e.target.value } : null)} placeholder="输入标题..." className="input input-bordered w-full h-14 rounded-lg font-bold text-lg" />
                    </fieldset>
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2">详细内容</legend>
                        <textarea value={memo?.content || ""} onChange={(e) => setMemo(prev => prev ? { ...prev, content: e.target.value } : null)} className="textarea textarea-bordered w-full h-87.5 leading-relaxed rounded-lg text-base" placeholder="在这里开始记录..."></textarea>
                    </fieldset>
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-50 mb-2 flex items-center gap-1"><TagIcon className="w-3 h-3" /> 标签 (用英文逗号分隔)</legend>
                        <input type="text" value={memo?.tags?.join(", ") || ""} onChange={(e) => {
                            const val = e.target.value;
                            setMemo(prev => prev ? { ...prev, tags: val.split(',').map(t => t.trim()).filter(t => t !== "") } : null)
                        }} className="input input-bordered w-full h-12 rounded-lg text-sm" placeholder="例如: 工作, 紧急, 灵感" />
                    </fieldset>
                </div>
                <div className="flex gap-4 pt-10 mt-6 border-t border-base-200">
                    <button className="btn btn-ghost flex-1 h-12 text-error gap-2 hover:bg-error/10"><TrashIcon className="w-5 h-5" /> 删除</button>
                    <button className="btn bg-base-content text-base-100 flex-2 h-12 rounded-lg shadow-xl shadow-base-content/10 font-bold hover:bg-black transition-all"><CheckIcon className="w-5 h-5" /> 保存修改</button>
                </div>
            </div>
        </div>
    );
};