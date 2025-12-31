# 📝 My Java Blog (个人博客系统)

> 一个基于 Spring Boot + Vue 3 的前后端分离个人博客系统。
> 简洁、美观、功能强大，支持 Markdown 编辑、暗黑模式切换、图片上传等功能。

![Java](https://img.shields.io/badge/Backend-Java%2017%2B-red)
![SpringBoot](https://img.shields.io/badge/Framework-SpringBoot%203.x-green)
![Vue3](https://img.shields.io/badge/Frontend-Vue%203-42b883)
![ElementPlus](https://img.shields.io/badge/UI-Element%20Plus-409eff)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)

## 📸 项目预览 (Screenshots)

> *建议在这里放几张运行截图，让项目更吸引人*

| 首页 (暗黑模式) | 文章详情页 |
| --- | --- |
| ![首页截图](https://via.placeholder.com/400x200?text=Home+Page) | ![详情页截图](https://via.placeholder.com/400x200?text=Detail+Page) |

| 编辑器 (Markdown) | 登录页面 |
| --- | --- |
| ![编辑器截图](https://via.placeholder.com/400x200?text=Editor) | ![登录页截图](https://via.placeholder.com/400x200?text=Login) |

## ✨ 核心功能 (Features)

- **用户系统**：登录、注册、JWT 认证（或 Session 管理）。
- **文章管理**：
  - 📝 Markdown 在线编辑（支持实时预览）。
  - 📸 **图片上传**：支持拖拽/粘贴上传图片。
  - 🗑️ 增删改查：完整的 CRUD 流程。
- **界面交互**：
  - 🌓 **暗黑模式**：支持白天/黑夜/跟随系统三种模式，丝滑切换。
  - 📱 响应式布局：适配 Element Plus UI。
- **数据统计**：首页侧边栏实时展示文章数、标签云、访问量等。
- **安全机制**：
  - 后端统一响应封装。
  - 前端路由守卫（未登录无法进入编辑器）。

## 🛠️ 技术栈 (Tech Stack)

### 后端 (Backend)
- **核心框架**: Spring Boot 3
- **ORM 框架**: MyBatis / MyBatis-Plus
- **数据库**: MySQL 8.0
- **工具库**: Hutool, Lombok

### 前端 (Frontend)
- **核心框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **路由/状态**: Vue Router 4
- **UI 组件库**: Element Plus
- **编辑器**: md-editor-v3
- **网络请求**: Axios

## 🚀 快速开始 (Quick Start)

### 1. 环境准备
- JDK 17+
- Node.js 16+
- MySQL 8.0

### 2. 数据库设置
创建数据库 `blog_db`，并导入项目根目录下的 `sql/init.sql` (如果有的话) 或执行以下 SQL：

```sql
-- 示例建表语句
CREATE TABLE article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    user_id BIGINT,
    create_time DATETIME,
    tags VARCHAR(255),
    view_count INT DEFAULT 0
);
-- (请补充完整的建表语句)
