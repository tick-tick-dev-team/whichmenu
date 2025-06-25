<script setup>
import NavMenu2 from '@/components/NavMenu2.vue';
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
//import dayjs from 'dayjs';

//const startDate = ref(dayjs().subtract(7, 'day').format('YYYY-MM-DD'));
//const endDate = ref(dayjs().format('YYYY-MM-DD'));

const startDate = ref(new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString().slice(0, 10));
const endDate = ref(new Date().toISOString().slice(0, 10));

// 예시 데이터 1건 포함
const logList = ref([
    {
        mngrNo: "admin001",
        mngrNm: "홍길동",
        lgnDt: "2025-06-19 14:10:05",
        lgnYn: "Y"
    }
]);

async function fetchLogs() {
    try {
        const res = await axios.post('/api/mngrLog/list', {
            srchBgngDt: startDate.value.replaceAll('-', ''),
            srchEndDt: endDate.value.replaceAll('-', '')
        });
        logList.value = res.data.sort((a, b) => new Date(b.lgnDt) - new Date(a.lgnDt));
    } catch (err) {
        console.error('로그 불러오기 실패:', err);
    }
}

watch([startDate, endDate], fetchLogs);

onMounted(() => {
    fetchLogs();
});
</script>

<template>
    <NavMenu2 />

    <div class="bbs-container">
        <h2 class="page-title">로그확인 - 관리자</h2>

        <!-- 날짜 필터 -->
        <div class="date-range">
            <v-text-field
                v-model="startDate"
                label="시작일"
                type="date"
                variant="outlined"
                density="comfortable"
                class="mr-2"
            />
            <v-text-field
                v-model="endDate"
                label="종료일"
                type="date"
                variant="outlined"
                density="comfortable"
            />
        </div>
        <!-- 로그 리스트 -->
        <v-row dense>
            <v-col v-for="(log, index) in logList" :key="index" cols="12">
                <v-card class="post-card" outlined>
                    <v-card-text>
                        <p class="log-item">
                            <span class="log-label">관리자번호<span class="justify-hack"></span></span>
                            <span>{{ log.mngrNo }}</span>
                        </p>
                        <p class="log-item">
                            <span class="log-label">관리자이름<span class="justify-hack"></span></span>
                            <span>{{ log.mngrNm }}</span>
                        </p>
                        <p class="log-item">
                            <span class="log-label">접 속 일 시<span class="justify-hack"></span></span>
                            <span>{{ log.lgnDt }}</span>
                        </p>
                        <p class="log-item">
                            <span class="log-label">접 속 여 부<span class="justify-hack"></span></span>
                            <v-chip
                                :color="log.lgnYn === 'Y' ? 'green' : 'red'"
                                text-color="white"
                                size="small"
                                label
                            >
                                {{ log.lgnYn === 'Y' ? '성공' : '실패' }}
                            </v-chip>
                        </p>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
      </div>
</template>

<style scoped>
.bbs-container {
    min-height: 100vh;
    padding: 20px;
    margin-top: 30px;
    position: relative;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
}

.page-title {
    font-size: 1.6rem;
    font-weight: 600;
    margin-bottom: 20px;
    text-align: center;
    color: #333;
}

.date-range {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    max-width: 600px;
    justify-content: center;
}

.post-card {
    margin-bottom: 20px;
    padding: 16px;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    max-width: 600px;
}

.log-item {
    font-size: 0.95rem;
    margin-bottom: 6px;
    color: #444;
    text-align: left;
    display: flex;
    align-items: center;
}

.log-label {
    display: inline-block;
    width: 100px;
    font-weight: 600;
    text-align: justify;
    text-justify: inter-character;
}
</style>
