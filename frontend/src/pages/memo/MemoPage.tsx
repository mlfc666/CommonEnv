import {useState, useEffect, useCallback} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {DocumentPlusIcon} from "@heroicons/react/24/outline";
import {memoService} from "../../services/memoService.ts";
import {userService} from "../../services/userService.ts";
import type {Memo} from "../../types/Memo.ts";
import type {MemoQueryDTO} from "../../types/MemoQueryDTO.ts";
import {MemoDrawer} from "./components/MemoDrawer.tsx";
import {FilterBar} from "./components/FilterBar.tsx";
import {MemoCard} from "./components/MemoCard.tsx";

// 备忘录容器组件负责执行全局身份校验、数据调度与子组件状态同步逻辑
export default function MemoPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();

    // 存储当前显示的备忘录记录数组
    const [memos, setMemos] = useState<Memo[]>([]);
    // 存储当前处于编辑或新建流程中的备忘录对象
    const [editingMemo, setEditingMemo] = useState<Memo | null>(null);
    // 用于驱动 Effect 重新执行数据拉取的计数器状态
    const [refreshKey, setRefreshKey] = useState<number>(0);

    // 维护列表检索参数与分页信息的统一查询对象
    const [query, setQuery] = useState<MemoQueryDTO>({
        page: 1,
        size: 9,
        keyword: "",
        tag: ""
    });

    // 执行身份合法性检查与业务数据拉取任务
    useEffect(() => {
        let isMounted = true;
        const loadPageData = async () => {
            try {
                // 首先验证当前会话的身份令牌是否依然有效
                const authRes = await userService.getUserInfo();
                if (authRes.code !== 200) {
                    if (isMounted) navigate("/login");
                    return;
                }

                // 身份确认通过后执行业务列表数据加载
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
        return () => {
            isMounted = false;
        };
    }, [query, refreshKey, navigate, t]);

    // 递增刷新键以通知容器组件触发数据的异步重载流程
    const handleRefresh = useCallback(() => {
        setRefreshKey(prev => prev + 1);
    }, []);

    // 变更查询状态中的筛选参数并重置当前页码
    const handleSearch = (keyword: string, tags: string[]) => {
        setQuery(prev => ({
            ...prev,
            keyword,
            tag: tags.join(","),
            page: 1
        }));
    };

    // 初始化新的数据结构并唤起侧边栏进行录入
    const handleAddNew = () => {
        setEditingMemo({title: "", content: "", tags: [], userId: 0, creatTime: Date.now()});
        const toggle = document.getElementById("memo-drawer") as HTMLInputElement;
        if (toggle) toggle.checked = true;
    };

    // 载入选中的记录副本并唤起侧边栏进行内容修订
    const handleEdit = (memo: Memo) => {
        setEditingMemo(memo);
        const toggle = document.getElementById("memo-drawer") as HTMLInputElement;
        if (toggle) toggle.checked = true;
    };

    return (
        <div className="drawer drawer-end text-base-content">
            <input id="memo-drawer" type="checkbox" className="drawer-toggle"/>

            <div className="drawer-content flex flex-col space-y-8">
                {/* 筛选条组件接收搜索回调以更新容器状态 */}
                <FilterBar onSearch={handleSearch}/>

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
                    {/* 创建新记录的入口卡片按钮 */}
                    <button
                        onClick={handleAddNew}
                        className="group card border-2 border-dashed border-base-300 rounded-xl items-center justify-center text-base-content/20 hover:border-base-content transition-all min-h-65 bg-base-100/50"
                    >
                        <DocumentPlusIcon className="w-12 h-12 mb-3 group-hover:scale-110"/>
                        <span className="font-bold tracking-widest text-sm uppercase">
                            {t('memo.page.new_btn')}
                        </span>
                    </button>

                    {/* 动态渲染业务卡片列表 */}
                    {memos.map(memo => (
                        <MemoCard key={memo.id} memo={memo} onEdit={handleEdit}/>
                    ))}
                </div>

                {/* 底部导航与翻页操作区域 */}
                <div
                    className="flex flex-col md:flex-row justify-between items-center mt-12 pt-8 border-t border-base-200 gap-6">
                    <div className="text-xs font-mono font-bold opacity-40 uppercase tracking-widest">
                        {t('memo.page.pagination_showing')}
                        <span className="text-base-content opacity-100 mx-1">
                            {(query.page - 1) * query.size + 1} - {query.page * query.size}
                        </span>
                        {t('memo.page.pagination_of')}
                        <span className="text-base-content opacity-100 mx-1">--</span>
                        {t('memo.page.pagination_memos')}
                    </div>

                    <div className="join border border-base-300 rounded-xl shadow-sm bg-base-100 overflow-hidden">
                        <button
                            disabled={query.page === 1}
                            onClick={() => setQuery(prev => ({...prev, page: prev.page - 1}))}
                            className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-r border-base-300"
                        >
                            {t('memo.page.pagination_prev')}
                        </button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-black no-animation">
                            {query.page}
                        </button>
                        <button
                            onClick={() => setQuery(prev => ({...prev, page: prev.page + 1}))}
                            className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-l border-base-300"
                        >
                            {t('memo.page.pagination_next')}
                        </button>
                    </div>
                </div>
            </div>

            {/* 注入刷新回调以确保操作成功后视图实时更新 */}
            <MemoDrawer
                memo={editingMemo}
                setMemo={setEditingMemo}
                onSuccess={handleRefresh}
            />
        </div>
    );
}