https://github.com/user-attachments/assets/39f21f40-339d-41dd-9fd4-d4980f7deea0# 📝 My Java Blog (个人博客系统)

> 一个基于 Spring Boot + Vue 3 的前后端分离个人博客系统。
> 简洁、美观、功能强大，支持 Markdown 编辑、暗黑模式切换、图片上传等功能。

![Java](https://img.shields.io/badge/Backend-Java%2017%2B-red)
![SpringBoot](https://img.shields.io/badge/Framework-SpringBoot%203.x-green)
![Vue3](https://img.shields.io/badge/Frontend-Vue%203-42b883)
![ElementPlus](https://img.shields.io/badge/UI-Element%20Plus-409eff)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)

## 📸 项目预览 (Screenshots)


| 首页 (暗黑模式) | 文章详情页 |
| --- | --- |
| ![首页截图](https://github.com/user-attachments/assets/39f21f40-339d-41dd-9fd4-d4980f7deea0) | ![详情页截图](https://github.com/user-attachments/assets/3807f8b3-cd01-41c8-8247-0f9fcb808e1d) |
| 编辑器 (Markdown) | 登录页面 |
| ![编辑器截图](https://github.com/user-attachments/assets/2a89047a-ec4f-4ecb-8a60-074402b44c79) | ![登录页截图](https://github.com/user-attachments/assets/006aa599-aec2-4bf7-b845-79b44e0f6498) |

## ✨ 核心功能 (Features)

- **🔐 用户鉴权**：
  - 登录/注册/注销。
  - 简单的 Token/Session 认证机制。
- **📝 文章管理**：
  - **Markdown 编辑器**：集成 `md-editor-v3`，支持实时预览、代码高亮。
  - **CRUD**：文章的发布、查询、修改、删除。
  - **图片上传**：支持拖拽/粘贴上传图片（本地存储 + 静态资源映射）。
- **🎨 界面交互**：
  - **暗黑模式 (Dark Mode)**：支持 ☀️ 浅色 / 🌙 深色 / 💻 跟随系统，自动持久化配置。
  - **响应式布局**：基于 Element Plus，美观大方。
- **📊 数据统计**：
  - 首页侧边栏实时展示：文章总数、热门标签、全站访问量。
  - 文章阅读量统计（View Count）。

## 🛠️ 技术栈 (Tech Stack)

### 后端 (Backend)
- **核心框架**: Spring Boot 3.x
- **ORM 框架**: MyBatis-Plus
- **数据库**: MySQL 8.0
- **工具库**: Hutool (文件处理/ID生成), Lombok

### 前端 (Frontend)
- **核心框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **路由**: Vue Router 4
- **UI 组件库**: Element Plus (配合暗黑模式)
- **编辑器**: md-editor-v3
- **网络请求**: Axios

## 🚀 快速开始 (Quick Start)

### 1. 环境准备
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库初始化 (Database Setup)
请在 MySQL 中创建一个名为 `blog_db` 的数据库，并执行以下完整的 SQL 语句：

```sql
-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS blog_db CHARACTER SET utf8mb4;
USE blog_db;

-- 2. 用户表 (User)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 初始化一个测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `nickname`) VALUES ('admin', '123456', 'Admin');

-- 3. 文章表 (Article)
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `content` longtext NOT NULL COMMENT '文章内容(Markdown)',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `tags` varchar(255) DEFAULT '' COMMENT '标签(逗号分隔)',
  `view_count` int DEFAULT 0 COMMENT '阅读量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';
```

### 3. 后端配置 (Configuration)

在 `src/main/resources/application.yml` (或 properties) 中配置数据库和文件上传限制：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root  # 你的数据库账号
    password: root  # 你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver

  # 文件上传大小限制 (防止上传大图报错)
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
```

### 4. 启动项目

#### 启动后端
1. 用 IDEA 打开后端项目。
2. 等待 Maven 依赖下载完成。
3. 运行 `BlogApplication.java`。
4. 确保控制台没有报错，且文件存储目录 `files/` 会自动在项目根目录生成。

#### 启动前端
```bash
cd frontend  # 进入前端文件夹
npm install  # 安装依赖
npm run dev  # 启动开发服务器
```
打开浏览器访问 `http://localhost:5173` 即可。

## 📂 目录结构 (Directory Structure)

```text
├── src/main/java/com/example/blog
│   ├── config          # WebConfig (跨域/资源映射)
│   ├── controller      # ArticleController, FileController, UserController
│   ├── entity          # BlogArticle, User
│   ├── mapper          # MyBatis Mapper
│   └── common          # Result, ResultCode (统一返回结果)
├── src/main/resources
│   └── application.yml # 配置文件
├── files/              # 图片上传存储目录 (自动生成)
└── frontend/           # Vue 前端项目
    ├── src/views       # Home, Editor, Login, ArticleDetail
    ├── src/components  # ThemeSwitch (暗黑模式开关)
    └── src/assets      # 静态资源/CSS
```

## 🤝 贡献 (Contribution)

欢迎提交 Issue 和 Pull Request！

---
Designed with ❤️ by xummmerr
