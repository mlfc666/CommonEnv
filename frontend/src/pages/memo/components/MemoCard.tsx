import React from "react";
import {useTranslation} from "react-i18next";
import {PencilSquareIcon} from "@heroicons/react/24/outline";
import type {Memo} from "../../../types/Memo.ts";

interface MemoCardProps {
    memo: Memo;
    onEdit: (memo: Memo) => void;
}

// 备忘录卡片组件负责展示列表项的摘要信息与交互入口
export const MemoCard: React.FC<MemoCardProps> = ({memo, onEdit}) => {
    const {t} = useTranslation();

    return (
        <div
            className="card bg-base-100 border border-base-300 rounded-xl shadow-none hover:border-base-content transition-all group relative text-base-content">
            <div className="card-body p-8 flex flex-col h-full">
                <div className="flex justify-between items-start mb-2">
                    <h2 className="text-xl font-bold truncate leading-tight pr-4">
                        {memo.title || t('memo.card.no_title')}
                    </h2>
                    <button
                        onClick={() => onEdit(memo)}
                        className="btn btn-ghost btn-sm btn-circle opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                        <PencilSquareIcon className="w-5 h-5"/>
                    </button>
                </div>
                <p className="text-sm opacity-60 line-clamp-4 leading-relaxed mb-6">
                    {memo.content || t('memo.card.no_content')}
                </p>
                <div className="flex items-center justify-between mt-auto pt-6 border-t border-base-200">
                    <div className="flex flex-wrap gap-2">
                        {memo.tags?.map(tag => (
                            <span key={tag}
                                  className="px-3 py-1 bg-base-content text-base-100 text-[10px] font-black uppercase rounded">
                                {tag}
                            </span>
                        ))}
                    </div>
                    <span className="text-sm font-mono font-bold opacity-80">
                        {new Date(memo.creatTime).toLocaleDateString()}
                    </span>
                </div>
            </div>
        </div>
    );
};