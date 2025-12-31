<script setup>
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
const router = useRouter()
</script>

<template>
  <div class="about-container">
    <div class="nav">
      <el-button link @click="router.push('/home')"><el-icon><ArrowLeft /></el-icon> 返回首页</el-button>
    </div>
    <div class="content">
     
    <h2>关于博主</h2>
    <p>热爱咖啡与 Bug 的全栈开发者</p>
  </div>
  <el-divider>技能树</el-divider>
  <div class="skill-tags">
    <el-tag effect="dark" color="#2c3e50">Java</el-tag>
    <el-tag effect="dark" color="#42b983">Vue 3</el-tag>
    <el-tag effect="dark" color="#e6a23c">MySQL</el-tag>
    <el-tag effect="dark" color="#f56c6c">Spring Boot</el-tag>
  </div>
  <div class="bio">
    <p>这里是我的数字花园。我在这里记录技术笔记、生活感悟以及那些通宵 debug 的夜晚。</p>
    <p>Email: hxin0405@foxmail.com </p>
  </div>
</div>

</template>

<style scoped>
.about-container { padding: 40px; text-align: center; color: #666; }
.nav { text-align: left; margin-bottom: 40px; }
h1 { font-size: 3rem; margin-bottom: 20px; }
</style>