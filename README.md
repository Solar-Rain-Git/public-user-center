## 用户中心项目笔记

> 目标：完整了解做项目的思路，接触一些企业级的开发技术，之后都能轻松做出管理系统！

## 企业做项目流程

1. 需求分析，设计（概要设计、详细设计）

2. 技术选型，初始化 / 引入需要的技术，写 Demo
3. 写代码（实现业务逻辑）
4. 测试（单元测试、系统测试）
5. 代码提交 / 代码评审
6. 部署，发布上线

## 1. 需求分析

1. 登录 / 注册
2. 用户管理（仅管理员可见）对用户的查询或者修改
3. 用户校验（仅xx用户可见）

## 2. 技术选型

前端：

- 三件套
- React
- 组件库 Ant Design
- Umi：蚂蚁集团的底层前端框架，是可扩展的企业级前端应用框架。Umi 以路由为基础，同时支持配置式路由和约定式路由，保证路由的功能完备，并以此进行功能扩展。然后配以生命周期完善的插件体系，覆盖从源码到构建产物的每个生命周期，支持各种功能扩展和业务需求。
- Ant Design Pro：基于 Ant Design 和 umi 的封装的一整套企业级中后台前端/设计解决方案，致力于在设计规范和基础组件的基础上，继续向上构建，提炼出典型模板/业务组件/配套设计资源，进一步提升企业级中后台产品设计研发过程中的『用户』和『设计者』的体验。

后端：

- java
- spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- springmvc（web 框架，提供接口访问、restful接口等能力）
- mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
- mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
- springboot（ **快速启动** / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
- junit 单元测试库
- mysql 数据库

部署：服务器 / 容器（平台）

## 3. 前端初始化

> https://pro.ant.design/zh-CN/docs/getting-started

### 3.1 前端瘦身

1. **移除国际化**
    webstorm进入项目文件夹根路径找到`package.json`文件，执行`i18n-remove`命令；
    删除`/src`目录下的`locales`文件夹；
    删除webstorm全局搜索与`SelectLang`相关的代码

2. **移除swagger**
    删除`/src/services`目录下的`swagger`文件夹；

3. **移除oneapi.json**
    删除`/src/services`目录下的`swagger`文件夹；
    删除`/config/config.ts`里的如下代码

    ```json
    openAPI: [
        {
            requestLibPath: "import { request } from '@umijs/max'",
            // 或者使用在线的版本
            // schemaPath: "https://gw.alipayobjects.com/os/antfincdn/M%24jrzTTYJN/oneapi.json"
            schemaPath: join(__dirname, 'oneapi.json'),
            mock: false,
        },
        {
            requestLibPath: "import { request } from '@umijs/max'",
            schemaPath: 'https://gw.alipayobjects.com/os/antfincdn/CA1dOm%2631B/openapi.json',
            projectName: 'swagger',
        },
    ],
    ```