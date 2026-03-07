import React, {lazy} from 'react';
import {
    AcademicCapIcon,
    AtSymbolIcon,
    ClipboardDocumentIcon,
    UserCircleIcon
} from "@heroicons/react/24/outline";

const IntroductionPage = lazy(() => import('../pages/IntroductionPage.tsx'));
const MarkdownTuitionPage = lazy(() => import('../pages/MarkdownTuitionPage.tsx'));
const SecurityPage = lazy(() => import('../pages/person/SecurityPage.tsx'));
const UserInfoPage = lazy(() => import('../pages/person/UserInfoPage.tsx'));
const MemoPage = lazy(() => import('../pages/memo/MemoPage.tsx'));

export type NavItem = {
    key: string;            // 这里的 key 将作为路径的一部分，也是翻译 key 的一部分
    path?: string;          // 可选：如果填了，就强制使用这个路径；不填则自动生成
    element?: React.ReactNode; // 该路由对应的页面组件
    icon?: React.ReactNode;
    children?: NavItem[];
    badge?: {
        text: string;
        color: string;
    };
};

export const navigation: NavItem[] = [
    {
        key: 'website',
        icon: <AtSymbolIcon className="w-5 h-5"/>,
        element: <IntroductionPage/>,
    },
    {
        key: 'memo',
        icon: <ClipboardDocumentIcon className="w-5 h-5"/>,
        element: <MemoPage/>,
    },
    {
        key: 'md',
        icon: <AcademicCapIcon className="w-5 h-5"/>,
        element: <MarkdownTuitionPage/>
    },
    {
        key: 'person',
        icon:<UserCircleIcon className="w-5 h-5"/>,
        element: <MarkdownTuitionPage/>,
        children: [
            {
                key: 'info',
                element: <UserInfoPage/>,

            },
            {
                key: 'security',
                element: <SecurityPage/>,
            },
        ],
    },
];