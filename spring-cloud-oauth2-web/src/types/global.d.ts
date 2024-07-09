declare module '*.vue' {
  // import Vue from "vue";
  // export default Vue
  import type {DefineComponent} from 'vue';
  const component: DefineComponent<{}, {}, any>;
  const _default: DefineComponent<{}, {}, {}, any, any, any, any, {}, any, any, {}>;
  export default component;
}

// 解决导入插件报错，声明该模块类型
declare module 'vue3-json-viewer';