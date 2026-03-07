import { useState, useEffect, useCallback } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { DocumentPlusIcon } from "@heroicons/react/24/outline";
import { memoService } from "../../services/memoService.ts";
import type { Memo } from "../../types/Memo.ts";
import type { MemoQueryDTO } from "../../types/MemoQueryDTO.ts";
import { MemoDrawer } from "./components/MemoDrawer.tsx";
import { FilterBar } from "./components/FilterBar.tsx";
import { MemoCard } from "./components/MemoCard.tsx";

// 备忘录管理页面作为容器组件负责数据调度与状态同步逻辑
export default function MemoPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    // 存储从后端拉取的备忘录列表数据
    const [memos, setMemos] = useState<Memo[]>([]);
    // 存储当前正在编辑或准备创建的备忘录对象
    const [editingMemo, setEditingMemo] = useState<Memo | null>(null);
    // 用于强制触发 useEffect 重新拉取数据的计数器状态
    const [refreshKey, setRefreshKey] = useState<number>(0);

    // 维护列表查询与分页参数的统一状态对象
    const [query, setQuery] = useState<MemoQueryDTO>({
        page: 1,
        size: 9,
        keyword: "",
        tag: ""
    });

    // 监听查询条件或刷新键的变化以执行异步数据获取任务
    useEffect(() => {
        const loadData = async () => {
            try {
                const res = await memoService.getMemos(query);
                if (res.code === 200) {
                    setMemos(res.data);
                } else if (res.code === 401) {
                    navigate("/login");
                }
            } catch (error) {
                console.error("加载备忘录列表数据失败" +  error);
            }
        };
        void loadData();
    }, [query, refreshKey, navigate]);

    // 递增刷新键以触发列表数据的异步重载逻辑
    const handleRefresh = useCallback(() => {
        setRefreshKey(prev => prev + 1);
    }, []);

    // 切换右侧侧边栏组件的物理显示状态
    const toggleDrawer = (open: boolean) => {
        const checkbox = document.getElementById("memo-drawer") as HTMLInputElement;
        if (checkbox) checkbox.checked = open;
    };

    // 初始化空白数据结构并唤起侧边栏执行新建操作
    const handleAddNew = () => {
        setEditingMemo({ title: "", content: "", tags: [], userId: 0, creatTime: Date.now() });
        toggleDrawer(true);
    };

    // 载入选中的备忘录记录并唤起侧边栏执行编辑操作
    const handleEdit = (memo: Memo) => {
        setEditingMemo(memo);
        toggleDrawer(true);
    };

    // 更新查询状态中的页码参数以触发数据重载
    const handlePageChange = (newPage: number) => {
        setQuery(prev => ({ ...prev, page: newPage }));
    };

    return (
        <div className="drawer drawer-end">
            <input id="memo-drawer" type="checkbox" className="drawer-toggle" />

            <div className="drawer-content flex flex-col space-y-8">
                {/* 筛选与搜索工具条组件 */}
                <FilterBar />

                {/* 备忘录卡片展示网格 */}
                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
                    {/* 新建备忘录的可点击占位卡片 */}
                    <button onClick={handleAddNew} className="group card border-2 border-dashed border-base-300 rounded-xl items-center justify-center text-base-content/20 hover:border-base-content hover:text-base-content transition-all min-h-65 bg-base-100/50">
                        <DocumentPlusIcon className="w-12 h-12 mb-3 group-hover:scale-110 transition-transform" />
                        <span className="font-bold tracking-widest text-sm uppercase">
                            {t('memo.page.new_btn')}
                        </span>
                    </button>

                    {/* 循环渲染已有的备忘录卡片 */}
                    {memos.map(memo => (
                        <MemoCard key={memo.id} memo={memo} onEdit={handleEdit} />
                    ))}
                </div>

                {/* 分页控制与统计信息区域 */}
                <div className="flex flex-col md:flex-row justify-between items-center mt-12 pt-8 border-t border-base-200 gap-6">
                    <div className="text-xs font-mono font-bold opacity-40 uppercase tracking-widest">
                        {t('memo.page.pagination_showing')} <span className="text-base-content opacity-100">{(query.page - 1) * query.size + 1} - {Math.min(query.page * query.size, 999)}</span> {t('memo.page.pagination_of')} <span className="text-base-content opacity-100">--</span> {t('memo.page.pagination_memos')}
                    </div>

                    <div className="join border border-base-300 rounded-xl shadow-sm bg-base-100 overflow-hidden">
                        <button
                            disabled={query.page === 1}
                            onClick={() => handlePageChange(query.page - 1)}
                            className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-r border-base-300"
                        >
                            {t('memo.page.pagination_prev')}
                        </button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-black border-r border-base-300 no-animation">
                            {query.page}
                        </button>
                        <button
                            onClick={() => handlePageChange(query.page + 1)}
                            className="join-item btn btn-ghost btn-md px-6 text-xs uppercase tracking-widest border-l border-base-300"
                        >
                            {t('memo.page.pagination_next')}
                        </button>
                    </div>
                </div>
            </div>

            {/* 负责编辑与删除逻辑的侧边栏组件 */}
            <MemoDrawer
                memo={editingMemo}
                setMemo={setEditingMemo}
                onSuccess={handleRefresh}
            />
        </div>
    );
}