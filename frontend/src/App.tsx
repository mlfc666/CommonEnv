import {Suspense, useEffect} from 'react'
import './App.css'
import {Navbar} from "./components/Navbar.tsx";
import {useTranslation} from "react-i18next";
import {HashRouter, Navigate, Outlet, Route, Routes} from "react-router-dom";
import Sidebar from "./components/Sidebar.tsx";
import {navigation} from "./components/Navigation.tsx";
import {renderRoutes} from "./components/RenderRoutes.tsx";



const MainLayout = () => {
    return (
        <div className="h-screen flex flex-col overflow-hidden bg-base-100">
            <Navbar/>
            <div className="flex flex-1 overflow-hidden">
                <aside className="w-80 overflow-y-auto border-r border-base-200 hidden lg:block">
                    <Sidebar/>
                </aside>
                <main className="flex-1 overflow-y-auto relative">
                    <Outlet/>
                </main>
            </div>
        </div>
    );
};

function App() {
    const {t, i18n} = useTranslation();

    useEffect(() => {
        document.documentElement.lang = i18n.language;
        document.title = t('components.navbar.name');
    }, [i18n.language, t]);

    useEffect(() => {
        const savedTheme = localStorage.getItem('daisyui-theme');
        if (savedTheme) {
            document.documentElement.setAttribute('data-theme', savedTheme);
        }
    }, []);

    return (
        <HashRouter>
            <Suspense fallback={<div className="h-screen flex items-center justify-center"><span className="loading loading-spinner loading-lg"></span></div>}>
                <Routes>
                    <Route path="/" element={<MainLayout/>}>
                        <Route index  element={<Navigate to="/website" replace />} />

                        {/* 自动生成的带 Layout 的路由 */}
                        {renderRoutes(navigation)}

                        <Route path="*" element={<div>404 Not Found</div>}/>
                    </Route>
                </Routes>
            </Suspense>
        </HashRouter>
    )
}

export default App