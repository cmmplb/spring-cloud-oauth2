# 学习oauth2

配合使用Element Plus + Vue3（idea开发，需要安装lombok插件）

版本：

| 依赖                                   | 版本             |
|--------------------------------------|----------------|
| spring-boot                          | 2.5.3          |
| spring-cloud                         | 2020.0.4       |
| spring-security                      | 3.2.10.RELEASE |
| spring-security-oauth2-autoconfigure | 2.1.2.RELEASE  |
| mybatis-plus                         | 3.4.3.1        |
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
├── 2.5         基于数据库读取数据信息+角色权限验证
├── 2.6         自定义认证模式
├── 2.7         基于数据库存取认证信息
├── 2.8         退出登录、事件监听和token信息扩展
````

---- 
1.模块搭建

2.功能实现

[2.1.认证服务四种模式.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.1.%E8%AE%A4%E8%AF%81%E6%9C%8D%E5%8A%A1%E5%9B%9B%E7%A7%8D%E6%A8%A1%E5%BC%8F.md)

[2.2.认证服务四种模式-前端实现.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.2.%E8%AE%A4%E8%AF%81%E6%9C%8D%E5%8A%A1%E5%9B%9B%E7%A7%8D%E6%A8%A1%E5%BC%8F-%E5%89%8D%E7%AB%AF%E5%AE%9E%E7%8E%B0.md)

[2.3.资源服务器.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.3.%E8%B5%84%E6%BA%90%E6%9C%8D%E5%8A%A1%E5%99%A8.md)

[2.4.登录、错误和授权页面替换+scope权限验证.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.4.%E7%99%BB%E5%BD%95%E3%80%81%E9%94%99%E8%AF%AF%E5%92%8C%E6%8E%88%E6%9D%83%E9%A1%B5%E9%9D%A2%E6%9B%BF%E6%8D%A2%2Bscope%E6%9D%83%E9%99%90%E9%AA%8C%E8%AF%81.md)

[2.5.基于数据库读取数据信息+角色权限验证.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.5.%E5%9F%BA%E4%BA%8E%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BB%E5%8F%96%E6%95%B0%E6%8D%AE%E4%BF%A1%E6%81%AF%2B%E8%A7%92%E8%89%B2%E6%9D%83%E9%99%90%E9%AA%8C%E8%AF%81.md)

[2.6.自定义认证模式.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.6.%E8%87%AA%E5%AE%9A%E4%B9%89%E8%AE%A4%E8%AF%81%E6%A8%A1%E5%BC%8F.md)

[2.7.基于数据库存取认证信息.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.7.%E5%9F%BA%E4%BA%8E%E6%95%B0%E6%8D%AE%E5%BA%93%E5%AD%98%E5%8F%96%E8%AE%A4%E8%AF%81%E4%BF%A1%E6%81%AF.md)

[2.8.退出登录、事件监听和token信息扩展.md](doc%2F2.%E5%8A%9F%E8%83%BD%E5%AE%9E%E7%8E%B0%2F2.8.%E9%80%80%E5%87%BA%E7%99%BB%E5%BD%95%E3%80%81%E4%BA%8B%E4%BB%B6%E7%9B%91%E5%90%AC%E5%92%8Ctoken%E4%BF%A1%E6%81%AF%E6%89%A9%E5%B1%95.md)

---- 

**认证服务四种模式**

![3.png](doc%2Fimages%2F2.1%2F3.png)

![4.png](doc%2Fimages%2F2.1%2F4.png)

![5.png](doc%2Fimages%2F2.1%2F5.png)

![6.png](doc%2Fimages%2F2.1%2F6.png)

**登录和授权页面替换**

![login.png](doc%2Fimages%2F2.4%2Flogin.png)

![approval.png](doc%2Fimages%2F2.4%2Fapproval.png)

学习参考地址:

`https://gitee.com/copoile/springcloud-oauth2`

`https://blog.csdn.net/qq15035899256/article/details/129541483`