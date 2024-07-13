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
>
> 这里只对Ant Design Pro初始化，需要的node环境请百度自行解决

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

    ```
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

## 4. 后端初始化

> 这里只对Java初始化，需要的MySQL环境请百度自行解决
>
> 1. GitHub 搜现成的代码
>
> 2. SpringBoot 官方的模板生成器（https://start.spring.io/）
>
> 3. 直接在 IDEA 开发工具中生成  ✔
>
>     如果要引入 java 的包，可以去 maven 中心仓库寻找（http://mvnrepository.com/）

### 4.1 IDEA生成初始化

#### 4.11 Spring配置

> 配置完下一步

![image-20240613162652852](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240613162652852.png)

#### 4.12 选择依赖

![image-20240613163641125](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240613163641125.png)

这些依赖项各有各的用途，在一个Spring Boot项目中，它们分别提供不同的功能和支持。以下是对每个依赖项的简要说明：

**1. Lombok**

**作用**： Lombok是一个Java库，它通过注解的方式简化和减少Java代码中的样板代码。例如，Lombok可以自动生成getter、setter、构造函数、`toString`、`equals`、`hashCode`等方法，从而减少手动编写这些代码的工作量。

**常用注解**：

- `@Getter` / `@Setter`
- `@ToString`
- `@EqualsAndHashCode`
- `@NoArgsConstructor` / `@AllArgsConstructor`
- `@Builder`

**示例**：

```java
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
}
```

**2. Spring Configuration Processor**

**作用**： Spring Configuration Processor是一个注解处理器，用于生成与Spring Boot配置属性相关的元数据。这有助于IDE（如IntelliJ IDEA或Eclipse）提供更好的自动补全和校验功能，从而提高开发效率和准确性。

**使用**：

- 在`application.properties`或`application.yml`中配置属性时，可以获得自动补全提示。
- 配合`@ConfigurationProperties`使用。

**示例**：

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    // getters and setters
}
```

**3. Spring Boot DevTools**

**作用**： Spring Boot DevTools是一个开发工具包，旨在提高开发效率。它包括自动重启、LiveReload、属性默认值以及其他开发时的便利功能。

**主要功能**：

- 自动重启：在类路径中的文件更改时自动重新启动应用程序。
- LiveReload：在浏览器中自动刷新页面。
- 提供更多开发时的默认配置。

**示例**： 无需额外配置，添加依赖后自动生效。

**4. Spring Web**

**作用**： Spring Web是Spring框架中的一个模块，用于构建Web应用程序，包括RESTful服务。它提供了创建Web应用程序的核心功能，包括控制器、视图解析和模型绑定。

**常用注解**：

- `@RestController`
- `@RequestMapping`
- `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping`

**示例**：

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
```

**5. MySQL Driver**

**作用**： MySQL Driver是MySQL数据库的JDBC驱动程序，用于在Java应用程序中连接和操作MySQL数据库。

**配置**： 在`application.properties`或`application.yml`中配置数据库连接属性。

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbname
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**6. MyBatis Framework**

**作用**： MyBatis是一个持久层框架，它简化了JDBC代码，提供了对数据库操作的更好支持。MyBatis通过XML或注解配置SQL语句，并将SQL结果映射到Java对象。

**配置**： 配置MyBatis Mapper和SQL映射文件。

```java
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
}
```

**示例**： 在Spring Boot中，通常通过配置类启用MyBatis：

```Java
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.mapper")
public class MyBatisConfig {
}
```

这些依赖项组合在一起，可以帮助你快速构建一个功能强大的Spring Boot应用程序，涵盖从开发工具、Web开发、数据库操作到配置管理等各个方面。初始化的spring如下。

![image-20240613165841510](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240613165841510.png)

**一定要刷新maven**

删除demo文件夹和static文件夹，移除demo

#### 4.13 SpringBoot框架整合

**1.MyBatis-Plus** https://baomidou.com/getting-started/  跟着教程一步步完成

将pom.yml文件里的以下代码注释掉，因为我们接入了MyBatis-Plus，会与已经存在的MyBatis起版本冲突

```
<!--        <dependency>-->
<!--            <groupId>org.mybatis.spring.boot</groupId>-->
<!--            <artifactId>mybatis-spring-boot-starter</artifactId>-->
<!--            <version>2.2.2</version>-->
<!--        </dependency>-->
```

## 5. 数据库设计

> 什么是数据库？存数据的
>
> 数据库里有什么？数据表（理解为 excel 表格）
>
> java 操作数据库？程序代替人工

### 5.1 什么是设计数据库表？

有哪些表（模型）？表中有哪些字段？字段的类型？数据库字段添加索引？表与表之间的关联？

举例：性别是否需要加索引（可以理解为文章目录）？

### 用户表设计

id（主键）bigint

username 昵称  varchar

userAccount 登录账号

avatarUrl 头像 varchar

gender 性别 tinyint

userPassword 密码  varchar

phone 电话 varchar

email 邮箱 varchar

userStatus 用户状态 int  0 - 正常

createTime 创建时间（数据插入时间）datetime

updateTime 更新时间（数据更新时间）datetime

isDelete 是否删除 0 1（逻辑删除）tinyint

userRole 用户角色 0 - 普通用户 1 - 管理员

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

## 6. 后端 | MyBatisX自动生成器的使用

MyBatisX 插件，自动根据数据库生成：

- domain：实体对象
- mapper：操作数据库的对象
- mapper.xml：定义了 mapper 对象和数据库的关联，可以在里面自己写 SQL
- service：包含常用的增删改查
- serviceImpl：具体实现 service

从而提高开发效率！

![image-20240615155229178](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240615155229178.png)

编写一个单元测试来检验是否成功

![image-20240615155542775](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240615155542775.png)

## 7. 后端 | 注册逻辑设计

> 1 用户在前端输入账户和密码、以及校验码（todo）
>
> 2 校验用户的账户、密码、校验密码，是否符合要求 
>
> （a非空、b账户长度 不小于 4 位、c密码就 不小于 8 位吧、d账户不能重复、e账户不包含特殊字符、f密码和校验密码相同）
>
> 3 对密码进行加密（密码千万不要直接以明文存储到数据库中）
>
> 4 向数据库插入用户数据

## 8. 后端 | 登录逻辑设计

### 8. 1 接口设计 

接受参数：用户账户、密码

请求类型：POST

请求体：JSON 格式的数据

请求参数很长时不建议用 get

返回值：用户信息（ 脱敏 ）

### 8. 2 登录逻辑 

1 校验用户账户和密码是否合法 （a非空、b账户长度不小于 4 位、c密码就不小于 8 位、d账户不包含特殊字符）

2 校验密码是否输入正确，要和数据库中的密文密码去对比 

3 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露 

4 我们要记录用户的登录态（session），将其存到服务器上（用后端 SpringBoot 框架封装的服务器 tomcat 去记录） cookie 

5 返回脱敏后的用户信息 

### 8. 3 如何知道是哪个用户登录了？ 

> javaweb 这一块的知识

1 连接服务器端后，得到一个 session 状态（匿名会话），返回给前端 

2 登录成功后，得到了登录成功的 session，并且给该session设置一些值（比如用户信息），返回给前端一个设置 cookie 的 ”命令“ session => cookie 

3 前端接收到后端的命令后，设置 cookie，保存到浏览器内 

4 前端再次请求后端的时候（相同的域名），在请求头中带上cookie去请求 

5 后端拿到前端传来的 cookie，找到对应的 session 

6 后端从 session 中可以取出基于该 session 存储的变量（用户的登录信息、登录名） 

### 8. 4 控制器注解

@RestController 适用于编写 restful 风格的 api，返回值默认为 json 类型

校验写在哪里？

●controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身（越少越好）

●service 层是对业务逻辑的校验（有可能被 controller 之外的类调用）

### 8.5 实现

控制层Controller封装请求

application.yml指定接口全局路径前缀：
```
servlet:
    context-path: /api
```

## 9. 后端 | 用户管理接口开发

接口设计关键：必须鉴权！！！

1. 查询用户（允许根据用户名查询）
2. 删除用户

## 10. 前端 | 登录设计

>  登录页面开发及验证

### 10.2 前后端交互

前端需要向后端发送请求才能获取数据 / 执行操作

怎么发请求：前端使用 ajax 来请求后端

前端请求库及封装关系

- axios 封装了 ajax
- request 是 ant design 项目又封装了一次

追踪 request 源码：用到了 umi 的插件、requestConfig 配置文件

现在的ant design pro版本将请求拦截统一在requestErrorConfig.ts里

## 11. 代理

正向代理：替客户端向服务器发送请求，可以解决跨域问题

反向代理：替服务器统一接收请求。

怎么实现代理？

- Nginx 服务器
- Node.js 服务器

举例

原本请求：http://localhost:8000/api/user/login

代理到请求：http://localhost:8080/api/user/login1710559699197358082_0.27330517789439046

![img](https://pic.code-nav.cn/post_picture/1610518142000300034/40647255-3a78-45f7-8bce-f4a13da0f907.png)

## 12. 前端 | 后端

前端 | 登录优化，新增注册开发路由、接口及组件，登录状态

后端 | 新增获取当前登录用户接口，优化脱敏用户方法

## 13. 前端 | 后端

### 13.1 前端

**用户管理：**

1. 管理员按照更多条件查询用户

2. 管理员逻辑删除用户

3. 上传头像功能（后续更改为上传到阿里云OSS）

4. 管理员修改用户规则：

    1. 管理员不得修改普通用户信息，只能修改用户状态和用户角色

    2. 管理员不能修改自己的状态和角色，只能修改除状态角色的其他信息

    3. 管理员不能修改同级管理员的任何信息

        （使用react父子组件传递数据触发函数来实现修改功能）

**个人中心：**使用Form表单组件来提交个人更改信息，用`setInitialState`方法更新全局用户信息

**新增个人中心路由，配置路由权限、修复bug等**

### 13.2 后端

**新增更新用户信息接口**

**修改用户查询接口，动态查询用户信息**

**修改用户删除接口，禁止删除管理员**

## 14. 用户注销

**前端：**新增用户注销请求

**后端：**新增用户注销接口、优化用户查询接口

## 15. 前后端优化

### 15.1 后端

#### 通用返回对象

目的：给对象补充一些信息，告诉前端请求在业务层面上的成功失败状态信息

通用返回类BaseResponse——工具类new BaseResponse——配置快捷键Live templates reok

自定义错误码ErrorCode.java

返回类支持返回正常错误

#### 封装全局异常处理

**定义业务异常类（实现）**：

1. 使Java异常类支持更多字段；
2. 自定义构造函数，更灵活、快捷设置字段
3. 编写全局异常处理器：Spring AOP，在调用方法前后进行额外处理`GlobalExceptionHandler.java`

**作用：**

1. 捕获全部代码异常，内部消化，让前端获取更准确业务报错信息，
2. 同时屏蔽掉项目框架本身的异常，不暴露服务器内部状态
3. 集中处理，比如日志

### 15.2 前端优化

#### 全局响应处理

应用场景：我们需要对接口的 **通用响应** 进行统一处理，比如从 response 中取出 data；或者根据 code 去集中处理错误，比如用户未登录、没权限之类的。

优势：不用在每个接口请求中都去写相同的逻辑

文件`requestErrorConfig.ts`中配置一个全局请求类（响应拦截器）。

## 16. 阿里云OSS文件上传配置

### 16.1 后端

> 参考：https://blog.csdn.net/qq_31745863/article/details/136647279?spm=1001.2014.3001.5506

编写阿里云OSS文件上传代码、优化用户业务接口

### 16.2 前端

新增文件上传接口，优化修改用户信息逻辑

## 17. 发送邮件配置

> 自定义FreeMarker：注册账户邮箱验证、忘记密码邮箱验证、管理员修改用户状态邮箱通知

### 17.1 后端

> 参考：
>
> https://blog.csdn.net/qq_49005459/article/details/120261877?spm=1001.2014.3001.5506
>
> https://blog.csdn.net/qq1328585964/article/details/123737477?spm=1001.2014.3001.5506

新增邮件发送依赖、编写邮件代码接口和实现类、修改密码接口、优化用户业务逻辑

### 17.2 前端

新增忘记密码页面、发送邮件接口、修改密码接口、优化用户业务逻辑

## 18. 部署打包

### 18.1 多环境理论

> 参考文章：https://blog.csdn.net/weixin_41701290/article/details/1201732831

**本地开发**：localhost（127.0.0.1）

**多环境**：指同一套项目代码在**不同的阶段需要**根据实际情况来调整配置并且**部署到不同的机器上**。

**为什么需要**？

1. 每个环境互不影响
2. 区分不同的阶段：开发 / 测试 / 生产
3. 对项目进行优化：
    1. 本地日志级别
    2. 精简依赖，节省项目体积
    3. 项目的环境 / 参数可以调整，比如 JVM 参数

针对不同环境做不同的事情。

**多环境分类**：

1. 本地环境（自己的电脑）localhost
2. 开发环境（远程开发）大家连同一台机器，为了大家开发方便
3. 测试环境（测试）开发 / 测试 / 产品，单元测试 / 性能测试 / 功能测试 / 系统集成测试，独立的数据库、独立的服务器
4. 预发布环境（体验服）：和正式环境一致，正式数据库，更严谨，查出更多问题
5. 正式环境（线上，公开对外访问的项目）：尽量不要改动，保证上线前的代码是 “完美” 运行
6. 沙箱环境（实验环境）：为了做实验

### 18.2 前端多环境实战

1. 请求地址说明

    开发环境：localhost:8080

    线上地址：例-http://192.168.6.129:8080

    ```js
    startFront(env) {
        if(env === 'prod') {
            // 不输出注释 
            // 项目优化
            // 修改请求地址
        } else {
            // 保持本地开发逻辑
        }
    }
    ```

2. 打包问题

    **项目的配置**

    > 不同的项目（框架）都有不同的配置文件，umi 的配置文件是 config，可以在配置文件后添加对应的环境名称后缀来区分开发环境和生产环境。参考文档：https://umijs.org/zh-CN/docs/deployment

    - umi4需要在config.ts里手动配置`exportStatic: {}`导出静态化来解决路由404问题
    - 使用nginx部署时要在配置文件添加`history: { type: 'browser' }`，下面有教程

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
    ```

    **环境配置**
    
    - 开发环境：config.dev.ts
    - 生产环境：config.prod.ts
    - 公共配置：config.ts 不带后缀

    **启动方式**
    
    - 开发环境：npm run start（本地启动，监听端口、自动更新）
    - 线上环境：npm run build（项目构建打包），可以使用 serve 工具启动（npm i -g serve）

### 18.3 后端多环境实战

SpringBoot 项目，通过 application.yml 添加不同的后缀来区分配置文件

可以在启动项目时传入环境变量：

```bash
java -jar ${name}.jar --spring.profiles.active=prod
```

主要是改：

- 依赖的环境地址
- 数据库地址
- 缓存地址
- 消息队列地址
- 项目端口号
- 服务器配置

1. 新增`application-prod.yml`(springboot的生产环境配置)，`application-prod.yml`是公共配置

    ```properties
    //主要修改线上生产数据库地址
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        username: rootuser
        password: 1712645973Se
        url: jdbc:mysql://solar-rain-db.rwlb.rds.aliyuncs.com:3306/usercenter
    ```

2. 修改`pom.xml`

    ```xml
    //去掉或注释<!--<skip>true</skip>-->，会导致没有清单文件及依赖的jar
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
        <configuration>
            <mainClass>com.solar.userbackend.UserBackendApplication</mainClass>
            <!--                    <skip>true</skip>-->
        </configuration>
        <executions>
            <execution>
                <id>repackage</id>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ```

3. 使用`maven`工具的生命周期指令先`clean`再`package`，得到jar包后执行以下指令就可以运行后端生产环境

    ```java
    java -jar .\userBackend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
    ```

## 19. 部署上线

### 19.1 原始部署

> `nginx`+`springboot`+`dist`
>
> 这里选用ubuntu20虚拟机来进行部署，以下为原生安装，主要学习练手。网上有一键配置的教程。

1. 安装web服务器`nginx`

    ```sh
    mkdir services
    cd /services
    sudo apt update
    sudo apt install curl
    curl -o nginx-1.24.0.tar.gz http://nginx.org/download/nginx-1.24.0.tar.gz
    tar -zxvf nginx-1.24.0.tar.gz
    cd nginx-1.24.0
    sudo apt install build-essential
    ./configure
    sudo apt install libpcre3 libpcre3-dev
    sudo apt install zlib1g zlib1g-dev
    sudo apt install libssl-dev
    ./configure --with-openssl
    make
    sudo make install
    ls /usr/local/nginx/sbin/nginx
    vim /etc/profile
    export PATH=$PATH:/usr/local/nginx/sbin	#在最后一行添加
    source /etc/profile
    nginx
    sudo apt install net-tools
    netstat -ntlp #查看启动情况
    #打开主机ip就可以看到nginx页面
    ```

    nginx配置文件

    ```sh
    cd /usr/local/nginx/conf
    cat nginx.conf 
    ```

2. 上传前端打包好的`dist`项目在`services`,重命名为`userFrontend`,在`config.ts`配置如下

    ```js
    history: { type: 'browser' },
    //exportStatic: {}
        
        
    //解释    
    // browser: 这种模式使用浏览器的 History API 来管理路由。它允许你使用像 /path/to/resource 这样的标准 URL 路径，而不会像传统的 Web 应用那样刷新页面。这种模式在现代的单页面应用（SPA）中很常见。
    history: {
        type: "browser",
    };
    //hash: 这种模式通过在 URL 中使用哈希符号 # 来管理路由。例如，/path/to/resource 可以表示为 /#/path/to/resource。这种模式兼容性很好，因为哈希部分不会发送到服务器，仅用于客户端路由。
    history: {
        type: "hash",
    };
    //memory: 这种模式简单地将路由信息保存在内存中，不会修改浏览器的 URL。这通常用于测试或者在不需要持久化 URL 的情况下使用。
    history: {
        type: "memory",
    };
    ```

3. 修改`nginx`配置

    ```sh
    cd /usr/local/nginx/conf
    vim nginx.conf
    ```

    ```sh
    user root;#将nginx.conf的user改为和启动用户一致
    worker_processes  1;
    
    #error_log  logs/error.log;
    #error_log  logs/error.log  notice;
    #error_log  logs/error.log  info;
    
    #pid        logs/nginx.pid;
    
    
    events {
        worker_connections  1024;
    }
    
    
    http {
        include       mime.types;
        default_type  application/octet-stream;
    
        #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
        #                  '$status $body_bytes_sent "$http_referer" '
        #                  '"$http_user_agent" "$http_x_forwarded_for"';
    
        #access_log  logs/access.log  main;
    
        sendfile        on;
        #tcp_nopush     on;
    
        #keepalive_timeout  0;
        keepalive_timeout  65;
    
        #gzip  on;
    
        server {
            listen       80;
            server_name  localhost;
    
            #charset koi8-r;
    
            #access_log  logs/host.access.log  main;
    
            location / {
                root   /root/services/userFrontend/;#项目文件
                index  index.html index.htm;
                try_files $uri $uri/ /index.html;#单页跳转配置
            }
    
            #error_page  404              /404.html;
    
            # redirect server error pages to the static page /50x.html
            #
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   html;
            }
    
            # proxy the PHP scripts to Apache listening on 127.0.0.1:80
            #
            #location ~ \.php$ {
            #    proxy_pass   http://127.0.0.1;
            #}
    
            # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
            #
            #location ~ \.php$ {
            #    root           html;
            #    fastcgi_pass   127.0.0.1:9000;
            #    fastcgi_index  index.php;
            #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
            #    include        fastcgi_params;
            #}
    
            # deny access to .htaccess files, if Apache's document root
            # concurs with nginx's one
            #
            #location ~ /\.ht {
            #    deny  all;
            #}
        }
    
    
        # another virtual host using mix of IP-, name-, and port-based configuration
        #
        #server {
        #    listen       8000;
        #    listen       somename:8080;
        #    server_name  somename  alias  another.alias;
    
        #    location / {
        #        root   html;
        #        index  index.html index.htm;
        #    }
        #}
    
    
        # HTTPS server
        #
        #server {
        #    listen       443 ssl;
        #    server_name  localhost;
    
        #    ssl_certificate      cert.pem;
        #    ssl_certificate_key  cert.key;
    
        #    ssl_session_cache    shared:SSL:1m;
        #    ssl_session_timeout  5m;
    
        #    ssl_ciphers  HIGH:!aNULL:!MD5;
        #    ssl_prefer_server_ciphers  on;
    
        #    location / {
        #        root   html;
        #        index  index.html index.htm;
        #    }
        #}
    
    }
    ```

4. 执行以下指令，接着浏览器打开主机ip
    ```sh
    nginx -s reload
    ps -ef|grep 'nginx'
    netstat -ntlp
    ```

5. 配置springboot后端

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

    上传后端`jar`包到`/root/services/userBackend/`后添加权限

    ```sh
    chmod a+x userBackend-0.0.1-SNAPSHOT.jar
    ```

    然后运行jar包

    ```sh
    java -jar userBackend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
    #后台运行
    nohup java -jar userBackend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &
    #查看后台任务
    jobs
    #查看所有的java程序
    jps
    ```


### 19.2 宝塔部署

> 这里不做服务器宝塔安装的过程

![image-20240708164738104](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240708164738104.png)

![image-20240708164958343](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240708164958343.png)

![image-20240708165334541](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240708165334541.png)

![image-20240708165633045](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240708165633045.png)

![image-20240708165735105](https://uploadtypora.oss-cn-hangzhou.aliyuncs.com/user-center/image-20240708165735105.png)

### 19.3 Docker部署

> 这里选用ubuntu20虚拟机来进行部署
>
> 安装：https://blog.csdn.net/Tester_muller/article/details/131440306

docker 是容器，可以将项目的环境（比如 java、nginx）和项目的代码一起打包成镜像，所有同学都能下载镜像，更容易分发和移植。

再启动项目时，不需要敲一大堆命令，而是直接下载镜像、启动镜像就可以了。

docker 可以理解为软件安装包。

Docker 安装：https://www.docker.com/get-started/ 或者宝塔安装

Dockerfile 用于指定构建 Docker 镜像的方法

Dockerfile 一般情况下不需要完全从 0 自己写，建议去 github、gitee 等托管平台参考同类项目（比如 springboot）

Dockerfile 编写：

- FROM 依赖的基础镜像
- WORKDIR 工作目录
- COPY 从本机复制文件
- RUN 执行命令
- CMD / ENTRYPOINT（附加额外参数）指定运行容器时默认执行的命令

根据 Dockerfile 构建镜像：

```bash
# 后端
docker build -t user-center-backend:v0.0.1 .

# 前端
docker build -t user-center-front:v0.0.1 .
```

Docker 构建优化：减少尺寸、减少构建时间（比如多阶段构建，可以丢弃之前阶段不需要的内容）

docker run 启动：

```bash
# 前端
docker run -p 80:80 -d user-center-front:v0.0.1

# 后端
docker run -p 8080:8080 -d user-center-backend:v0.0.1
```

如果后端8080端口没有启动成功，执行以下指令

```sh
#检查防火墙设置:
#确保防火墙允许通过端口 8080 的流量。使用以下命令检查是否有关于 8080 端口的规则：
sudo iptables -L -n
#如果防火墙配置存在限制，可以尝试添加规则允许流量通过端口 8080，如下所示：
sudo iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
sudo iptables-save
#请确保修改防火墙规则后重试访问。
```



虚拟化

1. 端口映射：把本机的资源（实际访问地址）和容器内部的资源（应用启动端口）进行关联
2. 目录映射：把本机的端口和容器应用的端口进行关联

进入容器：

```bash
docker exec -i -t  fee2bbb7c9ee /bin/bash
```

查看进程：

```bash
docker ps
```

查看日志：

```bash
docker logs -f [container-id]
```

杀死容器：

```bash
docker kill
```

强制删除镜像：

```bash
docker rmi -f
```

**Docker 平台部署**

1. 云服务商的容器平台（腾讯云、阿里云）
2. 面向某个领域的容器平台（前端 / 后端微信云托管） **要花钱！**

容器平台的好处：

1. 不用输命令来操作，更方便省事
2. 不用在控制台操作，更傻瓜式、更简单
3. 大厂运维，比自己运维更省心
4. 额外的能力，比如监控、告警、其他（存储、负载均衡、自动扩缩容、流水线）

爽就完事了！！！

## 20. 跨域解决

浏览器为了用户的安全，仅允许向 **同域名、同端口** 的服务器发送请求。

如何解决跨域？

最直接的方式：把域名、端口改成相同的

### 添加跨域头

让服务器告诉浏览器：允许跨域（返回 cross-origin-allow 响应头）

### 1. 网关支持（Nginx）

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

### 2. 修改后端服务

1. 配置 [@CrossOrigin ](https://www.code-nav.cn/CrossOrigin)注解
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

### 3. 定义新的 corsFilter Bean

参考：https://www.jianshu.com/p/b02099a435bd
