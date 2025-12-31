<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  Edit, Delete, SwitchButton, Monitor, User, 
  Folder, CollectionTag, Search 
} from '@element-plus/icons-vue'

const router = useRouter()
const currentUser = ref({})
const articleList = ref([])
const searchKeyword = ref('') 
const currentPage = ref(1)
const pageSize = ref(5) 
const total = ref(0)    

// 📊 新增：用于存储侧边栏统计数据
const siteStats = ref({
  articleCount: 0,
  viewCount: 0,
  tags: []
})

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  }

  getArticleList()
  getSidebarStats() // 👈 新增：加载侧边栏数据
})

// 获取摘要
const getSummary = (content) => {
  if (!content) return '暂无内容';
  const text = content.replace(/[#*`>]/g, '').trim();
  return text.length > 60 ? text.substring(0, 60) + '...' : text;
}

const formatTime = (timeStr) => { 
  if (!timeStr) return ''; 
  return timeStr.replace('T', ' ').substring(0, 16); 
}

const getArticleList = () => {
  axios.get('http://localhost:8080/article/search', {
    params: { 
      keyword: searchKeyword.value,
      pageNum: currentPage.value,  
      pageSize: pageSize.value     
    }
  }).then(res => {
    if (res.data.code === 200) {
      articleList.value = res.data.data.records 
      total.value = res.data.data.total
    }
  })
}

// 📊 新增：获取侧边栏统计信息的接口调用
const getSidebarStats = () => {
  axios.get('http://localhost:8080/article/sidebar').then(res => {
    if (res.data.code === 200) {
      siteStats.value = res.data.data
    }
  })
}

const goToDetail = (id) => router.push('/article/' + id)
const goToEditor = () => router.push('/editor') 
const goToAbout = () => router.push('/about')

const handleEdit = (article) => {
  router.push({ path: '/editor', query: { id: article.id } })
}

const handleDelete = (id) => {
  axios.delete('http://localhost:8080/article/delete/' + id).then(() => {
    ElMessage.success('删除成功')
    getArticleList() 
    getSidebarStats() // 删除后记得刷新统计数据
  })
}

const handleLogout = () => { 
  localStorage.removeItem('user'); 
  router.push('/login') // 登出建议去登录页，或者 '/'
}

const handlePageChange = (newPage) => {
  currentPage.value = newPage
  getArticleList()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleSearch = () => {
  currentPage.value = 1 
  getArticleList()
}
</script>

<template>
  <div class="blog-layout">
    <header class="navbar">
      <div class="nav-inner">
        <div class="logo"><el-icon><Monitor /></el-icon> <span>欢迎来到Xummmerr-Blog</span></div>
        <div class="nav-links">
            <div class="search-box">
                <el-input
                    v-model="searchKeyword"
                    placeholder="搜索文章..."
                    :prefix-icon="Search"
                    @keyup.enter="handleSearch"
                    clearable
                    @clear="handleSearch"
                    class="nav-search"
                />
                </div>
          <span class="nav-item active">首页</span>
          <template v-if="currentUser.id">
                <span class="nav-item" @click="goToAbout">关于我</span>
                <el-button type="primary" round :icon="Edit" @click="goToEditor">写文章</el-button>
                <el-button type="danger" circle plain :icon="SwitchButton" @click="handleLogout" style="margin-left: 15px;" />
          </template>
          <template v-else>
                <el-button type="primary" link @click="router.push('/login')">登录 / 注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <div class="hero-banner">
      <div class="hero-content">
        <h1>Talk is cheap.</h1>
        <p>Show me the code.</p>
      </div>
    </div>

    <div class="main-container">  
      <div class="left-column">
        
        <div v-if="articleList.length > 0">
          <div v-for="article in articleList" :key="article.id" class="article-card" @click="goToDetail(article.id)">
            <div class="card-meta">
              <span class="tag">技术分享</span>
              <span class="time">{{ formatTime(article.createTime) }}</span>
            </div>
            <h2 class="card-title">{{ article.title }}</h2>
            
            <p class="card-desc">{{ getSummary(article.content) }}</p>
            
            <div class="card-footer">
              <span class="author">
                <el-icon><User /></el-icon> {{ article.userId === currentUser.id ? '我' : '用户 '+article.userId }}
              </span>

              <div class="action-buttons" v-if="article.userId === currentUser.id">
                <el-button type="primary" link :icon="Edit" @click.stop="handleEdit(article)">编辑</el-button>
                <el-popconfirm title="确定删除吗？" @confirm="handleDelete(article.id)">
                  <template #reference>
                    <el-button type="danger" link :icon="Delete" @click.stop>删除</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>
          </div>

          <div class="pagination-area">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              v-model:current-page="currentPage"
              @current-change="handlePageChange"
            />
          </div>
        </div>

        <el-empty v-else description="暂无相关文章" />

      </div>

      <div class="right-column">   
        <div class="sidebar-card user-card">
          <div class="avatar-circle">{{ currentUser.nickname ? currentUser.nickname[0].toUpperCase() : 'U' }}</div>
          <h3>{{ currentUser.nickname || '未登录用户' }}</h3>
          <p>Java Fullstack Developer</p>
          
          <div class="stats">
            <div><b>{{ siteStats.articleCount }}</b><br>文章</div>
            <div><b>{{ siteStats.tags.length }}</b><br>标签</div>
            <div><b>{{ siteStats.viewCount }}</b><br>访问</div>
          </div>
        </div>

        <div class="sidebar-card">
          <div class="card-header"><el-icon><CollectionTag /></el-icon> 热门标签</div>
          <div class="tags-cloud">
            <span v-for="tag in siteStats.tags" :key="tag" class="tag-item">
              {{ tag }}
            </span>
            <el-empty v-if="siteStats.tags.length === 0" description="暂无标签" :image-size="60" />
          </div>
        </div>

        <div class="sidebar-card">
           <div class="card-header"><el-icon><Folder /></el-icon> 文章归档</div>
           <ul class="archive-list">
             <li>2025年 12月 ({{ siteStats.articleCount }})</li>
             <li>2025年 11月 (0)</li>
           </ul>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>
/* 🎨 暗黑模式适配重点：
   1. 背景颜色不要写死 white 或 #eee，要用 var(--bg-color), var(--card-bg)
   2. 文字颜色不要写死 black 或 #333，要用 var(--text-color)
*/

.blog-layout { 
  background-color: var(--bg-color); /* 👈 关键：跟随全局背景 */
  min-height: 100vh; 
  padding-top: 60px; 
  transition: background-color 0.3s;
}

.navbar { 
  position: fixed; top: 0; width: 100%; height: 60px; 
  /* 👇 修改这里：使用卡片背景，但加一点点透明度 (0.8) */
  background: rgba(255, 255, 255, 0.8); 
  /* 暗黑模式下，我们需要单独处理背景，下面会用到 CSS 变量技巧，
     但在 Element Plus 暗黑模式下，简单的办法是让它跟随 var(--card-bg) 并加透明度。
     为了简单起见，我们直接用 var(--card-bg) 配合 opacity */
  background-color: var(--card-bg);
  opacity: 0.95; /* 稍微透一点点 */
  
  backdrop-filter: blur(10px); /* 👈 加上毛玻璃效果 */
  border-bottom: 1px solid var(--el-border-color-light); 
  z-index: 99; 
  transition: all 0.3s;
}

.nav-inner { max-width: 1100px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center; height: 100%; padding: 0 20px; }
.logo { font-weight: 800; font-size: 20px; color: var(--text-color); display: flex; align-items: center; gap: 8px; } /* 👈 字体颜色 */
.nav-links { display: flex; align-items: center; gap: 20px; }
.nav-item { cursor: pointer; color: var(--text-color); font-weight: 500; transition: color 0.2s; opacity: 0.8; } /* 👈 字体颜色 */
.nav-item:hover, .nav-item.active { color: #6366f1; opacity: 1; }

.hero-banner { 
  height: 260px; 
  
  /* ❌ 删除原来的死黑色 background: #111827; */
  
  /* ✅ 使用我们刚才定义的变量 */
  background: var(--banner-bg); 
  
  /* ✅ 文字颜色也跟随变量 */
  color: var(--banner-text);
  
  display: flex; 
  align-items: center; 
  justify-content: center; 
  text-align: center; 
  
  /* 👇 加个过渡动画，切换模式时慢慢变色 */
  transition: background 0.5s ease, color 0.3s ease;
}

/* 修改标题的渐变字，让它在任何背景下都显眼 */
.hero-content h1 { 
  font-size: 2.5rem; 
  margin-bottom: 10px; 
  /* 我们可以把文字渐变去掉，直接用纯色，这样在多彩背景上更清晰 */
  /* 或者保留渐变，但颜色要调整。为了稳妥，建议直接用 currentColor */
  color: var(--banner-text); 
  background: none; 
  -webkit-background-clip: unset;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1); /* 加点阴影增加层次感 */
}
/* === 核心：双栏布局 === */
.main-container {
  max-width: 1100px; margin: -40px auto 40px; padding: 0 20px;
  display: flex; gap: 30px; position: relative; z-index: 10;
}

.left-column { flex: 1; }
.right-column { width: 300px; flex-shrink: 0; }

/* 文章卡片样式 */
.article-card {
  background: var(--card-bg); /* 👈 卡片背景变量 */
  border-radius: 12px; padding: 25px; margin-bottom: 20px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05); 
  cursor: pointer; transition: all 0.3s;
  color: var(--text-color); /* 👈 文字颜色变量 */
}
.article-card:hover { transform: translateY(-3px); box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1); }
.card-meta { display: flex; justify-content: space-between; font-size: 12px; color: #999; margin-bottom: 10px; }
.tag { background: #e0e7ff; color: #6366f1; padding: 2px 8px; border-radius: 4px; }
.card-title { margin: 0 0 10px 0; font-size: 1.25rem; color: var(--text-color); } /* 👈 标题颜色 */
.card-desc { color: var(--text-color); font-size: 14px; margin-bottom: 15px; line-height: 1.6; opacity: 0.8; } /* 👈 描述颜色，加点透明度 */
.card-footer { 
  border-top: 1px solid var(--el-border-color-light); /* 👈 边框颜色 */
  padding-top: 15px; 
  color: #9ca3af; font-size: 13px;
  display: flex; justify-content: space-between; align-items: center;
}

.action-buttons { display: flex; gap: 10px; }
.search-box { margin-right: 20px; width: 200px; }
.nav-search .el-input__wrapper { border-radius: 20px; background: #f9fafb; }
.sidebar-card { 
    background: var(--card-bg); /* 👈 卡片背景变量 */
    color: var(--text-color);   /* 👈 文字颜色变量 */
    border-radius: 12px; padding: 20px; margin-bottom: 20px; 
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05); 
}
.card-header { font-weight: bold; margin-bottom: 15px; display: flex; align-items: center; gap: 5px; color: var(--text-color); }

.user-card { text-align: center; }
.avatar-circle { width: 70px; height: 70px; background: linear-gradient(135deg, #6366f1, #8b5cf6); border-radius: 50%; margin: 0 auto 10px; display: flex; align-items: center; justify-content: center; color: white; font-size: 24px; font-weight: bold; }
.user-card h3 { margin: 5px 0; color: var(--text-color); }
.user-card p { font-size: 13px; color: #666; margin-bottom: 15px; }
.stats { display: flex; justify-content: space-around; border-top: 1px solid var(--el-border-color-light); padding-top: 15px; font-size: 14px; }

.tags-cloud { display: flex; flex-wrap: wrap; gap: 8px; }
.tag-item { background: var(--el-fill-color-light); color: var(--text-color); padding: 4px 10px; border-radius: 20px; font-size: 12px; cursor: pointer; transition: 0.2s; }
.tag-item:hover { background: #6366f1; color: white; }

.archive-list { list-style: none; padding: 0; margin: 0; }
.archive-list li { font-size: 14px; color: var(--text-color); opacity: 0.8; padding: 8px 0; border-bottom: 1px solid var(--el-border-color-light); cursor: pointer; }
.archive-list li:hover { color: #6366f1; opacity: 1; }

.pagination-area {
  background: var(--card-bg); 
  padding: 15px; border-radius: 12px; margin-top: 20px;
  display: flex; justify-content: center; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}
</style>