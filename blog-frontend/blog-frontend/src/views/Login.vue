<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router' // 引入路由钩子
import { ElMessage } from 'element-plus'
import { User, Lock, Edit, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter() // 获取路由器
const isRegisterMode = ref(false)
const authForm = ref({ username: '', password: '', nickname: '' })

const handleLogin = () => {
  if(!authForm.value.username || !authForm.value.password) { ElMessage.warning('请输入账号和密码'); return }
  axios.post('http://localhost:8080/user/login', authForm.value).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('欢迎回来，' + res.data.data.nickname)
      // ✨ 核心变化：登录成功后，存用户信息，跳转到首页
      localStorage.setItem('user', JSON.stringify(res.data.data))
      router.push('/') 
    } else { ElMessage.error(res.data.msg || '登录失败') }
  })
}

const handleRegister = () => {
  axios.post('http://localhost:8080/user/register', authForm.value).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('注册成功！请登录')
      isRegisterMode.value = false
    } else { ElMessage.error(res.data.msg || '注册失败') }
  })
}
</script>

<template>
  <div class="login-bg">
    <div class="login-card-center animate-in">
      <div class="login-header">
        <div class="logo-icon">⚡</div>
        <h2>JavaBlog</h2>
        <p class="subtitle">{{ isRegisterMode ? '加入极客社区' : '探索代码世界' }}</p>
      </div>
      <el-form size="large" class="dark-form">
        <el-form-item>
          <el-input v-model="authForm.username" placeholder="Username" :prefix-icon="User" class="neon-input"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="authForm.password" type="password" placeholder="Password" :prefix-icon="Lock" show-password class="neon-input"/>
        </el-form-item>
        <el-form-item v-if="isRegisterMode">
          <el-input v-model="authForm.nickname" placeholder="Nickname" :prefix-icon="Edit" class="neon-input"/>
        </el-form-item>
        <div class="btn-area">
          <el-button v-if="!isRegisterMode" type="primary" class="neon-btn" @click="handleLogin" round>登录 <el-icon class="el-icon--right"><ArrowRight /></el-icon></el-button>
          <el-button v-else type="success" class="neon-btn success" @click="handleRegister" round>注册账号</el-button>
        </div>
      </el-form>
      <div class="footer-links">
        <span v-if="!isRegisterMode" @click="isRegisterMode = true">没有账号？<b>立即注册</b></span>
        <span v-else @click="isRegisterMode = false">已有账号？<b>返回登录</b></span>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 登录页专属样式 */
.login-bg {
  position: fixed; top: 0; left: 0; width: 100%; height: 100%;
  background: url('https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?q=80&w=1988&auto=format&fit=crop') no-repeat center center;
  background-size: cover; 
  display: flex; justify-content: center; align-items: center; z-index: 100;
}
.login-bg::before { content: ''; position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.6); z-index: -1; }
.login-card-center {
  width: 380px; padding: 40px; background: rgba(20, 20, 30, 0.75); backdrop-filter: blur(20px);
  border-radius: 20px; border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5); text-align: center; color: white;
}
.animate-in { animation: fadeInUp 0.8s ease-out; }
@keyframes fadeInUp { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }
.logo-icon { font-size: 40px; margin-bottom: 10px; text-shadow: 0 0 15px #6366f1; }
.login-header h2 { margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px; }
.subtitle { color: #9ca3af; margin: 10px 0 30px; font-size: 14px; }
.btn-area { margin-top: 20px; }
.neon-btn {
  width: 100%; height: 45px; font-size: 16px; font-weight: bold; letter-spacing: 1px;
  background: linear-gradient(90deg, #6366f1, #8b5cf6); border: none; box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  transition: transform 0.2s, box-shadow 0.2s;
}
.neon-btn:hover { transform: translateY(-2px); box-shadow: 0 6px 25px rgba(99, 102, 241, 0.6); }
.neon-btn.success { background: linear-gradient(90deg, #10b981, #059669); box-shadow: 0 4px 15px rgba(16, 185, 129, 0.4); }
.footer-links { margin-top: 25px; font-size: 13px; color: #6b7280; }
.footer-links b { color: #e0e7ff; margin-left: 5px; cursor: pointer; }
</style>