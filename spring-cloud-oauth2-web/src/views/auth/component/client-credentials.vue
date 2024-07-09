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
      <el-form-item label="client_id" prop="client_id">
        <el-input v-model="form.client_id"/>
      </el-form-item>
      <el-form-item label="client_secret" prop="client_secret">
        <el-input v-model="form.client_secret"/>
      </el-form-item>
      <el-form-item label="grant_type" prop="grant_type">
        <el-text type="primary">{{ form.grant_type }}</el-text>
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
  client_id: string;
  client_secret: string;
  grant_type: string;
}

const ruleFormRef = ref<FormInstance>();

// 表单校验规则，具体看element-plus文档
const rules = reactive<FormRules<RuleForm>>({
  client_id: [
    {required: true, message: 'client_id不能为空', trigger: 'blur'}
  ],
  client_secret: [
    {required: true, message: 'client_secret不能为空', trigger: 'blur'}
  ],
  grant_type: [
    {required: true, message: 'grant_type不能为空', trigger: 'blur'}
  ]
});

// 表单，这里给默认值方便测试，如果后台配置其他的，记得修改
const form = reactive({
  client_id: 'web',
  client_secret: '123456',
  grant_type: 'client_credentials'
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