import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '@/stores/auth'
import profile from '@/views/profile.vue'
import mainPage from '@/views/mainPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/main',
      name: 'main',
      component: mainPage,
      meta: {
        requiresAuth: true,
        title: 'Главная страница'
      }
    },
    {
      path: '/profile',
      name: 'profile',
      component: profile,
      meta: {
        requiresAuth: true,
        title: 'Ваш профиль'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/loginPage.vue'),
      meta: {
        requiresAuth: false,
        title: 'Войти'
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/registerPage.vue'),
      meta: {
        requiresAuth: false,
        title: 'Зарегистрироваться'
      }
    },
    {
      path: '/find_game',
      name: 'find',
      component: () => import('@/views/findGame.vue'),
      meta: {
        requiresAuth: true,
        title: 'Поиск игры...'
      }
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition 
    } else {
      return { top: 0 } 
    }
  }
})

router.beforeEach((to, from, next) => {
  const store = authStore()
  
  const isAuthenticated = store.isAuth
  console.log('Роутер',store.isAuth)
  if (to.meta.title) {
    document.title = `${to.meta.title} | CodeGonks`
  }
  
  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      console.log('Неавторизованный доступ к защищенному маршруту:', isAuthenticated)
      next({ 
        name: 'login',
        query: { redirect: to.fullPath } 
      })
    } else {
      next()
    }
  } 
  else if (to.name === 'Login' && isAuthenticated) {
    next({ name: 'main' })
  } 
  else {
    next()
  }
})

router.afterEach((to, from) => {
  console.log(`Переход с ${from.fullPath} на ${to.fullPath}`)
})

export default router