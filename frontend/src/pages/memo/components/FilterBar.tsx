import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {MagnifyingGlassIcon, ChevronDownIcon} from "@heroicons/react/24/outline";
import {memoService} from "../../../services/memoService.ts";

interface FilterBarProps {
    onSearch: (keyword: string, tags: string[]) => void;
}

// 筛选工具栏组件负责收集用户的检索指令并反馈至上层容器
export const FilterBar: React.FC<FilterBarProps> = ({onSearch}) => {
    const {t} = useTranslation();
    const [availableTags, setAvailableTags] = useState<string[]>([]);
    const [selectedTags, setSelectedTags] = useState<string[]>([]);
    const [keyword, setKeyword] = useState<string>("");

    // 组件加载时执行标签元数据的拉取任务
    useEffect(() => {
        let isMounted = true;
        const fetchTags = async () => {
            try {
                const res = await memoService.getAllTags();
                if (isMounted && res.code === 200) setAvailableTags(res.data);
            } catch (error) {
                console.error(t('common.error_network') + error);
            }
        };
        void fetchTags();
        return () => {
            isMounted = false;
        };
    }, [t]);

    return (
        <div
            className="flex flex-wrap gap-4 bg-base-100 border border-base-300 p-6 rounded-xl items-center shadow-sm text-base-content">
            <label className="input input-bordered flex items-center gap-2 grow h-12 rounded-lg">
                <MagnifyingGlassIcon className="w-5 h-5 opacity-40"/>
                <input
                    type="text"
                    value={keyword}
                    onChange={e => setKeyword(e.target.value)}
                    placeholder={t('memo.filter.search_placeholder')}
                    className="grow text-sm border-none focus:ring-0"
                />
            </label>

            <div className="dropdown">
                <div tabIndex={0} role="button"
                     className="btn btn-bordered h-12 rounded-lg bg-base-100 border-base-300 font-normal min-w-40 justify-between">
                    <span className="text-sm">
                        {selectedTags.length > 0 ? `${t('sidebar.memo')} (${selectedTags.length})` : t('memo.filter.all_tags')}
                    </span>
                    <ChevronDownIcon className="w-4 h-4 opacity-50"/>
                </div>
                <ul tabIndex={0}
                    className="dropdown-content z-10 menu p-2 shadow-2xl bg-base-100 border border-base-300 rounded-lg w-52 mt-2 max-h-60 overflow-y-auto">
                    <li>
                        <label className="label cursor-pointer justify-start gap-3 py-2">
                            <input type="checkbox" className="checkbox checkbox-sm rounded"
                                   checked={selectedTags.length === 0} onChange={() => setSelectedTags([])}/>
                            <span
                                className={`label-text ${selectedTags.length === 0 ? 'font-bold' : ''}`}>{t('memo.filter.all_tags')}</span>
                        </label>
                    </li>
                    <div className="divider my-0 opacity-20"></div>
                    {availableTags.map(tag => (
                        <li key={tag}>
                            <label className="label cursor-pointer justify-start gap-3 py-2">
                                <input
                                    type="checkbox"
                                    className="checkbox checkbox-sm rounded"
                                    checked={selectedTags.includes(tag)}
                                    onChange={() => setSelectedTags(prev => prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag])}
                                />
                                <span className="label-text">{tag}</span>
                            </label>
                        </li>
                    ))}
                </ul>
            </div>

            <button
                onClick={() => onSearch(keyword, selectedTags)}
                className="btn bg-base-content text-base-100 h-12 px-10 rounded-lg hover:bg-black transition-all border-none"
            >
                {t('memo.filter.submit')}
            </button>
        </div>
    );
};