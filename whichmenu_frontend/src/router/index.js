import { createRouter, createWebHistory } from 'vue-router';
import RcmndBoard from '@/views/bbs/RcmndBoard.vue';
import InqBoard from '@/views/bbs/InqBoard.vue';
import NtcBoard from '@/views/bbs/NtcBoard.vue';

const routes = [
  { path: '/', redirect: '/bbs/rcmnd' },
  { path: '/bbs/rcmnd', name: 'RcmndBoard', component: RcmndBoard },
  { path: '/bbs/inquiry', name: 'InqBoard', component: InqBoard },
  { path: '/bbs/notice', name: 'NtcBoard', component: NtcBoard }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
