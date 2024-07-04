<script setup lang="ts">
import {onMounted} from 'vue';
import axios from 'axios';

onMounted(() => {
    axios.get('/api/system/actuator/health').then(res => {
        console.log('system:', res);
    });
    axios.get('/api/auth/actuator/health').then(res => {
        console.log('auth:', res);
    });
});
</script>

<template>
    <div>
        <h1>Home</h1>
    </div>
</template>