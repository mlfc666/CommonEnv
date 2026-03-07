// 显式定义 Turnstile 渲染选项接口
interface TurnstileOptions {
    sitekey: string;
    callback?: (token: string) => void;
    "error-callback"?: () => void;
    "expired-callback"?: (token: string) => void;
    "timeout-callback"?: () => void;
    theme?: 'light' | 'dark' | 'auto';
    language?: string;
    appearance?: 'always' | 'execute' | 'interaction-only';
    retry?: 'auto' | 'never';
    "retry-interval"?: number;
    size?: 'normal' | 'flexible' | 'compact';

}

// 扩展全局 Window 接口
interface Window {
    turnstile: {
        render: (container: string | HTMLElement, options: TurnstileOptions) => string;
        reset: (widgetId?: string) => void;
        getResponse: (widgetId?: string) => string;
        remove: (widgetId?: string) => void;
    };
}