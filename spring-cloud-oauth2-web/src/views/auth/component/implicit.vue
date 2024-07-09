<template>
  <div class='implicit-container'>
    <div>
      <el-row>
        <el-text type="info">
          implicit模式需要客户端去授权服务器申请token，认证通过后回调地址栏携带token信息。
        </el-text>
      </el-row>
      <el-rate/>
      <el-row>
        <el-text type="danger">
          <span>注意这里的回调地址需要在授权服务器配置。AuthorizationServerConfiguration=>authorizedGrantTypes</span>
        </el-text>
      </el-row>
    </div>
    <el-button type="info" class="request-button" plain @click="checkToken">跳转授权服务器</el-button>
  </div>
</template>

<script setup lang="ts">

const checkToken = () => {
  // 浏览器地址跳转到授权服务器，后面拼接一个type参数，用来标识授权类型，回调的时候处理，注意这里的回调地址http://localhost:18080/auth需要在授权服务器配置
  location.href = 'http://localhost:10000/auth/oauth/authorize?client_id=web&response_type=token&redirect_uri=http://localhost:18080/auth?type=implicit';
};
</script>

<style lang="scss" scoped>
.implicit-container {
  height: 168px;
  width: 416px;
  position: relative;

  .request-button {
    position: absolute;
    right: 0;
    bottom: 0;
    border: 1px solid #409EFF;
    background: none;
    font-weight: 300;
    color: #409EFF;
  }

  .request-button:hover {
    background: #409EFF11;
  }
}
</style>