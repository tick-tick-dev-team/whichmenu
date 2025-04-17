<script setup>
import { ref, computed } from 'vue';
import { useDisplay } from 'vuetify';

const { smAndDown } = useDisplay(); // 작은 화면(모바일 + 태블릿)
import NavMenu from '@/components/NavMenu.vue';
import NavMenu2 from '@/components/NavMenu2.vue';

const selectedCenter = ref('메디벤처센터');
const centerList = ['메디벤처센터', '니가찾는그밥집'];

const imageUrl = '/path/to/menu-image.jpg'; // 식단 이미지 경로
const period = '2025.02.03 ~ 2025.02.07';
const updated = '2025.01.17 16:03:33';

// 선택된 센터에 따라 이미지 경로 변경
const menuImage = computed(() => {
  return selectedCenter.value === '니가찾는그밥집'
    ? '/img/test_img5.jpg'
    : '/img/test_img.jpg';
});
</script>

<template>
    <!-- 헤더 -->
    <NavMenu2></NavMenu2>
    
    <!-- 메인 -->
    <v-main fluid class="main-wrapper">
        <div class="full-bleed-background">
            <div class="content-wrapper">
                <!-- selectbox -->
                <div class="select-wrapper">
                    <v-select
                        v-model="selectedCenter"
                        :items="centerList"
                        dense
                        outlined
                        hide-details
                        style="max-width: 200px"
                    ></v-select>
                </div>

                <!-- 이미지 -->
                <div class="image-wrapper">
                    <img :src="menuImage" alt="식단 이미지" class="menu-image" />
                </div>
                <v-container class="text-center">
                    <v-text class="period-text font-weight-bold">
                        해당기간 : {{ period }}
                    </v-text>
                    <v-text class="period-text">
                        최근 업데이트 : {{ updated }}
                    </v-text>
                </v-container>
                <!-- 버튼 목록 -->
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" >업로드 하기</v-btn>
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" >식당정보 조회</v-btn>
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" >삭제(관리자)</v-btn>
            </div>
        </div>
    </v-main>
</template>


<style scoped>
.main-wrapper {
    padding: 0;
    margin: 0;
}
/* 전체 배경 */
.full-bleed-background {
    /* background-color:lightgray; */
    width: 100%;
    height: 100%;
    padding-top: 44px; /* 헤더 피하기 */
    overflow: hidden; /* 넘침 방지 */
    box-sizing: border-box;
}

/* 수직 정렬 컨테이너 */
.content-wrapper {
    display: flex;
    flex-direction: column;
    height: 100%;
    box-sizing: border-box;
}

/* select는 위쪽 오른쪽 정렬 */
.select-wrapper {
    display: flex;
    justify-content: flex-end;
    height: 10px;
    flex-shrink: 0;
}

/* 이미지 중앙 정렬 */
.image-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 60px; /* selectbox랑 간격 */
}

.menu-image {
    max-width: 100%;
    height: auto; /* 이미지 원본 비율 유지 */
    object-fit: contain;
}

.period-text {
    display: block;
    text-align: center;
    font-size: 16px;
    font-weight: 500;
    color: #333;
}

.func-btns {
    font-size: 16px;
    height: 50px;
    max-height: 50px;
    line-height: 50px;
    white-space: nowrap;
}
</style>