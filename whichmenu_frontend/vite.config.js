import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'node:path'; // path 모듈 추가

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // '@'를 'src' 경로로 매핑
    }
  }
})
