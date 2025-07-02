<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useDisplay } from 'vuetify';
import axios from 'axios';

const { smAndDown } = useDisplay();

import NavMenu2 from '@/components/NavMenu2.vue';

// 선택된 식당
const selectedCenter = ref('');
// 식당 리스트
const centerList = ref([]);

// 메뉴 정보
const menuImage = ref('');
const period = ref('');
const updated = ref('');

// 식당 리스트 조회
const fetchCenterList = async () => {
    try {
        const response = await axios.get('/api/rest/list', {
            params: {
                useYn: 'Y'
            }
        });
        centerList.value = response.data;
        if (centerList.value.length > 0) {
            selectedCenter.value = centerList.value[0].restId;
        }
    } catch (error) {
        console.error('식당 리스트 조회 실패:', error);
    }
};

// 메뉴 정보 조회
const fetchMenuInfo = async (restId) => {
    try {
        const response = await axios.post('/api/mlmeu/rest', {
            restId: restId
        });

        const menuDto = response.data.MlMenuDto;
        const atchList = response.data.atchList || [];

        if (menuDto) {
            menuImage.value = atchList.length > 0 ? `http://localhost:8080/atch/${atchList[0].filePath.split(/[/\\]/).at(-1)}` : '';
            period.value = menuDto.bgngDt + " ~ " + menuDto.endDt;
            updated.value = menuDto.mdfcnDt;
        } else {
            menuImage.value = '';
            period.value = '';
            updated.value = '';
        }
    } catch (error) {
        console.error('메뉴 정보 조회 실패:', error);
    }
};

// 초기 데이터 조회
onMounted(async () => {
    await fetchCenterList();
    if (selectedCenter.value) {
        await fetchMenuInfo(selectedCenter.value);
    }
});

// 선택 변경 시 재조회
watch(selectedCenter, async (newVal) => {
    if (newVal) {
        await fetchMenuInfo(newVal);
    }
});
</script>
<template>
    <!-- 헤더 -->
    <NavMenu2 />

    <!-- 메인 -->
    <v-main fluid class="main-wrapper">
        <div class="full-bleed-background">
            <div class="content-wrapper">
                <!-- selectbox -->
                <div class="select-wrapper">
                    <v-select
                        v-model="selectedCenter"
                        :items="centerList"
                        item-title="restNm"
                        item-value="restId"
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
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'">업로드 하기</v-btn>
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'" :to="'/rest/restInfo'">식당정보 조회</v-btn>
                <v-btn block class="my-1 func-btns" color="black" dark :density="smAndDown ? 'compact' : 'default'">삭제(관리자)</v-btn>
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
