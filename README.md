# Milkomeda
![tag](https://img.shields.io/github/tag/yizzuide/Milkomeda.svg) ![license](https://img.shields.io/github/license/yizzuide/Milkomeda.svg)

名字源于未来要融合的”银河织女系“，代表当前Spring生态的全家桶体系，这个项目以Spring生态为基础，从实际业务上整理出来的快速开发模块。

目前添加的模块有：
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
- [x] Echo（回响）：用于第三方请求，支持签名/验签、数据加密、可定制统一响应类型和成功校验。*1.13.0+*
    * 依赖技术：Spring MVC
    * 设计模式：模板方法模式、适配器模式、工厂方法模式
    
## Requirements
* Java 8
* Lombok 1.18.x
* SpringBoot 2.x

## Installation
```xml
<dependency>
    <groupId>com.github.yizzuide</groupId>
    <artifactId>milkomeda-spring-boot-starter</artifactId>
    <version>${milkomeda-last-version}</version>
</dependency>
```

## Dependency
```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.httpcomponents</groupId>
   <artifactId>httpclient</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.commons</groupId>
   <artifactId>commons-lang3</artifactId>
</dependency>
<dependency>
   <groupId>commons-beanutils</groupId>
   <artifactId>commons-beanutils</artifactId>
   <version>1.9.4</version>
</dependency>
<dependency>
   <groupId>joda-time</groupId>
   <artifactId>joda-time</artifactId>
</dependency>
<dependency>
   <groupId>org.projectlombok</groupId>
   <artifactId>lombok</artifactId>
</dependency>
```

## Documentation
[Pulsar使用文档](https://github.com/yizzuide/Milkomeda/wiki/Pulsar%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8)

[Comet使用文档](https://github.com/yizzuide/Milkomeda/wiki/Comet%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8)

[Pillar使用文档](https://github.com/yizzuide/Milkomeda/wiki/Pillar%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8)

[Particle使用之API方式](https://github.com/yizzuide/Milkomeda/wiki/Particle%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8%E4%B9%8BAPI%E6%96%B9%E5%BC%8F)

[Particle使用之注解方式](https://github.com/yizzuide/Milkomeda/wiki/Particle%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8%E4%B9%8B%E6%B3%A8%E8%A7%A3%E6%96%B9%E5%BC%8F)

[Light使用文档](https://github.com/yizzuide/Milkomeda/wiki/Light%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8)

[Echo使用文档](https://github.com/yizzuide/Milkomeda/wiki/Echo%E6%A8%A1%E5%9D%97%E4%BD%BF%E7%94%A8)

## Author
yizzuide, fu837014586@163.com

## License
Milkomeda is available under the MIT license. See the LICENSE file for more info.

