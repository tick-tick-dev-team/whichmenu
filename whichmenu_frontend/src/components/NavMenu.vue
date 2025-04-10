<!-- NavMenu.vue -->
<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const isOpen = ref(false);
const router = useRouter();

const navigate = (path) => {
    isOpen.value = false;
    router.push(path);
};
</script>

<template>
    <div class="nav-wrapper">
        <!-- v-menu의 activator 슬롯으로 버튼 감싸기 -->
        <v-menu
            v-model="isOpen"
            offset-y
            transition="scale-transition"
            location="bottom right"
        >
            <template #activator="{ props }">
                <v-btn icon v-bind="props">
                    <v-icon>mdi-menu</v-icon>
                </v-btn>
            </template>

            <!-- 드롭다운 메뉴 -->
            <v-list>
                <v-list-item @click="navigate('/')">
                    <v-list-item-title>식단</v-list-item-title>
                </v-list-item>
                <v-list-item @click="navigate('/bbs/notice')">
                    <v-list-item-title>공지</v-list-item-title>
                </v-list-item>
                <v-list-item @click="navigate('/bbs/rcmnd')">
                    <v-list-item-title>맛집을 알려주세요!</v-list-item-title>
                </v-list-item>
                <v-list-item @click="navigate('/bbs/inquiry')">
                    <v-list-item-title>문의게시판</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
    </div>
</template>


<style scoped>
.nav-wrapper {
    display: flex;
    align-items: center;
}
</style>
