# Milkomeda
![tag](https://img.shields.io/github/tag/yizzuide/Milkomeda.svg) ![license](https://img.shields.io/github/license/yizzuide/Milkomeda.svg)

名字源于未来要融合的”银河织女系“，代表当前Spring生态的全家桶体系，这个项目以Spring生态为基础，从实际业务上整理出来的快速开发模块。

## Modules
- [x] Pulsar（脉冲星）: 用于长轮询、耗时请求fast-timeout等。*0.1.0+*
   * 依赖技术：Spring MVC
   * 设计模式：适配器模式、代理模式
- [x] Comet（彗星）:  用于统一的请求切面日志记录（包括Controller层、Service层（*1.12.0+*））。*0.2.0+*
   * 依赖技术：Spring MVC
   * 设计模式：策略模式
- [x] Pillar（创生柱）: 用于if/else业务块拆分。*0.2.0+*
   * 可选依赖技术：Spring IoC
   * 设计模式：策略模式、适配器模式
- [x] Particle（粒子）: 用于幂等/去重、次数限制，及可扩展限制器责任链。*1.5.0+*
   * 依赖技术：Spring MVC、SpringBoot Data Redis
   * 设计模式：策略模式、责任链模式、组合模式
- [x] Light (光）: 用于快速缓存，支持超级缓存（ThreadLocal）、一级缓存（内存缓存池）、二级缓存（Redis)。 *1.8.0+*
   * 依赖技术：SpringBoot Data Redis
   * 设计模式：策略模式、模板方法模式、门面模式
- [x] Fusion（核裂变）：用于动态切面修改方法返回值（主要针对于定制前端响应数据格式）、根据条件启用/禁用组件方法的执行。*1.12.0+*
   * 依赖技术：Spring AOP
   * 设计模式：策略模式
- [x] Echo（回响）：用于第三方请求，支持签名/验签、数据加密、可定制统一响应类型和成功校验。*1.13.0+*
   * 依赖技术：Spring MVC
   * 设计模式：模板方法模式、适配器模式、工厂方法模式
- [x] Crust（外壳）：用于生成JWT Token，支持验证、刷新Token，可选配置对称与RSA非对称生成Token，BCrypt或自定义salt表字段加密的方式。*1.14.0+*
   * 依赖技术：Spring Security
   * 设计模式：模板方法模式、适配器模式
- [x] Ice（冰）：用于延迟列队的需求，支持配置延迟分桶、任务执行超时时间（TTR）、超时重试、Task自动调度等。*1.15.0+*
   * 依赖技术：Spring IoC、Spring Task、SpringBoot Data Redis
   * 设计模式：策略模式、享元模式、门面模式、面向声明式编程
- [x] Neutron（中子星）：用于定时作业任务，支持数据库持久化，动态创建Job、删除、修改Cron执行表达式。*1.18.0+*
   * 依赖技术：Spring IoC、Quartz
   * 设计模式：门面模式
- [x] Moon（月球）：用于在多个类型值之间轮询，支持并发操作，支持泛型数据值，不同的环业务相互隔离。*2.2.0*
  * 依赖技术：Spring IoC
  * 设计数据结构：环形链表
  * 设计模式：门面模式
    
## Requirements
* Java 8
* Lombok 1.18.x
* SpringBoot 2.x

## Version control guidelines
- 1.16.0+ for Spring Boot 2.1.x - 2.2.x
- Dalston.1.11.0-Dalston.1.12.0 for Spring Boot 1.5.x
- Others for Spring Boot 2.0.x

## Installation
```xml
<dependency>
    <groupId>com.github.yizzuide</groupId>
    <artifactId>milkomeda-spring-boot-starter</artifactId>
    <version>${milkomeda-last-version}</version>
</dependency>
```

## 2.0 Release
Milkomeda 2.0 is now available (Dec 2019).

- 构建的包更小，减少即时的依赖，根据开启的模块选择依赖。
- 模块的使用更加明了，需要使用什么模块，使用`@EnableXXX`（除了非Spring依赖模块不需要开启）。
- 部分模块使用API改进，优先使用注解的声明式编程，并使用SpEL增强，然后是API方法调用。
- 各模块间的功能相互增强，如：`Crust`添加`Light`模块缓存加持、`Comet`添加`Pillar`模块拆分处理等。
- 重构各模块的Config配置依赖，合理拆分工具类、Context等。

> 在2.0改造中，有了@mars的加入，非常感谢提供很多好的建议和改进


## Migrating to 2.x from 1.x
1. 除了`Pillar`模块外，其它模块都需要通过`@EnableXXX`来启用模块（迁移请注意！）。
2. 在1.x版本默认依赖的`Spring Data Redis`已被删除，需要根据使用模块是否依赖来在项目中添加（迁移请注意！）。
3. 模块`Particle`的限制器注解在取请求头的语法`@`改为`:`（由于和SpEL的`@`语法冲突问题）。
4. 模块`Light`的API方法方式改为使用`@LightCacheable`（仿Spring Cache，部分属性方法支持SpEL），默认使用了超级缓存（不用再操心超级缓存的复杂API了）。
5. 模块`Crust`的token方式内建支持`Light`模块的高效多级缓存。
6. 模块`Comet`添加注解`@CometParam`注解用于同时支持解析`application/x-www-form-urlencoded`、`JSON`的Body消息数据（Spring MVC默认是不支持的）。

## Documentation
[See Wiki](https://github.com/yizzuide/Milkomeda/wiki)

## Releases log
[See Releases](https://github.com/yizzuide/Milkomeda/releases)

## Contributing
*Mikomeda*还需要更多的业务型实用模块，欢迎有想法的开发者加入进来！可以通过以下原则来Pull Request:

- 从`master`分支拉取新分支，新添加的功能模块分支以`feat-`前缀开头，问题修复则以`fix-`为前缀。
- 添加的模块尽可能地通用，不能含有业务代码，最好可以提供使用的Demo并添加到`MilkomedaDemo`工程里，如果有好的想法需要讨论可以提一个以[feature]开头的issue进行讨论。
- 新添加的代码尽可能地规范，代码必需经过格式化，类的命名需要添加模块名前缀，新添加的模块需要添加到`Milkomeda`的`com.github.yizzuide.milkomeda`包下，属性和方法需要添加注释表明如果使用。
- 建议遵行提交注释以`feat|fix|docs|style|refactor|perf|test|workflow|ci|chore|types:`为前缀。
- 提交时不要提交IDE的配置相关文件和临时生成的文件，请注意排除。

> 关于如果开发*Mikomeda*项目：使用IDEA新建空的工程，再把工程模块`Mikomeda`和`MikomedaDemo`导入即可。

## Author
yizzuide, fu837014586@163.com

## License
Milkomeda is available under the MIT license. See the LICENSE file for more info.

## Thanks
<a href="https://www.jetbrains.com/idea/" target="_blank">
          <img width="64px" src="./logo/idea.png" alt="IntelliJ IDEA">
</a>

