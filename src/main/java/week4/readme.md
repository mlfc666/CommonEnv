# 备忘录管理系统 - 全量开发技术文档

## 查看Api设计文档
* **API说明文档**：[查看 Api 设计文档](./API_README.md)

## 1. 系统架构综述

本系统采用分层架构设计，从底层基础设施到上层表现层实现了高度解耦。核心亮点在于自研的轻量级 Web 框架，该框架基于 Java 反射机制实现了类似 Spring Boot 的注解驱动开发模式。

### 1.1 数据库物理设计
系统采用 MySQL 存储，通过 `DBInitializer` 确保表结构的自动同步与完整性。

| 表名 | 核心职责 | 关键约束 |
| :--- | :--- | :--- |
| `users` | 存储用户信息、加盐密码及登录状态 | `username` 唯一约束，`id` 主键自增 |
| `memos` | 存储备忘录标题、内容、标签及归属 | `user_id` 外键关联，支持级联删除 |

---

## 2. 框架层核心组件 (Framework)

框架层负责处理底层的 HTTP 通信、路由分发、依赖注入及安全校验。

### 2.1 核心处理器与容器
| 类名 | 职责描述 |
| :--- | :--- |
| `DispatcherHandler` | **中央调度器**：解析请求路径，执行参数绑定（JSON/Query/Multipart），调用控制器并封装统一响应。 |
| `BeanContainer` | **单例容器**：管理 Service 实例，支持框架层进行依赖查找与注入。 |
| `RouteScanner` | **路由扫描器**：扫描包路径，解析类和方法上的 Web 注解，构建路由映射表。 |
| `AuthValidator` | **校验接口**：定义业务层自定义鉴权逻辑的标准接口。 |

### 2.2 注解定义 (Annotations)
| 注解名 | 作用范围 | 详细用途 |
| :--- | :--- | :--- |
| `@RestController` | 类 | 标记该类为 API 控制器，由框架托管。 |
| `@GetMapping` | 方法 | 绑定 HTTP GET 请求路径。 |
| `@PostMapping` | 方法 | 绑定 HTTP POST 请求路径。 |
| `@RequiresAuth` | 类/方法 | 标记接口需要进行 JWT 令牌校验。 |
| `@Inject` | 字段 | 触发自动依赖注入（DI）。 |
| `@RequestBody` | 参数 | 将请求体 JSON 映射为 Java 对象。 |
| `@RequestParam` | 参数 | 提取 URL 查询参数或表单字段。 |
| `@RequestPart` | 参数 | 接收 `multipart/form-data` 中的文件流。 |
| `@AuthClaim` | 参数 | 自动注入 JWT Payload 中的指定字段（如 `uid`）。 |

### 2.3 框架模型与工具 (Models & Utils)
| 类名 | 职责描述 |
| :--- | :--- |
| `ApiResponse` | **标准响应格式**：包含 `code`, `message`, `data` 的 Record 记录。 |
| `RouteInfo` | **路由元数据**：存储控制器实例、方法引用及权限配置。 |
| `MultipartFile` | **文件包装器**：承载上传文件的文件名、类型及二进制数据。 |
| `JwtUtils` | **安全令牌工具**：负责 JWT 的签发、签名校验、过期检查及载荷解析。 |
| `MultipartUtils` | **流解析工具**：手动解析 RFC 7578 标准的字节流，提取文件数据。 |
| `CloudflareUtils` | **人机校验工具**：对接 Cloudflare Turnstile API 进行服务端二次验证。 |

---

## 3. 业务应用层 (App Layer)

### 3.1 控制器层 (Controllers)
| 类名 | 职责描述 |
| :--- | :--- |
| `UserController` | 暴露用户生命周期接口：注册、登录、信息查询、头像上传、密码修改及注销。 |
| `MemoController` | 暴露备忘录业务接口：增删改查、多维组合搜索及标签统计。 |

### 3.2 服务层 (Services)
| 接口 / 实现类 | 核心业务逻辑 |
| :--- | :--- |
| `UserService` / `UserServiceImpl` | 处理加盐哈希加密、Token 时效逻辑（LogoutTime）、物理文件存储管理、人机校验逻辑集成。 |
| `MemoService` / `MemoServiceImpl` | 负责业务权限隔离（只能操作自己的备忘录）、数据清洗（标签去重）、创建时间戳生成。 |

### 3.3 持久化层 (Repositories)
| 接口 / 实现类 | SQL 逻辑描述 |
| :--- | :--- |
| `UserRepository` / `UserRepositoryImpl` | 用户表 CRUD，包含特定的 `logout_time` 更新逻辑以支持令牌撤回。 |
| `MemoRepository` / `MemoRepositoryImpl` | 核心在于 `findByQuery`，利用 StringBuilder 实现关键字、标签、时间段的动态 SQL 拼接。 |

### 3.4 业务模型与传输对象 (Models & DTOs)
| 类名 | 类型 | 用途描述 |
| :--- | :--- | :--- |
| `User` | Entity | 映射 `users` 表的实体对象。 |
| `Memo` | Entity | 映射 `memos` 表的实体对象，支持 `tags` 的数组-字符串转换。 |
| `LoginDTO` | Record | 承载登录/注册参数，包含用户名、密码及人机验证令牌。 |
| `MemoQueryDTO` | Record | 封装复杂搜索条件，支持关键字、标签、筛选天数及分页参数。 |
| `UserInfoDTO` | Record | 返回给前端的脱敏用户信息。 |
| `MemoInfoDTO` | Record | 返回备忘录统计信息，包含扁平化处理后的唯一标签集合。 |
| `PasswordUpdateDTO` | Record | 封装修改密码所需的旧密码校验值与新密码。 |

---

## 4. 基础设施与系统组件 (Infrastructure)

### 4.1 核心入口与服务器
| 类名 | 职责描述 |
| :--- | :--- |
| `Main` | 程序主入口：初始化数据库服务、启动 Web 容器，并提供控制台管理菜单。 |
| `WebServer` | Web 容器配置类：注册 Bean、注入验证器、启动反射扫描并绑定端口。 |
| `Database` | DDL 维护类：定义物理表结构及初始化顺序。 |

### 4.2 请求处理器 (Handlers)
| 类名 | 职责描述 |
| :--- | :--- |
| `MasterHandler` | **总线处理器**：根据路径前缀将请求分发至 `DispatcherHandler` (API) 或 `StaticHandler` (静态资源)。 |
| `StaticHandler` | **资源处理器**：处理 JAR 包内 `/static` 资源及磁盘 `/uploads` 头像文件的访问。 |

### 4.3 工具类与安全 (Utils & Security)
| 类名 | 职责描述 |
| :--- | :--- |
| `AppAuthValidator` | **自定义鉴权器**：实现 `AuthValidator` 接口，通过比对数据库 `logout_time` 校验 Token 是否已被撤回。 |
| `PasswordUtils` | **加密工具**：实现 SHA-256 带盐哈希，确保密码在数据库中不可逆。 |
| `TablePrinter` | **可视化工具**：在控制台以格式化表格形式输出数据库原始数据，辅助调试。 |

---

## 5. 系统异常体系 (Exceptions)

系统通过自定义异常树实现了精确的业务错误反馈。

| 异常类名 | HTTP 状态码 | 触发场景 |
| :--- | :--- | :--- |
| `BadRequestException` | 400 | 参数缺失、类型转换失败或业务校验不通过。 |
| `UnauthorizedException` | 401 | 令牌缺失、签名错误、登录过期或 Token 被撤销。 |
| `ForbiddenException` | 403 | 人机验证未通过。 |
| `NotFoundException` | 404 | 请求路径不存在或查询的资源不存在。 |
| `ConflictException` | 409 | 用户名冲突。 |
| `InternalErrorException` | 500 | 磁盘写入失败、数据库连接异常或服务器内部故障。 |

---

## 6. 技术特色亮点

1.  **JWT 强撤销机制**：通过在 `users` 表引入 `logout_time` 字段，并在每次鉴权时通过 `AppAuthValidator` 校验 `iat` 时间戳，解决了 JWT 默认无法在服务端主动失效的难题。
2.  **动态标签处理**：在数据库端使用逗号分隔字符串存储标签以节省空间，在应用层利用 Java Stream API 实时进行扁平化、去重和清洗，兼顾了存储效率与逻辑灵活性。
3.  **零依赖注入**：完全基于反射实现的 `@Inject` 注解，在系统启动阶段自动完成 Service 层与 Repository 层的链路组装，无需外部 IOC 容器。
4.  **人机二次校验**：在注册和登录关键节点强制集成 Cloudflare Turnstile 服务端校验，有效防御自动化脚本攻击。