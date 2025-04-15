import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'node:path'; // path 모듈 추가
import vuetify from 'vite-plugin-vuetify'; // npm install vite-plugin-vuetify

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(),
    vuetify({ autoImport: true }),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // '@'를 'src' 경로로 매핑
    }
  },
  server: { // CORS 설정
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
