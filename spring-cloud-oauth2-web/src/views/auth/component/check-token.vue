<template>
  <div class='check-token-container'>
    <div class="button-header">
      <el-button type="success" class="request-button" plain @click="checkToken">校验token</el-button>
    </div>
    <div v-if="responseObj" class="response-content">
      <el-text type="success">响应结果：</el-text>
      <!-- https://github.com/chenfengjw163/vue-json-viewer/blob/master/README_CN.md -->
      <json-viewer
        :value="responseObj"
        :expand-depth=5
        copyable
        boxed
        sort>
      </json-viewer>
    </div>
  </div>
</template>

<script setup lang="ts">
import {JsonViewer} from 'vue3-json-viewer';
import 'vue3-json-viewer/dist/index.css';
// 响应结果对象，注意json-viewer渲染的是一个对象，而不是json字符串
import {ref} from 'vue';
import axios from 'axios';

const props = defineProps({
  // 验证码
  token: {
    type: String,
    required: true
  }
});

const responseObj = ref();

const checkToken = () => {
  axios.get('/api/auth/oauth/check_token?token=' + props.token).then(res => {
    responseObj.value = res.data;
  });
};
</script>

<style lang="scss" scoped>
.check-token-container {

  margin-top: 20px;

  .button-header {
    display: flex;
    justify-content: flex-end;
    margin-right: 106px;
  }
}
</style>