import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import vuetify from './plugins/vuetify'; // Vuetify 설정 파일 import
import '@mdi/font/css/materialdesignicons.css'; // ← 아이콘 설정 시 이거 꼭 있어야 함!


const app = createApp(App); // Vue 앱 인스턴스를 먼저 생성
app.use(router); // Vue 애플리케이션에 Router를 등록
app.use(vuetify); // Vuetify 사용 등록
app.mount('#app')
