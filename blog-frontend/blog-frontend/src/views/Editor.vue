<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Promotion } from '@element-plus/icons-vue'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const router = useRouter()
const route = useRoute()
const form = ref({ title: '', content: '' }) 
const currentUser = ref({})
const isEditMode = ref(false)

// 🌓 控制编辑器本身的主题 (light/dark)
// 简单的做法是：如果有 html.dark 类，就认为是 dark
const editorTheme = ref('light')

onMounted(() => {
  // 1. 检查暗黑模式 (用于让编辑器内部变黑)
  if (document.documentElement.classList.contains('dark')) {
    editorTheme.value = 'dark'
  }

  // 2. 检查登录状态
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  } else {
    ElMessage.warning('请先登录后再写文章')
    router.push('/login') 
    return
  }

  // 3. 检查是否有 ID 参数
  const articleId = route.query.id
  if (articleId) {
    isEditMode.value = true
    loadArticle(articleId)
  }
})

// 📸 新增：处理图片上传
const onUploadImg = async (files, callback) => {
  const res = await Promise.all(
    files.map((file) => {
      return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('file', file);
        
        axios.post('http://localhost:8080/file/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
        .then((res) => resolve(res))
        .catch((err) => reject(err));
      });
    })
  );

  // 提取后端返回的 URL (假设后端返回结构是 res.data.data = "http://...")
  // callback 是 md-editor 提供的回调，传入 url 数组即可自动插入 markdown
  callback(
    res.map((item) => {
      if(item.data.code === 200) {
        return item.data.data 
      } else {
        ElMessage.error(item.data.msg || '上传失败')
        return '上传失败'
      }
    })
  );
};

// 加载文章详情
const loadArticle = (id) => {
  axios.get('http://localhost:8080/article/detail/' + id)
    .then(res => {
      if(res.data.code === 200 && res.data.data) {
        form.value.title = res.data.data.title
        form.value.content = res.data.data.content
        form.value.id = res.data.data.id 
      } else {
        ElMessage.error('无法加载文章内容')
      }
    })
    .catch(err => {
      console.error(err)
      ElMessage.error('加载失败')
    })
}

// 提交
const submitArticle = () => {
  if(!form.value.title || !form.value.content) { 
    ElMessage.warning('标题和内容不能为空')
    return 
  }
  
  const articleData = { 
    ...form.value, 
    userId: currentUser.value.id 
  }
  
  if (isEditMode.value) {
    // === 修改 ===
    axios.post('http://localhost:8080/article/update', articleData).then(res => {
      if(res.data.code === 200) {
        ElMessage.success('修改成功！')
        router.push('/') 
      } else {
        ElMessage.error(res.data.msg || '修改失败')
      }
    })
  } else {
    // === 新增 ===
    axios.post('http://localhost:8080/article/add', articleData).then(res => {
      if(res.data.code === 200) {
        ElMessage.success('发布成功！')
        router.push('/') 
      } else {
        ElMessage.error(res.data.msg || '发布失败')
      }
    })
  }
}
</script>

<template>
  <div class="editor-container">
    <header class="editor-header">
      <div class="left">
        <el-button link @click="router.push('/')">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </el-button>
        <input 
          v-model="form.title" 
          class="title-input" 
          placeholder="请输入文章标题..." 
        />
      </div>
      <div class="right">
        <el-button type="primary" round @click="submitArticle">
          {{ isEditMode ? '确认修改' : '发布文章' }} 
          <el-icon class="el-icon--right"><Promotion /></el-icon>
        </el-button>
      </div>
    </header>

    <div class="editor-main">
      <MdEditor 
        v-model="form.content" 
        class="my-editor"
        :theme="editorTheme" 
        placeholder="开始挥洒你的代码..." 
        @onUploadImg="onUploadImg"
      />
    </div>
  </div>
</template>

<style scoped>
/* 🎨 适配暗黑模式：背景色和文字颜色使用变量 */
.editor-container { 
  height: 100vh; 
  display: flex; 
  flex-direction: column; 
  background: var(--bg-color); /* 👈 关键：跟随全局背景 */
  transition: background-color 0.3s;
}

.editor-header {
  height: 60px; 
  border-bottom: 1px solid var(--el-border-color-light); /* 👈 使用 Element 边框变量 */
  display: flex; 
  align-items: center; 
  justify-content: space-between; 
  padding: 0 20px;
  background: var(--card-bg); /* 👈 头部背景 */
}

.left { 
  display: flex; 
  align-items: center; 
  gap: 20px; 
  flex: 1; 
}

.title-input { 
  border: none; 
  outline: none; 
  font-size: 20px; 
  font-weight: bold; 
  width: 100%; 
  /* 👇 适配暗黑模式输入框 */
  background: transparent;
  color: var(--text-color); 
}
.title-input::placeholder { color: #999; }

.editor-main { 
  flex: 1; 
  overflow: hidden; 
}

.my-editor {
  height: 100%;
}
</style>