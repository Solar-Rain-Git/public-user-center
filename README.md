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

![image-20240613162652852](C:/Users/%E8%8C%95%E8%8C%95/AppData/Roaming/Typora/typora-user-images/image-20240613162652852.png)

#### 4.12 选择依赖

![image-20240613163641125](C:/Users/%E8%8C%95%E8%8C%95/AppData/Roaming/Typora/typora-user-images/image-20240613163641125.png)

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

![image-20240613165841510](C:/Users/%E8%8C%95%E8%8C%95/AppData/Roaming/Typora/typora-user-images/image-20240613165841510.png)

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

![image-20240615155229178](C:/Users/%E8%8C%95%E8%8C%95/AppData/Roaming/Typora/typora-user-images/image-20240615155229178.png)

编写一个单元测试来检验是否成功

![image-20240615155542775](C:/Users/%E8%8C%95%E8%8C%95/AppData/Roaming/Typora/typora-user-images/image-20240615155542775.png)

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

正向代理：替客户端向服务器发送请求，可以解决跨域问题1710559699197358082_0.7020564202784085

反向代理：替服务器统一接收请求。

怎么实现代理？

- Nginx 服务器
- Node.js 服务器
- 1710559699197358082_0.9619090812781044

### 举例

原本请求：http://localhost:8000/api/user/login

代理到请求：http://localhost:8080/api/user/login1710559699197358082_0.27330517789439046

![img](https://pic.code-nav.cn/post_picture/1610518142000300034/40647255-3a78-45f7-8bce-f4a13da0f907.png)
