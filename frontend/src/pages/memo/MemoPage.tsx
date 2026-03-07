import { useState, useEffect, useCallback } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { DocumentPlusIcon } from "@heroicons/react/24/outline";
import { memoService } from "../../services/memoService.ts";
import { userService } from "../../services/userService.ts";
import type { Memo } from "../../types/Memo.ts";
import type { MemoQueryDTO } from "../../types/MemoQueryDTO.ts";
import { MemoDrawer } from "./components/MemoDrawer.tsx";
import { FilterBar } from "./components/FilterBar.tsx";
import { MemoCard } from "./components/MemoCard.tsx";

// 备忘录容器组件负责执行身份校验、业务数据加载及分页逻辑计算
export default function MemoPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [memos, setMemos] = useState<Memo[]>([]);
    const [editingMemo, setEditingMemo] = useState<Memo | null>(null);
    const [refreshKey, setRefreshKey] = useState<number>(0);

    // 存储从后端获取的记录总数
    const [total, setTotal] = useState<number>(0);

    const [query, setQuery] = useState<MemoQueryDTO>({
        page: 1,
        size: 9,
        keyword: "",
        tag: ""
    });

    // 计算衍生分页数据用于 UI 渲染与交互判断
    const totalPages = Math.ceil(total / query.size);
    const startRecord = (query.page - 1) * query.size + 1;
    const endRecord = Math.min(query.page * query.size, total);

    // 监听刷新键变化同步获取最新的标签列表与记录总数
    useEffect(() => {
        let isMounted = true;
        const fetchInfo = async () => {
            try {
                const res = await memoService.getMemoInfo();
                if (isMounted && res.code === 200) {
                    setTotal(res.data.total);
                }
            } catch (error) {
                console.error(t('common.error_network') + error);
            }
        };
        void fetchInfo();
        return () => { isMounted = false; };
    }, [refreshKey, t]);

    // 执行身份核验与备忘录列表数据的异步拉取任务
    useEffect(() => {
        let isMounted = true;
        const loadPageData = async () => {
            try {
                const authRes = await userService.getUserInfo();
                if (authRes.code !== 200) {
                    if (isMounted) navigate("/login");
                    return;
                }

                const memoRes = await memoService.getMemos(query);
                if (isMounted && memoRes.code === 200) {
                    setMemos(memoRes.data);
                }
            } catch (error) {
                console.error(t('common.error_network') + error);
                if (isMounted) navigate("/login");
            }
        };
        void loadPageData();
        return () => { isMounted = false; };
    }, [query, refreshKey, navigate, t]);

    const handleRefresh = useCallback(() => {
        setRefreshKey(prev => prev + 1);
    }, []);

    const handleSearch = (keyword: string, tags: string[]) => {
        setQuery(prev => ({
            ...prev,
            keyword,
            tag: tags.join(","),
            page: 1
        }));
    };

    const handleAddNew = () => {
        setEditingMemo({ title: "", content: "", tags: [], userId: 0, creatTime: Date.now() });
        const toggle = document.getElementById("memo-drawer") as HTMLInputElement;
        if (toggle) toggle.checked = true;
    };

    const handleEdit = (memo: Memo) => {
        setEditingMemo(memo);
        const toggle = document.getElementById("memo-drawer") as HTMLInputElement;
        if (toggle) toggle.checked = true;
    };

    return (
        <div className="drawer drawer-end text-base-content">
            <input id="memo-drawer" type="checkbox" className="drawer-toggle" />

            <div className="drawer-content flex flex-col space-y-8">
                <FilterBar onSearch={handleSearch} />

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
                    <button
                        onClick={handleAddNew}
                        className="group card border-2 border-dashed border-base-300 rounded-xl items-center justify-center text-base-content/20 hover:border-base-content transition-all min-h-65 bg-base-100/50"
                    >
                        <DocumentPlusIcon className="w-12 h-12 mb-3 group-hover:scale-110" />
                        <span className="font-bold tracking-widest text-sm uppercase">
                            {t('memo.page.new_btn')}
                        </span>
                    </button>

                    {memos.map(memo => (
                        <MemoCard key={memo.id} memo={memo} onEdit={handleEdit} />
                    ))}
                </div>

                <div className="flex flex-col md:flex-row justify-between items-center mt-12 pt-8 border-t border-base-200 gap-6">
                    <div className="text-xs font-mono font-bold opacity-40 uppercase tracking-widest">
                        {t('memo.page.pagination_showing')}
                        <span className="text-base-content opacity-100 mx-1">
                            {total === 0 ? 0 : startRecord} - {endRecord}
                        </span>
                        {t('memo.page.pagination_of')}
                        <span className="text-base-content opacity-100 mx-1">{total}</span>
                        {t('memo.page.pagination_memos')}
                    </div>

                    <div className="join border border-base-300 rounded-xl shadow-sm bg-base-100 overflow-hidden">
                        <button
                            disabled={query.page === 1}
                            onClick={() => setQuery(prev => ({ ...prev, page: prev.page - 1 }))}
                            className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-r border-base-300"
                        >
                            {t('memo.page.pagination_prev')}
                        </button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-black border-r border-base-300 no-animation">
                            {query.page}
                        </button>
                        <button
                            /* 修正：当当前页达到总页数时禁用下一页按钮 */
                            disabled={query.page >= totalPages}
                            onClick={() => setQuery(prev => ({ ...prev, page: prev.page + 1 }))}
                            className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-l border-base-300"
                        >
                            {t('memo.page.pagination_next')}
                        </button>
                    </div>
                </div>
            </div>

            <MemoDrawer
                memo={editingMemo}
                setMemo={setEditingMemo}
                onSuccess={handleRefresh}
            />
        </div>
    );
}