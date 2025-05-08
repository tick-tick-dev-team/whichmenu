import { createRouter, createWebHashHistory } from 'vue-router';
import MlMain     from '@/views/ml/MlMain.vue';
import RcmndBoard from '@/views/bbs/RcmndBoard.vue';
import InqBoard   from '@/views/bbs/InqBoard.vue';
import NtcBoard   from '@/views/bbs/NtcBoard.vue';
import RestInfo   from '@/views/rest/RestInfo.vue';

const routes = [
  { path: '/'             , redirect: '/ml/mlMain' },
  { path: '/ml/mlMain'    , name: 'MlMain'    , component: MlMain }, // 메뉴 공유 화면(메인)
  { path: '/bbs/rcmnd'    , name: 'RcmndBoard', component: RcmndBoard }, // 메뉴 추천 게시판
  { path: '/bbs/inquiry'  , name: 'InqBoard'  , component: InqBoard }, // 문의게시판
  { path: '/bbs/notice'   , name: 'NtcBoard'  , component: NtcBoard }, // 공지게시판
  { path: '/rest/restInfo', name: 'RestInfo'  , component: RestInfo } // 식당관리
];

const router = createRouter({
  //history: createWebHistory(), // 운영의 정석 /path 형식
  history: createWebHashHistory(), // 개발에서는 이렇게 /#/path 형식
  routes
});

export default router;
