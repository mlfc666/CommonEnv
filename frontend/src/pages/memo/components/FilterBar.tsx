import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { MagnifyingGlassIcon, ChevronDownIcon } from "@heroicons/react/24/outline";
import { memoService } from "../../../services/memoService.ts";

// 筛选栏组件负责协调搜索词、多标签互斥筛选及时间范围的过滤逻辑
export const FilterBar: React.FC = () => {
    const { t } = useTranslation();

    // 存储从服务器拉取的全量标签池
    const [availableTags, setAvailableTags] = useState<string[]>([]);
    // 存储用户选中的具体标签数组
    const [selectedTags, setSelectedTags] = useState<string[]>([]);

    // 组件挂载时执行一次全量标签获取
    useEffect(() => {
        const fetchTags = async () => {
            try {
                const res = await memoService.getAllTags();
                if (res.code === 200) {
                    setAvailableTags(res.data);
                }
            } catch (error) {
                console.error("标签拉取失败" + error);
            }
        };
        void fetchTags();
    }, []);

    // 处理具体标签的选择切换逻辑
    const toggleTag = (tag: string) => {
        setSelectedTags(prev =>
            prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]
        );
    };

    // 重置逻辑将清空所有已选标签回归全部状态
    const selectAll = () => {
        setSelectedTags([]);
    };

    return (
        <div className="flex flex-wrap gap-4 bg-base-100 border border-base-300 p-6 rounded-xl items-center shadow-sm">
            {/* 关键词搜索输入框 */}
            <label className="input input-bordered flex items-center gap-2 grow h-12 rounded-lg">
                <MagnifyingGlassIcon className="w-5 h-5 opacity-40" />
                <input type="text" placeholder={t('memo.filter.search_placeholder')} className="grow text-sm" />
            </label>

            {/* 具有互斥逻辑的多选标签下拉菜单 */}
            <div className="dropdown">
                <div tabIndex={0} role="button" className="btn btn-bordered h-12 rounded-lg bg-base-100 border-base-300 font-normal min-w-40 justify-between">
                    <span className="text-sm">
                        {selectedTags.length > 0 ? `${t('sidebar.memo')} (${selectedTags.length})` : t('memo.filter.all_tags')}
                    </span>
                    <ChevronDownIcon className="w-4 h-4 opacity-50" />
                </div>
                <ul tabIndex={0} className="dropdown-content z-10 menu p-2 shadow-2xl bg-base-100 border border-base-300 rounded-lg w-52 mt-2 max-h-60 overflow-y-auto">
                    {/* 全部标签选项：选中则清空其他，其他有选中则自身取消 */}
                    <li>
                        <label className="label cursor-pointer justify-start gap-3 py-2">
                            <input
                                type="checkbox"
                                className="checkbox checkbox-sm rounded"
                                checked={selectedTags.length === 0}
                                onChange={selectAll}
                            />
                            <span className={`label-text ${selectedTags.length === 0 ? 'font-bold' : ''}`}>
                                {t('memo.filter.all_tags')}
                            </span>
                        </label>
                    </li>
                    <div className="divider my-0 opacity-20"></div>
                    {/* 动态渲染的具体标签列表 */}
                    {availableTags.map(tag => (
                        <li key={tag}>
                            <label className="label cursor-pointer justify-start gap-3 py-2">
                                <input
                                    type="checkbox"
                                    className="checkbox checkbox-sm rounded"
                                    checked={selectedTags.includes(tag)}
                                    onChange={() => toggleTag(tag)}
                                />
                                <span className="label-text">{tag}</span>
                            </label>
                        </li>
                    ))}
                </ul>
            </div>

            {/* 时间跨度筛选选择器 */}
            <select className="select select-bordered h-12 rounded-lg min-w-35">
                <option>{t('memo.filter.all_time')}</option>
                <option value="3">{t('memo.filter.days_3')}</option>
                <option value="7">{t('memo.filter.days_7')}</option>
            </select>

            {/* 触发搜索行为的提交按钮 */}
            <button className="btn bg-base-content text-base-100 h-12 px-10 rounded-lg hover:bg-black transition-all border-none">
                {t('memo.filter.submit')}
            </button>
        </div>
    );
};