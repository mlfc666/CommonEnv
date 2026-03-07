# 觉知备忘录 - 前端开发全量技术文档

## 查看Api设计文档
* **API说明文档**：[查看 Api 设计文档](./API_README.md)
## 1. 项目基础与构建配置 (Build & Base)

前端基于 Vite + React + TypeScript 架构，整合了 Tailwind CSS 与 DaisyUI 提升 UI 开发效率。

| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `vite.config.ts` | **构建配置文件** | 集成 TailwindCSS、React、SVGR 插件。配置开发服务器代理（Proxy）将 `/api` 请求转发至 8080 端口。定义 `outDir` 将打包产物输出至后端静态资源目录。 |
| `main.tsx` | **应用入口点** | 初始化 React 根节点，引入全局 i18n 配置，挂载 `App` 组件。 |
| `App.tsx` | **顶层架构组件** | 定义 `MainLayout` 布局结构。实现主题与语言的初始化加载。使用 `HashRouter` 管理路由，集成 `Suspense` 处理异步组件加载。 |
| `App.css` | **全局样式表** | 引入 Tailwind 指令，通过 `@plugin` 引入 DaisyUI 及其主题配置。 |

---

## 2. 状态管理与基础设施 (Managers & Config)

负责全局配置、多语言、主题切换及资源映射。

| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `manager/i18n.ts` | **国际化配置中心** | 整合 `zh`, `en`, `zhTW` 资源。配置语言探测器与后端加载插件，支持多语言动态切换。 |
| `manager/ThemeManger.ts` | **主题管理器** | 定义 15 种内置主题。通过 `document.documentElement` 修改 `data-theme` 属性并实现 localStorage 持久化。 |
| `config/DataSourceConfig.ts` | **资源路径配置** | 定义图片、Markdown 文件及文件下载服务器的基础路径。内置 `getQQImage` 函数用于解析 QQ 头像接口。 |

---

## 3. 服务层与数据通讯 (Services & API)

封装了与后端 Java API 的所有异步请求逻辑。

| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `services/userService.ts` | **用户业务服务** | 使用 Web Crypto API 实现 SHA-256 前端哈希。封装登录、注册、资料获取、头像上传（FormData）、更名、改密及注销接口。 |
| `services/memoService.ts` | **备忘录业务服务** | 封装备忘录的列表查询（POST 分页）、创建、覆盖更新、物理删除及标签/总数信息统计接口。 |

---

## 4. 类型系统定义 (Types & DTOs)

采用 TypeScript 严格定义数据结构，确保前后端数据契约一致。

| 文件名 | 类型定义内容 | 用途 |
| :--- | :--- | :--- |
| `types/ApiResponse.ts` | `ApiResponse<T>` | 封装标准的后端响应结构（code, message, data）。 |
| `types/User.ts` | `User` | 用户实体基础接口。 |
| `types/UserInfoDTO.ts` | `UserInfoDTO` | 用户个人资料返回对象。 |
| `types/LoginDTO.ts` | `LoginDTO` | 包含用户名、密码哈希及 CF 令牌的请求对象。 |
| `types/Memo.ts` | `Memo` | 备忘录实体，支持可选的 `tags` 数组。 |
| `types/MemoQueryDTO.ts` | `MemoQueryDTO` | 分页与条件过滤的组合查询对象。 |
| `types/MemoInfoDTO.ts` | `MemoInfoDTO` | 包含标签池及总记录数的统计对象。 |
| `types/PasswordUpdateDTO.ts` | `PasswordUpdateDTO` | 密码变更所需的旧密码与新密码载荷。 |
| `types/index.ts` | `Post`, `SiteConfig` | 站点通用元数据与配置接口。 |
| `types/global.d.ts` | `Window` | 扩展全局对象，定义 Cloudflare Turnstile 的方法签名。 |

---

## 5. 自定义钩子 (Custom Hooks)

提取可复用的逻辑片段，提升组件的纯粹性。

| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `hooks/useRemoteMarkdown.ts` | **远程文档加载钩子** | 根据当前语言环境拼接 URL。实现内存级 `markdownCache` 防止重复请求。处理加载状态与异常捕获。 |
| `hooks/useTurnstile.ts` | **人机验证集成钩子** | 管理 Turnstile 挂载节点的引用。通过回调函数获取 `cfToken` 并负责组件销毁时的资源清理。 |

---

## 6. 通用 UI 组件 (Components)

### 6.1 布局与导航类
| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `components/Navbar.tsx` | **顶部导航栏** | 集成主题下拉菜单、语言切换。监听路由变化，动态同步显示当前登录用户的头像状态。 |
| `components/Sidebar.tsx` | **左侧侧边栏** | 递归渲染 `NavItem`。根据当前路径高亮菜单。支持文件夹折叠与顶级菜单字体加粗逻辑。 |
| `components/Navigation.tsx` | **路由元数据配置** | 集中定义侧边栏的 Key、路径、图标及对应的 Lazy 组件。 |
| `components/RenderRoutes.tsx` | **递归路由生成器** | 将 `navigation` 配置转换为 React Router 的 `Route` 树。自动为所有页面包裹 `PageContainer`。 |
| `components/PageContainer.tsx` | **页面标准容器** | 统一处理页面内边距、标题展示。针对科技树等特殊页面实现滚动条控制策略。 |

### 6.2 文档渲染与增强类
| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `components/MarkdownRender.tsx` | **核心渲染引擎** | 基于 `react-markdown`。自定义拦截标题、链接（FileCard）、代码块（CodeBlock）及图片。实现 QQ 头像协议（`qq:123`）的转换。 |
| `components/RemoteMarkdownRender.tsx` | **远程文档封装器** | 结合 `useRemoteMarkdown` 钩子，提供统一的 Skeleton 加载动画与错误提示界面。 |
| `components/CodeBlock.tsx` | **代码高亮组件** | 集成 `prism` 高亮。支持明暗主题切换、一键复制代码。具备行号显示逻辑。 |
| `components/FileCard.tsx` | **附件展示卡片** | 拦截 Markdown 中的特定下载链接。识别 ZIP/普通文件图标，提供下载引导界面。 |
| `components/ContributorAvatar.tsx` | **贡献者头像组件** | 解析 QQ 接口生成头像。支持悬停 Tooltip 及加载失败时的文字兜底显示。 |

---

## 7. 页面模块 (Pages)

### 7.1 认证模块
| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `pages/LoginPage.tsx` | **登录页面** | 集成 Turnstile。处理哈希加密流程。登录成功后持久化 JWT 至 localStorage。 |
| `pages/RegisterPage.tsx` | **注册页面** | 提供新账户创建界面。执行人机验证，完成后自动执行身份认证跳转。 |

### 7.2 个人中心模块
| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `pages/person/UserInfoPage.tsx` | **个人信息页** | 包含 `AvatarCard` 与 `InfoForm`。实现头像文件选取即上传，支持昵称修改的前端合法性校验。 |
| `pages/person/SecurityPage.tsx` | **安全设置页** | 提供改密表单及危险操作区（注销账户）。实现身份状态的实时核验，未授权则强制重定向。 |

### 7.3 备忘录业务模块
| 文件名 | 职责描述 | 核心逻辑点 |
| :--- | :--- | :--- |
| `pages/memo/MemoPage.tsx` | **主列表页** | 协调 `FilterBar` 与 `MemoCard`。实现分页逻辑计算（totalPages, startRecord）。管理侧边栏的开启与关闭状态。 |
| `pages/memo/components/FilterBar.tsx` | **高级搜索栏** | 获取全量标签 pool。实现关键字、多选标签及预设时间段（3/7天）的组合过滤。 |
| `pages/memo/components/MemoCard.tsx` | **简报卡片** | 展示备忘录标题、摘要及标签云。提供悬停可见的快速编辑入口。 |
| `pages/memo/components/MemoDrawer.tsx` | **编辑抽屉** | 处理备忘录的新增、覆盖更新及物理删除操作。通过英文逗号自动解析标签数组。 |

---

## 8. 语言包与资源 (Locales & Icons)

| 文件名 | 职责描述 |
| :--- | :--- |
| `locales/zh.ts` | 维护简体中文的全量 UI 文案、提示消息及错误异常翻译。 |
| `locales/en.ts` | 维护英文对应的翻译资源，确保 DTO 字段映射的一致性。 |
| `locales/zhTW.ts` | 维护繁体中文翻译，提供符合地区习惯的用词描述。 |
| `icons/svgIcons.ts` | **SVG 资源池** | 通过 Vite 插件将 SVG 文件导入为 React 组件并集中注册。 |
| `icons/SvgIcon.tsx` | **通用图标组件** | 提供统一的 `size` 与 `name` 属性，强制设置 `currentColor` 填充以适配 CSS 主题。 |
| `icons/declarations.d.ts` | **TS 类型声明** | 定义模块化导入 SVG 文件的类型签名。 |

---

## 9. 核心业务流程描述

1.  **认证流程**：用户输入明文密码 -> 前端执行 SHA-256 -> 配合 Cloudflare Token 提交 -> 后端校验并返回 JWT -> 前端存入 `localStorage`。
2.  **受限请求流程**：`userService/memoService` 发起 fetch -> 从 `localStorage` 获取令牌 -> 注入 `Authorization: Bearer <token>` -> 后端拦截器校验。
3.  **文档加载流程**：`RemoteMarkdownRender` 触发 -> `useRemoteMarkdown` 检查内存缓存 -> 发起异步请求获取远程 `.md` 文本 -> `MarkdownRender` 进行 AST 解析并应用自定义 UI 组件。
4.  **主题/语言持久化**：应用启动 -> `App.tsx` 触发 useEffect -> 从 `localStorage` 读取 `daisyui-theme` 或 `i18nextLng` -> 应用于 DOM 节点。