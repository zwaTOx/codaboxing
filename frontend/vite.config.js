import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    proxy: {
      '/api': {
        target: `http://82.202.138.90:8080/api`,
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, ''),
        cookieDomainRewrite: {
          "localhost": "82.202.138.90"
        },
        configure: (proxy, _) => {
          proxy.on('proxyReq', (proxyReq, req, _) => {
            console.log('Proxying request:', req.url);
          });
        }
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
