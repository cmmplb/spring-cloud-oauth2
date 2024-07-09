<template>
  <div class='client-credentials-container'>
    <!-- https://element-plus.org/zh-CN/component/form.html -->
    <el-form
      ref="ruleFormRef"
      :model="form"
      label-width="auto"
      :rules="rules"
      label-suffix=':'
    >
      <el-form-item label="username" prop="username">
        <el-input v-model="form.username"/>
      </el-form-item>
      <el-form-item label="password" prop="password">
        <el-input v-model="form.password"/>
      </el-form-item>
      <el-form-item label="grant_type" prop="grant_type">
        <el-text type="success">{{ form.grant_type }}</el-text>
      </el-form-item>
      <el-form-item class="request-content">
        <el-button type="primary" round class="request-button" @click="submitForm(ruleFormRef)">发起请求</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from 'vue';
import type {FormInstance, FormRules} from 'element-plus';

interface RuleForm {
  username: string;
  password: string;
  grant_type: string;
}

const ruleFormRef = ref<FormInstance>();

// 表单校验规则，具体看element-plus文档
const rules = reactive<FormRules<RuleForm>>({
  username: [
    {required: true, message: 'username不能为空', trigger: 'blur'}
  ],
  password: [
    {required: true, message: 'password不能为空', trigger: 'blur'}
  ],
  grant_type: [
    {required: true, message: 'grant_type不能为空', trigger: 'blur'}
  ]
});

// 表单，这里给默认值方便测试，如果后台配置其他的，记得修改
const form = reactive({
  username: 'admin',
  password: '123456',
  grant_type: 'password'
});

// 通过emit向父组件传递事件
const emit = defineEmits(['request']);

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      emit('request', form);
    } else {
      console.log('error submit!', fields);
    }
  });
};
</script>

<style scoped lang='scss'>
.client-credentials-container {

  // 发起请求样式
  .request-content {
    // 用了个绝对定位，让按钮居右
    position: relative;

    .request-button {
      position: absolute;
      right: 0;
      border: 1px solid #409EFF;
      background: none;
      font-weight: 300;
      color: #409EFF;
    }

    .request-button:hover {
      background: #409EFF11;
    }
  }
}
</style>