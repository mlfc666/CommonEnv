import React, { useEffect, useRef, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { UserIcon, KeyIcon, ArrowRightOnRectangleIcon } from "@heroicons/react/24/outline";
import {userService} from "../services/userService.ts";

export default function LoginPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const turnstileRef = useRef<HTMLDivElement>(null);
    const [cfToken, setCfToken] = useState<string | null>(null);

    // 挂载 Cloudflare 人机验证
    useEffect(() => {
        let widgetId: string;
        if (window.turnstile && turnstileRef.current) {
            widgetId = window.turnstile.render(turnstileRef.current, {
                sitekey: '0x4AAAAAACnWdLj7v2MmY7cI',
                callback: (token: string) => setCfToken(token),
                appearance: 'always',
                theme: 'light',
                size: 'flexible'
            });
        }
        // 清理函数：防止 React 18 严格模式下重复渲染
        return () => {
            if (widgetId && window.turnstile) {
                window.turnstile.remove(widgetId);
            }
        };
    }, []);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!cfToken) return alert("请完成人机验证");

        const hashedPassword = await userService.hashPassword(password);
        const res = await userService.login({ username, password: hashedPassword, cfToken });

        if (res.code === 200) {
            localStorage.setItem("jwt_token", res.data);
            navigate("/website");
        } else {
            alert(res.message);
        }
    };

    return (
        <div className="min-h-[80vh] flex items-center justify-center">
            <div className="card w-full max-w-md bg-base-100 border border-base-300 rounded-xl shadow-none p-10">
                <div className="flex flex-col items-center mb-10 text-center">
                    <div className="p-4 bg-base-200 rounded-2xl mb-4">
                        <ArrowRightOnRectangleIcon className="w-8 h-8" />
                    </div>
                    <h1 className="text-2xl font-bold tracking-tight">欢迎回来</h1>
                    <p className="text-sm opacity-50 mt-2 text-balance">请输入您的凭据以访问控制面板</p>
                </div>

                <form onSubmit={handleLogin} className="space-y-6">
                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-60">用户名</legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <UserIcon className="w-5 h-5 opacity-40" />
                            <input type="text" value={username} onChange={e => setUsername(e.target.value)} className="grow" placeholder="Your username" required />
                        </label>
                    </fieldset>

                    <fieldset className="fieldset p-0">
                        <legend className="fieldset-legend font-bold text-xs uppercase opacity-60">密码</legend>
                        <label className="input input-bordered flex items-center gap-3 h-12 rounded-lg">
                            <KeyIcon className="w-5 h-5 opacity-40" />
                            <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="grow" placeholder="••••••••" required />
                        </label>
                    </fieldset>

                    {/* Turnstile 容器 */}
                    <div ref={turnstileRef} className="flex"></div>

                    <button type="submit" className="btn bg-base-content text-base-100 btn-block h-12 rounded-lg font-bold hover:bg-black transition-all">
                        登录系统
                    </button>
                </form>

                <div className="mt-8 text-center text-sm opacity-60">
                    还没有账号? <Link to="/register" className="font-bold text-base-content hover:underline">立即注册</Link>
                </div>
            </div>
        </div>
    );
}