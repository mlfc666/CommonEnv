import { useState } from "react";
import { useTranslation } from "react-i18next";
import { DocumentPlusIcon } from "@heroicons/react/24/outline";
import type { Memo } from "../../types/Memo.ts";
import { MemoDrawer } from "./components/MemoDrawer.tsx";
import { FilterBar } from "./components/FilterBar.tsx";
import { MemoCard } from "./components/MemoCard.tsx";

// 备忘录管理页面负责协调筛选列表展示与侧边栏编辑逻辑
export default function MemoPage() {
    const { t } = useTranslation();

    // 初始化模拟数据
    const [memos] = useState<Memo[]>([
        { id: 1, userId: 1, title: "开发第四周总结", content: "完成备忘录页面的开发，适配DaisyUI极简工业风。下一步是对接后端接口，实现数据持久化。目前进度良好，样式已基本对齐。", tags: ["工作", "紧急"], creatTime: 1700000000000 },
        { id: 2, userId: 1, title: "购买清单", content: "牛奶, 面包, 咖啡豆, 还有一些生活用品，记得带上环保购物袋。顺便看看有没有新鲜的水果。", tags: ["生活"], creatTime: 1700100000000 }
    ]);

    const [editingMemo, setEditingMemo] = useState<Memo | null>(null);

    // 切换侧边栏的显隐状态
    const toggleDrawer = (open: boolean) => {
        const checkbox = document.getElementById("memo-drawer") as HTMLInputElement;
        if (checkbox) checkbox.checked = open;
    };

    // 初始化一个空的备忘录对象并开启侧边栏
    const handleAddNew = () => {
        setEditingMemo({ title: "", content: "", tags: [], userId: 1, creatTime: Date.now() });
        toggleDrawer(true);
    };

    // 填充选中的备忘录数据并开启侧边栏
    const handleEdit = (memo: Memo) => {
        setEditingMemo(memo);
        toggleDrawer(true);
    };

    return (
        <div className="drawer drawer-end">
            <input id="memo-drawer" type="checkbox" className="drawer-toggle" />
            <div className="drawer-content flex flex-col space-y-8">
                <FilterBar />

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
                    {/* 新建入口卡片 */}
                    <button onClick={handleAddNew} className="group card border-2 border-dashed border-base-300 rounded-xl items-center justify-center text-base-content/20 hover:border-base-content hover:text-base-content transition-all min-h-65 bg-base-100/50">
                        <DocumentPlusIcon className="w-12 h-12 mb-3 group-hover:scale-110 transition-transform" />
                        <span className="font-bold tracking-widest text-sm uppercase">
                            {t('memo.page.new_btn')}
                        </span>
                    </button>

                    {memos.map(memo => (
                        <MemoCard key={memo.id} memo={memo} onEdit={handleEdit} />
                    ))}
                </div>

                {/* 分页控制区域 */}
                <div className="flex flex-col md:flex-row justify-between items-center mt-12 pt-8 border-t border-base-200 gap-6">
                    <div className="text-xs font-mono font-bold opacity-40 uppercase tracking-widest">
                        {t('memo.page.pagination_showing')} <span className="text-base-content opacity-100">1-9</span> {t('memo.page.pagination_of')} <span className="text-base-content opacity-100">42</span> {t('memo.page.pagination_memos')}
                    </div>

                    <div className="join border border-base-300 rounded-xl shadow-sm bg-base-100 overflow-hidden">
                        <button className="join-item btn btn-ghost btn-md px-8 text-xs uppercase tracking-widest border-r border-base-300">
                            {t('memo.page.pagination_prev')}
                        </button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-black border-r border-base-300">1</button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-bold opacity-30 border-r border-base-300">2</button>
                        <button className="join-item btn btn-ghost btn-md px-6 font-bold opacity-30">3</button>
                        <button className="join-item btn btn-ghost btn-md px-6 text-xs uppercase tracking-widest border-l border-base-300">
                            {t('memo.page.pagination_next')}
                        </button>
                    </div>
                </div>
            </div>

            {/* 侧边栏编辑组件 */}
            <MemoDrawer memo={editingMemo} setMemo={setEditingMemo} />
        </div>
    );
}