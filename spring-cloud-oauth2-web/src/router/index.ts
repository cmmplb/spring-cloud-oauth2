import {createRouter, createWebHistory} from 'vue-router';
import {App} from 'vue';

// 公共静态路由
const constantRoutes = [
  {
    path: '/',
    redirect: '/auth', // 重定向到/auth，效果是访问/时重定向到/auth
    children: [
      {
        path: '/auth',
        component: () => import ('@/views/auth/index.vue'),
        name: '认证授权'
      }
    ]
  }
];

const router = createRouter({
  // 相同的url，history会触发添加到浏览器历史记录栈中，hash不会触发，history需要后端配合，如果后端不配合刷新新页面会出现404，hash不需要
  history: createWebHistory(),
  // Hash模式会在根目录后面拼接/#/，优点是刷新页面不会丢失，缺点是URL会多一个/#/
  // history: createWebHashHistory(),
  routes: constantRoutes,
  strict: true,
  scrollBehavior: () => ({left: 0, top: 0})
});

export function setupRouter(app: App<Element>) {
  app.use(router);
}