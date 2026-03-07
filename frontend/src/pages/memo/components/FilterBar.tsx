import React from "react";
import { useTranslation } from "react-i18next";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";

// 筛选栏组件负责处理列表的搜索与过滤条件的输入
export const FilterBar: React.FC = () => {
    const { t } = useTranslation();

    return (
        <div className="flex flex-wrap gap-4 bg-base-100 border border-base-300 p-6 rounded-xl items-center shadow-sm">
            <label className="input input-bordered flex items-center gap-2 grow h-12 rounded-lg">
                <MagnifyingGlassIcon className="w-5 h-5 opacity-40" />
                <input type="text" placeholder={t('memo.filter.search_placeholder')} className="grow text-sm" />
            </label>
            <select className="select select-bordered h-12 rounded-lg min-w-35">
                <option>{t('memo.filter.all_tags')}</option>
                <option>{t('memo.filter.tag_work')}</option>
                <option>{t('memo.filter.tag_life')}</option>
            </select>
            <select className="select select-bordered h-12 rounded-lg min-w-35">
                <option>{t('memo.filter.all_time')}</option>
                <option value="3">{t('memo.filter.days_3')}</option>
                <option value="7">{t('memo.filter.days_7')}</option>
            </select>
            <button className="btn bg-base-content text-base-100 h-12 px-10 rounded-lg hover:bg-black transition-all">
                {t('memo.filter.submit')}
            </button>
        </div>
    );
};