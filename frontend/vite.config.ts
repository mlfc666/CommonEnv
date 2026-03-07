import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import svgr from 'vite-plugin-svgr';
// https://vite.dev/config/
export default defineConfig({
    plugins: [tailwindcss(),react(),svgr({
        // 配置：允许通过 ?react 后缀显式导入为组件（可选，但推荐）
        include: '**/*.svg',
    }),],
    server: {
        // 允许指定的 Host 访问
        allowedHosts: ['dev.mlfc.moe']
    },
    base: './',
    build: {
        // 将前端打包后的文件直接输出到 Java 的静态资源目录
        outDir: '../src/main/resources/static',
        emptyOutDir: true
    }
})