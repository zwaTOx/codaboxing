import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import profile from '@/views/profile.vue'
import mainPage from '@/views/mainPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/main',
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
      path: '/me',
      name: 'me',
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
      path: '/game/:id',
      name: ' ',
      component: () => import('@/views/game.vue'),
      meta: {
        requiresAuth: true,
        title: 'Дуэль'
      },
      props: true,
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
  const store = useAuthStore();
  const isAuthenticated = store.isAuth

  console.log('Роутер', store.isAuth)
  if (to.meta.title) {
    document.title = `${to.meta.title} | CodeGonks`
  }

  if (to.meta.requiresAuth && !isAuthenticated) {
    console.log('Redirecting to login - not authenticated')
    next({ 
      name: 'login',
      query: { redirect: to.fullPath } 
    })
    return
  }
  
  if ((to.name === 'login' || to.name === 'register') && isAuthenticated) {
    console.log('Redirecting to main - already authenticated')
    next({ name: 'main' })
    return
  }
  next();
})

router.afterEach((to, from) => {
  const store = useAuthStore();
  const isAuthenticated = store.isAuth;

  console.log(to.meta, isAuthenticated)
  console.log(`Переход с ${from.fullPath} на ${to.fullPath}`)
})

export default router