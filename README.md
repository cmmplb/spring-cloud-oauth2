# 学习oauth2

配合使用Element Plus + Vue3（idea开发，需要安装lombok插件）

版本：

| 依赖                                   | 版本             |
|--------------------------------------|----------------|
| SpringBoot                           | 2.5.3          |
| SpringCloud                          | 2020.0.4       |
| SpringSecurity                       | 3.2.10.RELEASE |
| SpringSecurity                       | 3.2.10.RELEASE |
| spring-security-oauth2-autoconfigure | 2.1.2.RELEASE  |
| jdk                                  | 1.8            |

仓库地址：https://gitee.com/cmmplb/spring-cloud-oauth2

doc目录添加了步骤文档。

````
spring-cloud-oauth2
├── doc                                                 文档
├── spring-cloud-oauth2-auth-server                     认证服务器 [20000]
├── spring-cloud-oauth2-eureka-server                   注册中心 [8761]
├── spring-cloud-oauth2-gateway                         网关服务 [10000]
├── spring-cloud-oauth2-resource-server                 资源服务    
├── spring-cloud-oauth2-system-server                   系统模块 [30000]
├── spring-cloud-oauth2-ui:                             前端框架 [18080]
├──pom.xml                                              父工程依赖
````

防止每个功能代码迭代替换了前面步骤的代码，所以每个功能模块的代码都放在了单独的分支上，按照doc目录下的序号打的分支。

````
feature
├── 1.x         模块搭建
├── 2.1         认证服务四种模式
├── 2.2         认证服务四种模式-前端实现
├── 2.3         资源服务器
├── 2.4         登录、错误和授权页面替换+scope权限验证
````

学习参考地址:

`https://gitee.com/copoile/springcloud-oauth2`

`https://blog.csdn.net/qq15035899256/article/details/129541483`