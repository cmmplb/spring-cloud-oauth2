<template>
  <div class='auth-container'>
    <el-card class="card-content">
      <!-- https://element-plus.org/zh-CN/component/tabs.html -->
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane
          v-for="tab in tabs"
          :name="tab.name"
        >
          <template #label>
            <el-tag :type="tab.type">{{ tab.content }}</el-tag>
          </template>
          <template v-if="tab.name === 'client_credentials'">
            <ClientCredentials @request="request"/>
          </template>
          <template v-else-if="tab.name === 'password'">
            <Password @request="request"/>
          </template>
          <template v-else-if="tab.name === 'implicit'">
            <Implicit/>
          </template>
          <template v-else>
            <AuthorizationCode/>
          </template>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <div class="code-button-content">
      <el-button type="warning" class="code-button" plain @click="getAccessToken" v-if="code">
        通过code获取access_token
      </el-button>
    </div>

    <div v-if="responseObj" class="response-content">
      <el-text type="primary">响应结果：</el-text>
      <!-- https://github.com/chenfengjw163/vue-json-viewer/blob/master/README_CN.md -->
      <json-viewer
        :value="responseObj"
        :expand-depth=5
        copyable
        boxed
        sort>
      </json-viewer>
    </div>

    <CheckToken :token="accessToken" v-if="accessToken"/>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import Password from '@/views/auth/component/password.vue';
import Implicit from '@/views/auth/component/implicit.vue';
import CheckToken from '@/views/auth/component/check-token.vue';
import ClientCredentials from '@/views/auth/component/client-credentials.vue';
import AuthorizationCode from '@/views/auth/component/authorization-code.vue';
import {JsonViewer} from 'vue3-json-viewer';
import 'vue3-json-viewer/dist/index.css';
import axios, {AxiosHeaders} from 'axios';
import type {TabsPaneContext} from 'element-plus';
import {ElMessage} from 'element-plus';

// 当前激活的tab页签
const activeName = ref('client_credentials');

// 响应结果对象，注意json-viewer渲染的是一个对象，而不是json字符串
const responseObj = ref();

const accessToken = ref();

const code = ref();

// tab数据
const tabs = ref([
  {
    name: 'client_credentials',
    type: 'primary',
    content: '客户端模式'
  },
  {
    name: 'password',
    type: 'success',
    content: '密码模式'
  },
  {
    name: 'implicit',
    type: 'info',
    content: '简化模式'
  },
  {
    name: 'authorization_code',
    type: 'warning',
    content: '授权码模式'
  }
]);

// 请求头类型声明
interface Headers extends AxiosHeaders {
  'Content-Type': string;
  Authorization: string;
}

const request = (data: any) => {
  const headers = {'Content-Type': 'application/x-www-form-urlencoded'} as Headers;
  if (activeName.value === 'password') {
    // 密码模式要通过Basic认证，所以要在请求头中添加Authorization，值为Basic + 空格 + base64编码{client_id}:{client_secret}
    headers['Authorization'] = 'Basic ' + btoa('web:123456');
  }
  axios.post('/api/auth/oauth/token', data, {headers}).then(res => {
    responseObj.value = res.data;
    accessToken.value = res.data.access_token;
  }).catch(err => {
    // Invalid authorization code: 17TlIP
    if (err.response.data.error_description.includes('Invalid authorization code:')) {
      // element消息提示
      ElMessage.error('code失效，请重新获取code');
    }
  });
};

// 切换tab触发的事件
const handleClick = (tab: TabsPaneContext) => {
  // 切换时清除响应结果
  responseObj.value = undefined;
  accessToken.value = undefined;
  if (tab.paneName === 'implicit') {
    setAccessToken();
  }
};

// 通过code获取access_token
const getAccessToken = () => {
  request({
    client_id: 'web',
    client_secret: '123456',
    code: code.value,
    grant_type: 'authorization_code',
    redirect_uri: 'http://localhost:18080/auth?type=code'
  });
};

// 设置code
const setCode = () => {
  // 获取浏览器参数
  const searchParams = new URLSearchParams(window.location.search);
  code.value = searchParams.get('code');
};

// 设置access_token
const setAccessToken = () => {
  let name, value;
  let str = location.href;
  let num = str.indexOf('#');
  str = str.substring(num + 1);
  const arr = str.split('&');
  const params = {} as any;
  for (let i = 0; i < arr.length; i++) {
    num = arr[i].indexOf('=');
    if (num > 0) {
      name = arr[i].substring(0, num);
      value = arr[i].substring(num + 1);
      params[name] = value;
    }
  }
  accessToken.value = params.access_token;
};

onMounted(() => {
  // http://localhost:18080/auth?type=implicit#access_token=6e0880ea-13bf-4a95-8f76-6cb11ae920c2&token_type=bearer&expires_in=22478&scope=all
  if (window.location.href.includes('type=implicit')) {
    // 如果存在implicit参数，则代表是通过简化模式页面回调，把tab页切换为简化模式
    activeName.value = 'implicit';
    setAccessToken();
  }
  // http://localhost:18080/auth?type=code&code=53vGZz
  if (window.location.href.includes('type=code')) {
    // 如果存在code参数，则代表是通过授权码模式页面回调，把tab页切换为授权码模式
    activeName.value = 'authorization_code';
    setCode();
  }
});
</script>

<style scoped lang='scss'>
.auth-container {
  padding: 150px 320px;

  .card-content {
    // flex布局
    display: flex;
    // 居中
    justify-content: center;
  }

  .code-button-content {
    display: flex;
    justify-content: flex-end;

    .code-button {
      margin-top: 20px;

    }
  }

  .response-content {
    margin-top: 20px;
  }
}

/* 移除tabs下面的一条线的伪类样式 */
::v-deep(.el-tabs__nav-wrap:after) {
  content: ".";
  display: block;
  visibility: hidden;
  height: 0;
  line-height: 0;
  clear: both;
}
</style>