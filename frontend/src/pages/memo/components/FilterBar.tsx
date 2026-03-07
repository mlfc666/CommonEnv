import React from "react";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";

export const FilterBar: React.FC = () => {
    return (
        <div className="flex flex-wrap gap-4 bg-base-100 border border-base-300 p-6 rounded-xl items-center shadow-sm">
            <label className="input input-bordered flex items-center gap-2 grow h-12 rounded-lg">
                <MagnifyingGlassIcon className="w-5 h-5 opacity-40" />
                <input type="text" placeholder="搜索关键词..." className="grow text-sm" />
            </label>
            <select className="select select-bordered h-12 rounded-lg min-w-35">
                <option>全部标签</option>
                <option>工作</option>
                <option>生活</option>
            </select>
            <select className="select select-bordered h-12 rounded-lg min-w-35">
                <option>全部时间范围</option>
                <option value="3">最近 3 天</option>
                <option value="7">最近 7 天</option>
            </select>
            <button className="btn bg-base-content text-base-100 h-12 px-10 rounded-lg hover:bg-black transition-all">搜索</button>
        </div>
    );
};