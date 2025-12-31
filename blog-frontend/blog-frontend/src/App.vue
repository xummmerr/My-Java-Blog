<script setup>
import { RouterView } from 'vue-router'
import ThemeSwitch from '@/components/ThemeSwitch.vue'
</script>

<template>
  <ThemeSwitch />
  <router-view />
</template>

<style>
/* 1. 全局样式重置 */
* { 
  box-sizing: border-box; 
}

/* 2. 👇👇👇 核心修改：定义 CSS 变量 (调色板) 👇👇👇 */
:root {
  /* === 白天模式 (Light) === */
  --bg-color: #f4f4f4;       /* 整体背景灰白 */
  --text-color: #333333;     /* 文字深灰 */
  --card-bg: #ffffff;        /* 卡片纯白 */
  
  /* 🌟 新增：Banner 渐变背景 (清新蓝) */
  --banner-bg: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  /* 🌟 新增：Banner 文字颜色 (深色) */
  --banner-text: #2c3e50;
}

html.dark {
  /* === 黑夜模式 (Dark) === */
  --bg-color: #121212;       /* 整体背景深黑 */
  --text-color: #e5e5e5;     /* 文字灰白 */
  --card-bg: #1e1e1e;        /* 卡片深灰 */
  
  /* 🌟 新增：Banner 渐变背景 (深邃蓝黑) */
  --banner-bg: linear-gradient(to right, #0f2027, #203a43, #2c5364);
  /* 🌟 新增：Banner 文字颜色 (纯白) */
  --banner-text: #ffffff;
}
/* 👆👆👆 变量定义结束 👆👆👆 */


/* 3. 应用变量到 Body */
body { 
  margin: 0; 
  padding: 0; 
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  overflow-x: hidden; 

  /* 让 Body 背景和文字跟随变量 */
  background-color: var(--bg-color);
  color: var(--text-color);
  
  /* 丝滑过渡动画 */
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* 4. 修复 Element Plus 输入框在暗黑模式下的显示问题 (可选) */
.neon-input .el-input__wrapper {
  background-color: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  color: white !important;
}

/* 5. 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
::-webkit-scrollbar-track {
  background: var(--bg-color); 
}
::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style>