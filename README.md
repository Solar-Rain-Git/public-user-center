## Solar-Rain一站式用户中心

> 在线访问：https://user-center.sunrainzc.cn/
>
> Github：https://github.com/Solar-Rain-Git/public-user-center
>
> （PC端项目）

## 1. 项目介绍

> 企业开发流程：
>
> 需求分析 => 设计（概要设计、详细设计）=> 技术选型 => 初始化 / 引入需要的技术 => 写 Demo => 写代码（实现业务逻辑） => 测试（单元测试、系统测试）=> 代码提交 / 代码评审 => 部署 => 发布上线

### 1.1 简介

⭐**企业核心的用户中心系统**，基于 `Spring Boot` 后端 + `React` 前端的 **全栈项目** 。

⭐**可直接移植到其他项目进行其他业务开发**。

### 1.2 功能：

1. 用户邮箱注册、账户/邮箱登录
2. 用户忘记密码邮箱通过重置
3. 用户权限路由访问
4. 用户个人中心修改数据
5. 管理员用户管理
    1. 管理员按照更多条件查询用户
    2. 管理员逻辑删除用户
    3. 管理员不得修改普通用户信息，只能修改用户状态和用户角色，邮箱通知修改信息
    4. 管理员不能修改自己的状态和角色，只能修改除状态角色的其他信息
    5. 管理员不能修改同级管理员的任何信息
6. 支持用户头像上传至阿里云OSS对象存储
7. 支持邮箱发送相关信息给具体用户

### 1.3 截图：

![image-20240711183533240](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711183533240.png)

![image-20240711183632160](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711183632160.png)

![image-20240711183550393](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711183550393.png)

![image-20240711183821100](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711183821100.png)

![image-20240711184039513](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711184039513.png)

![image-20240712195932879](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240712195932879.png)

![image-20240711184322985](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711184322985.png)

![image-20240711184434042](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240711184434042.png)

## 2. 项目技术

### 2.1 前端

主要运用阿里 Ant Design 生态：

- HTML + CSS + JavaScript 三件套
- React 开发框架
- Ant Design Pro 项目模板
- Ant Design 端组件库
- Umi 开发框架
- Umi Request 请求库
- 正向和反向代理

### 2.2 前端技术选型原因

> React 是主流的前端开发框架。

**优势：**

1. **组件化开发**： React 的核心概念是组件化开发，将UI拆分为独立的可重用组件，使代码更易于理解、维护和测试。
2. **虚拟 DOM**： React引入了虚拟DOM的概念，通过在内存中维护虚拟DOM树，最小化了实际DOM操作，提高性能和响应速度。
3. **单向数据流**： React采用了单向数据流模型，使数据流动可控，易于跟踪和调试。
4. **生态系统**：React拥有丰富的生态系统，包括大量的第三方库和组件，可以加速开发并提供各种解决方案。
5. **跨平台支持**： React可以用于构建Web应用、移动应用（React Native）、桌面应用（Electron）等，使得开发团队可以在不同平台上共享代码和技能。
6. **社区支持**： React拥有庞大的开发者社区和活跃的维护团队，可以获得大量的教程、文档和支持。

简单来说，就是用的人多、性能很好、生态成熟。

**适用场景：**

1. 单页面应用（SPA）： React非常适合构建单页面应用，其中所有交互都在同一个页面中完成，无需每次加载新页面。
2. 大规模应用： React 的性能和可维护性使其成为开发大规模应用的理想选择，特别是需要多人协作的项目。
3. 跨平台开发： 如果需要同时支持Web、iOS和Android，可以使用React和React Native来共享代码和技能。
4. 服务端渲染（SSR）： React可以用于服务器端渲染，提高应用的性能和搜索引擎优化（SEO）。

简单来说，适合开发企业级、高性能应用。

> 项目中为什么选择了 Ant Design Pro 脚手架？可以谈谈你对 Ant Design Pro 的使用体会和优缺点。

**为了提高开发效率。**

Ant Design Pro 是一个开箱即用的企业级前端应用开发脚手架，基于 React 和 Ant Design 构建，能够通过命令行选项的方式，快速创建一个包含示例页面、全局通用布局、权限管理、路由管理、国际化、前端工程化的默认项目。我只需要在此基础上编写和业务相关的页面即可，大幅节省时间。

Ant Design Pro 虽然功能强大且方便，但是对新手不够友好，一方面是项目功能太多，理解项目中的代码文件、系统阅读官方文档都要花费不少时间；另一方面是框架封装的代码较多，如果现有的功能不满足要求，自己定制开发新能力的成本较大。而且 Ant Design Pro 框架更新迭代太快，阅读文档和安装依赖时一定要选择指定的版本，否则可能就会出现依赖冲突、代码不兼容的情况。

### 2.3 后端

- 编程语言java
- spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- springboot（ **快速启动** / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
- springmvc（web 框架，提供接口访问、restful接口等能力）
- mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
- mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
- junit 单元测试库
- mysql 数据库
- 阿里云OSS
- Email邮箱发送配置

### 2.4 后端技术选型原因

> 作为一个前端必会的后端技术

**Spring Boot 的优势：**

1. 快速开发和简化配置： Spring Boot采用了约定大于配置的原则，减少了繁琐的配置，提供了自动配置和起步依赖，让开发人员能够更快速地搭建应用程序。
2. 内嵌式Web容器： Spring Boot内置了常见的Web容器，如Tomcat、Jetty和Undertow，这意味着你可以将你的应用程序打包成一个独立的JAR文件，无需外部的Web服务器。
3. 自动配置： Spring Boot根据项目的依赖自动配置应用程序的组件，大大减少了手动配置的工作，提高了开发效率。
4. 生态系统支持： Spring Boot整合了Spring框架的各个模块，如Spring MVC、Spring Data、Spring Security等，可以轻松构建全栈应用。
5. 监控和管理： Spring Boot提供了丰富的监控和管理功能，包括健康检查、性能指标、远程Shell等，有助于更好地管理和维护应用程序。
6. 适用于微服务架构： Spring Boot是构建微服务架构的理想选择，它可以轻松地创建独立的服务，而且与Spring Cloud等微服务相关的工具集成得很好。
7. 大型社区支持： Spring Boot有一个庞大的开发者社区，提供丰富文档、教程和第三方库，便于开发人员解决问题和分享经验。

**Spring Boot 的适用场景：**

1. Web应用程序开发：Spring Boot适用于构建各种类型的Web应用程序，包括单页应用、多页应用、RESTful API等。
2. 微服务架构：Spring Boot可以用于构建微服务应用，每个微服务可以独立开发、部署和管理。
3. 批处理应用程序：Spring Boot提供了对Spring Batch的支持，用于处理大规模的数据批处理任务。
4. 云原生应用：Spring Boot与容器编排平台（如Kubernetes）和云服务（如AWS、Azure、Google Cloud）集成得很好，可以轻松部署和扩展应用程序。
5. 企业级应用程序：Spring Boot提供了大量企业级特性，如事务管理、安全性、缓存、消息队列等，适用构建复杂企业级应用。

**什么是 MyBatis-Plus？它有什么作用？它和 MyBatis 有哪些区别？**

MyBatis 是一个半 ORM（对象关系映射）框架，它内部封装了 JDBC，使用它后，开发者只需要关注 SQL 语句本身，不需要花费精力去处理加载驱动、创建连接、创建 statement 等繁杂的过程，提高开发效率。

MyBatis-Plus 是 MyBatis 的增强版框架，它对 MyBatis 进行了 **二次封装** ，只做增强不做改变。通过提供一系列的 API 方法和代码生成器，使重复的 CRUD（创建、读取、更新、删除）操作更加简单，无需手动编写 SQL 语句，**大幅提高开发效率** 。

此外，还有一些其他优点。比如：

1. 提供了条件构造器，可以通过编程方式构建复杂的查询条件，无需编写繁琐的 SQL 语句
2. 提供了分页查询支持，无需手动编写分页逻辑
3. 提供了一个代码生成器工具，可以根据数据库表自动生成 Java 实体类、Mapper 接口和 XML 映射文件，减少手工编写工作量。

## 3. 项目运行

> 想要在本地运行项目，clone下来后需要更改后端的一些配置

### 3.1 后端 | 数据库

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username     VARCHAR(255)                       NULL COMMENT '昵称',
    userAccount  VARCHAR(255)                       NULL COMMENT '登录账号',
    avatarUrl    VARCHAR(1024)                      NULL COMMENT '头像URL',
    gender       TINYINT                            NULL COMMENT '性别 (0: 未知, 1: 男, 2: 女)',
    userPassword VARCHAR(512)                       NOT NULL COMMENT '密码',
    phone        VARCHAR(128)                       NULL COMMENT '电话',
    email        VARCHAR(512)                       NULL COMMENT '邮箱',
    userStatus   INT      DEFAULT 0                 NOT NULL COMMENT '用户状态 (0: 正常, 1: 禁用)',
    createTime   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime   DATETIME DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete     TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除 (0: 否, 1: 是)',
    userRole     TINYINT  DEFAULT 0                 NOT NULL COMMENT '用户角色 (0: 普通用户, 1: 管理员)'
) COMMENT '用户信息表';
```

### 3.2 后端 | 开发环境配置

```yaml
#application.yml  找到位置更改以下配置
spring:
  #本地数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: xxxx
    password: xxxx
    url: xxxxxxx
  #邮箱配置
  mail:
    #smtp服务主机  qq邮箱则为smtp.qq.com;163邮箱是smtp.163.com
    host: smtp.qq.com
    #服务协议
    protocol: smtp
    # 编码集
    default-encoding: UTF-8
    #发送邮件的账户
    username: xxxxxx@qq.com
    #授权码
    password: xxxxxxxx
    test-connection: true
    port: 587
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true 
#阿里云OSS文件上传配置
aliyun:
  oss:
    end-point: xxxxx
    access-key-id: xxxxx
    access-key-secret: xxxxx
    bucket-name: xxxxx
```

### 3.3 运行入口文件

在idea里配置好Java环境后，加载`maven`依赖，运行`UserBackendApplication.java`

### 3.4 前端

```sh
npm install
npm start
```

## 4. 项目部署

>常见部署方式
>
>- Linux 单机部署
>- Nginx Web 服务器
>- Docker 容器
>- 容器托管平台

### 4.1 前端

线上接口`requestErrorConfig.ts`：

```js
// 请求拦截器
requestInterceptors: [
    (config: RequestOptions) => {
        // 拦截请求配置，进行个性化处理。
        let url = process.env.NODE_ENV === 'production' ? "http://192.168.6.129:8080" + config?.url : config?.url;
        // 设置带上 cookie 的选项
        const withCredentials = true;
        // @ts-ignore
        const timeout = config?.timeout + 10000;
        return {...config, url, timeout, withCredentials};
    },
],
//将"http://192.168.6.129:8080"huan
```

1. 打包`dist`文件

    ```sh
    npm run build
    ```

2. 部署方式 | nginx 

    将`dist`上传至服务器文件夹，指定nginx配置即可，例：

    ```nginx
    user  root;
    worker_processes  1;
    
    events {
        worker_connections  1024;
    }
    
    http {
        include       mime.types;
        default_type  application/octet-stream;
        sendfile        on;
        keepalive_timeout  65;
    
        server {
            listen       80;
            server_name  localhost;
            location / {
                root   /root/services/project/dist/;#项目文件;
                index  index.html index.htm;
                try_files $uri $uri/ /index.html;#单页跳转配置
            }
    
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   html;
            }
        }
    }
    ```

3. 部署方式 | Docker

    在服务器根据 Dockerfile 构建镜像：

    ```bash
    # 前端
    docker build -t user-center-front:v0.0.1 .
    ```

    docker run 启动：

    ```bash
    # 前端
    docker run -p 80:80 -d user-center-front:v0.0.1
    ```

    注意端口占用问题

4. 宝塔部署，自行百度

### 4.2 后端

1. 通过`application-prod.yml`修改线上数据库

    ```properties
    #application-prod.yml  更换为你的线上数据库
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        username: xxxxx
        password: xxxxxx
        url: xxxxxxxx
    ```

2. 服务器配置springboot后端

    ```sh
    #使用 apt 命令安装 OpenJDK 8 及其所有依赖项。
    sudo apt install -y openjdk-8-jdk
    #验证 JDK 1.8 是否安装成功
    java -version
    #使用 apt 命令安装 Maven。
    sudo apt install -y maven
    #安装完成后，验证 Maven 是否安装成功
    mvn -version
    ```

3. 利用`maven`工具打包后端`jar`包上传至服务器，上传后端`jar`包后添加权限

    ```sh
    chmod a+x userBackend-0.0.1-SNAPSHOT.jar
    ```

    然后运行jar包

    ```sh
    #后台运行
    nohup java -jar userBackend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
    #查看后台任务
    jobs
    #查看所有的java程序
    jps
    ```

4. Docker部署后端

    在服务器根据 Dockerfile 构建镜像：

    ```bash
    # 后端
    docker build -t user-center-backend:v0.0.1 .
    ```

    docker run 启动：

    ```bash
    # 后端
    docker run -p 8080:8080 -d user-center-backend:v0.0.1
    ```


5. 宝塔部署，自行百度

### 4.3 跨域

浏览器为了用户的安全，仅允许向 **同域名、同端口** 的服务器发送请求。

如何解决跨域？

最直接的方式：把域名、端口改成相同的

**添加跨域头**

让服务器告诉浏览器：允许跨域（返回 cross-origin-allow 响应头）

**1. 网关支持（Nginx）**

```nginx
# 跨域配置
location ^~ /api/ {
    proxy_pass http://127.0.0.1:8080/api/;
    add_header 'Access-Control-Allow-Origin' $http_origin;
    add_header 'Access-Control-Allow-Credentials' 'true';
    add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    add_header Access-Control-Allow-Headers '*';
    if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Credentials' 'true';
        add_header 'Access-Control-Allow-Origin' $http_origin;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
        add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        add_header 'Access-Control-Max-Age' 1728000;
        add_header 'Content-Type' 'text/plain; charset=utf-8';
        add_header 'Content-Length' 0;
        return 204;
    }
}
```

**2. 修改后端服务**

1. 配置**` @CrossOrigin `**注解
2. 添加 web 全局请求拦截器

```java
@Configuration
public class WebMvcConfg implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                //当 **Credentials为true时，** Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                .allowedOrigins("http://localhost:9527", "http://127.0.0.1:9527", "http://127.0.0.1:8082", "http://127.0.0.1:8083")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }
}
```

**3. 定义新的 corsFilter Bean**

参考：https://www.jianshu.com/p/b02099a435bd

## 5. 项目收获

> 主要学习到企业性质项目开发流程，从需求分析到技术选型，从开发实施到代码提交审核，从项目部署到发布上线

### 5.1 前端

1. 熟悉 React 框架开发，能够根据业务定制前端模板，比如**基于umi封装全局异常处理逻辑**、**多环境配置**等
2. 熟悉前端代码规范，并能够使用 ESLint + Prettier + TypeScript + StyleLint 等技术保证前端项目质量。
3. 熟悉 Ant Design Pro 框架，以及 Ant Design、Ant Design ProComponents 等组件库的使用
4. 能够使用 VS Code、WebStorm IDE 等开发工具快速开发前端项目
5. 提升问题解决能力，能够利用 GitHub Issues 区、AI 工具、搜索引擎、Stack Overflow 等自主解决问题

### 5.2 后端

1. 熟悉 Java 知识（如集合类、异常处理），能熟练运用 Lambda 表达式编程，使用 JUnit 编写单元测试。
2. 熟悉 SSM + Spring Boot 开发框架，能够使用 MyBatis Plus + MyBatis X 自动生成基础 CRUD 代码
3. 熟悉 MySQL 数据库及库表设计，能够通过创建索引、Explain 分析等方式优化性能
4. 熟悉常见业务问题的解决方案：比如**正向 / 反向代理**、全局跨域解决、**多环境问题解决等**
5. 熟练使用 Git、IDEA、ChatGPT、Navicat 等工具提高开发协作效率
6. 熟悉使用**阿里云OSS对象存储文件上传**配置
7. 熟悉**第三方邮件发送配置**，结合freemarker自定义ftl文件发送邮件给用户
8. 熟悉如何**自定义全局异常处理器**、**自定义通用返回数据格式**

### 5.3 部署

1. 熟悉了**云服务器**的购买和搭建过程，**域名解析**等
2. **学会了多种项目部署方式**：Linux 单机部署、Nginx Web 服务器、Docker 容器、容器托管平台
3. 学会`Nginx`配置代理，**网关配置跨域代理**
4. 学会了多种**跨域问题**的解决方法
5. **学会前后端分离部署，统一请求后端接口**

## 6. 项目优化

> 前后端业务代码优化过后，真实企业项目的业务大多优化都是放在后端处理的
>
> 以下问题是作为一名前端开发能想到的一些项目优化问题

1. 使用 Redis 存储用户登录的分布式 Session，实现多机用户登录状态同步
2. 使用 RBAC 权限管理设计，给不同的用户分配不同的权限，实现复杂的权限管理
3. 通过 set-cookie 父域名的方式，使得用户 cookie 能够在多个同父域名子系统间共享，实现单点登录（也可以使用 OAuth2、JWT Token 等方式）
4. 后端使用 AOP 或者 Filter 实现全局请求拦截器，统一去判断用户权限、统一记录请求日志等
