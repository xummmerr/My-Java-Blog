<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Clock, User, View } from '@element-plus/icons-vue'
// 引入预览组件和样式
import { MdPreview, MdCatalog } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const route = useRoute()
const router = useRouter()
const article = ref({})
const id = 'preview-only' // 预览组件的唯一ID，用于目录定位

onMounted(() => {
  const articleId = route.params.id
  loadArticle(articleId)
})

const loadArticle = (articleId) => {
  axios.get('http://localhost:8080/article/detail/' + articleId).then(res => {
    if (res.data.code === 200) {
      article.value = res.data.data
    } else {
      ElMessage.error('文章不存在或已被删除')
    }
  })
}

const formatTime = (timeStr) => {
  return timeStr ? timeStr.replace('T', ' ').substring(0, 16) : ''
}
</script>

<template>
  <div class="detail-container">
    <div class="nav-bar">
      <el-button link @click="router.push('/')">
        <el-icon><ArrowLeft /></el-icon> 返回首页
      </el-button>
    </div>

    <div class="content-layout">
      <div class="article-main">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta-info">
          <span class="meta-item"><el-icon><User /></el-icon> 作者ID: {{ article.userId }}</span>
          <span class="meta-item"><el-icon><Clock /></el-icon> {{ formatTime(article.createTime) }}</span>
          <span class="meta-item"><el-icon><View /></el-icon> 阅读 99+</span>
        </div>

        <el-divider />

        <MdPreview 
          :editorId="id" 
          :modelValue="article.content" 
        />
      </div>

      <div class="article-catalog">
        <div class="catalog-card">
          <div class="catalog-title">目录</div>
          <MdCatalog :editorId="id" :scrollElement="scrollElement" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.nav-bar { margin-bottom: 20px; }

/* 布局：左文右目录 */
.content-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start; /* 顶部对齐 */
}

.article-main {
  flex: 1; /* 占据剩余空间 */
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  min-height: 500px;
}

.title { font-size: 2.5em; margin-bottom: 20px; color: #333; }

.meta-info {
  color: #999;
  font-size: 14px;
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}
.meta-item { display: flex; align-items: center; gap: 5px; }

/* 右侧目录样式 */
.article-catalog {
  width: 250px;
  position: sticky; /* 粘性定位，滚动时固定 */
  top: 20px;
}

.catalog-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.catalog-title {
  font-weight: bold;
  margin-bottom: 10px;
  padding-left: 10px;
  border-left: 4px solid #409EFF;
}
</style>