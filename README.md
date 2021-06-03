# 模块介绍

1. xc-framework-parent\
所有模块的父模块，顶级模块，继承springboot，目的只有一个，控制依赖版本，打包方式为pom

2. xc-framework-utils\
全是静态工具类，然后引入其他工具包，lombok、lang3、fastjson等

3. xc-framework-common\
通用组件，继承utils的基础上（其实可以和util模块合并），
    1. 加入异常处理，
    2. 通用返回，
    3. Feign拦截器
        * 主要是微服务之间调用，传递header、cookie信息，传递jwt令牌的
        * zuul到微服务是可以传递令牌的，但是微服务和微服务之间是不走网关的，所以需要单独设置feign拦截器，加上相关配置
    4. base类（BaseController、BaseData等）
    5. 其他工具包：swagger、openfeign等

4. xc-framework-model\
实体类，所有模块的pojo、dto、vo、分页类等

5. xc-service-api\
所有模块controller接口，用来定义接口给前端看的，其实就是定义feign接口，远程调用
由于所有的controller接口都放在一个服务中，所以所有模块可以依赖此接口，然后使用
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common包下的类
@ComponentScan(basePackages={"com.xuecheng.manage_cms"})//扫描本项目下的所有类

6. xc-service-manage-cms\
页面查询服务，使用mongodb数据库，是管理页面的首页管理，增加菜单等信息、控制官网的首页展示信息

7. xc-service-manage-cms-client\
静态页面生成管理的

8. xc-service-manage-course\
课程管理服务，课程上下架等操作

9. xc-service-base-filesystem\
fastdfs存储服务

10. xc-govern-center\
注册中心

11. xc-service-manage-course\
课程中心

12. xc-service-ucenter-auth
认证服务



xc-govern-gateway
xc-service-base-filesystem
xc-service-learning

xc-service-manage-course
xc-service-manage-media
xc-service-manage-media-processor
xc-service-manage-order
xc-service-search
xc-service-ucenter


## 认证模块讲解：
xc-service-ucenter-auth 认证服务

oauth2服务流程:

客户端 --（客户端id）--> 微信的auth2微服务 - 数据库查询客户端id被注册，可以使用，然后让用户点击确认，确认可以登录，就同意，返回授权码\
客户端 <--（授权码）-- 微信的auth2微服务

客户端 --（授权码+密码）--> 微信的auth2微服务 - 授权码没问题，然后使用私钥加密，生成令牌\
客户端 <--（令牌）-- 微信的auth2微服务

客户端 --（令牌）--> 微信的用户资源微服务 - 使用公钥解密，发现可以，就返回用户信息\
客户端 <--（用户信息）-- 微信的用户资源微服务或者其他的资源服务器

所以：微服务框架为：
认证服务器 + 资源服务器

客户端从认证服务器先拿授权码，再得令牌（由资源服务器通过私钥生成），令牌到手后，
去访问其他微服务（叫做资源服务器），通过公钥解密，获得想要的内容

所以：除了认证服务微服务是oauth2的以为，所有其他微服务也要加入认证功能，（要存放公钥）

1. 添加依赖
<!--springsecurity安全框架-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>

2. 增加资源微服务配置类ResourceServerConfig

jti小令牌
