import { useEffect, useRef, useState } from "react";

export function useTurnstile(siteKey: string) {
    const turnstileRef = useRef<HTMLDivElement>(null);
    const [cfToken, setCfToken] = useState<string | null>(null);

    useEffect(() => {
        let widgetId: string;
        if (window.turnstile && turnstileRef.current) {
            widgetId = window.turnstile.render(turnstileRef.current, {
                sitekey: siteKey,
                callback: (token: string) => setCfToken(token),
                appearance: 'always',
                theme: 'light',
                size: 'flexible'
            });
        }
        return () => {
            if (widgetId && window.turnstile) {
                window.turnstile.remove(widgetId);
            }
        };
    }, [siteKey]);

    return { turnstileRef, cfToken };
}