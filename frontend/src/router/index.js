import { createRouter, createWebHistory } from 'vue-router'
import profile from '@/views/profile.vue'
import mainPage from '@/views/mainPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/main',
      component: mainPage
    },
    {
      path: '/profile',
      name: 'profile',
      component: profile,
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/loginPage.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/registerPage.vue'),
    }
  ],
})

export default router
