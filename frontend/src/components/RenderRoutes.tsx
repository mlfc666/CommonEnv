// --- 递归路由生成器 (核心修改) ---
// 新增 baseKey 参数，用于递归传递翻译层级 (默认为 'sidebar')
import { Route } from "react-router-dom";
import type {NavItem} from "./Navigation.tsx";
import {PageContainer} from "./PageContainer.tsx";

export const renderRoutes = (items: NavItem[], baseKey = 'sidebar') => {
    return items.map((item) => {
        // 计算当前的翻译 Key (例如: sidebar.mods.install.frame)
        const currentTransKey = `${baseKey}.${item.key}`;

        // 预处理 Element：如果有 element，就自动包一层 PageContainer
        let wrappedElement = null;
        if (item.element) {
            wrappedElement = (
                <PageContainer transKey={currentTransKey}>
                    {item.element}
                </PageContainer>
            );
        }

        // 渲染子路由 (文件夹)
        if (item.children && item.children.length > 0) {
            return (
                <Route key={item.key} path={item.key}>
                    {/* 如果这个文件夹本身也有页面 (item.element)，作为 index 渲染 */}
                    {wrappedElement && <Route index element={wrappedElement}/>}

                    {/* 递归渲染子项，传入当前的 Key 作为下一层的 baseKey */}
                    {renderRoutes(item.children, currentTransKey)}
                </Route>
            );
        }

        // 渲染叶子路由
        if (wrappedElement) {
            return <Route key={item.key} path={item.key} element={wrappedElement}/>;
        }

        return null;
    });
}