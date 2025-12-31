import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import Editor from '../views/Editor.vue'
import About from '../views/About.vue'

const routes = [
    // 👇 1. 核心修改：把根路径 / 给 Home (首页)
    { 
        path: '/', 
        name: 'Home', 
        component: Home 
    },
    
    // 👇 2. 把 Login 挪到 /login
    { 
        path: '/login', 
        name: 'Login', 
        component: Login 
    },
    
    // 其他保持不变
    { path: '/article/:id', name: 'ArticleDetail', component: ArticleDetail },
    { path: '/editor', name: 'Editor', component: Editor },
    { path: '/about', name: 'About', component: About },
    { path: '/', name: 'Home', component: Home }, 
    { path: '/home', redirect: '/' }, 
    { path: '/login', name: 'Login', component: Login }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})



export default router