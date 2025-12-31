<script setup>
import { ref, onMounted, watch } from 'vue'
import { Moon, Sunny, Monitor } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 当前模式：auto | light | dark
const currentMode = ref(localStorage.getItem('theme') || 'auto')

// 系统是否是深色
const systemDark = window.matchMedia('(prefers-color-scheme: dark)')

// 核心逻辑：设置主题
const applyTheme = (mode) => {
  const html = document.documentElement
  
  if (mode === 'dark') {
    html.classList.add('dark')
  } else if (mode === 'light') {
    html.classList.remove('dark')
  } else {
    // === auto 跟随系统 ===
    if (systemDark.matches) {
      html.classList.add('dark')
    } else {
      html.classList.remove('dark')
    }
  }
}

// 监听模式变化
watch(currentMode, (newVal) => {
  localStorage.setItem('theme', newVal) // 存入缓存
  applyTheme(newVal) // 应用样式
  
  const text = newVal === 'auto' ? '跟随系统' : (newVal === 'dark' ? '暗黑模式' : '亮色模式')
  ElMessage.success(`已切换至：${text}`)
})

// 监听系统变化（仅在 auto 模式下生效）
systemDark.addEventListener('change', (e) => {
  if (currentMode.value === 'auto') {
    if (e.matches) document.documentElement.classList.add('dark')
    else document.documentElement.classList.remove('dark')
  }
})

// 初始化执行一次
onMounted(() => {
  applyTheme(currentMode.value)
})

// 切换菜单指令
const handleCommand = (command) => {
  currentMode.value = command
}
</script>

<template>
  <div class="theme-switcher">
    <el-dropdown trigger="click" @command="handleCommand" placement="top-start">
      <el-button circle size="large" type="info" class="switch-btn">
        <el-icon v-if="currentMode === 'light'" :size="20"><Sunny /></el-icon>
        <el-icon v-else-if="currentMode === 'dark'" :size="20"><Moon /></el-icon>
        <el-icon v-else :size="20"><Monitor /></el-icon>
      </el-button>
      
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="light">
            <el-icon><Sunny /></el-icon> 亮色模式
          </el-dropdown-item>
          <el-dropdown-item command="dark">
            <el-icon><Moon /></el-icon> 暗黑模式
          </el-dropdown-item>
          <el-dropdown-item command="auto" divided>
            <el-icon><Monitor /></el-icon> 跟随系统
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped>
.theme-switcher {
  position: fixed;
  bottom: 30px;
  left: 30px;
  z-index: 9999; /* 保证在最上层 */
}

/* 按钮加点阴影，更立体 */
.switch-btn {
  box-shadow: 0 4px 10px rgba(0,0,0,0.2);
  width: 50px;
  height: 50px;
}
</style>