# Spring Cloud Config 配置中心實現 (configMgr)

Spring Cloud Config 用於為分佈式系統中的基礎設施和微服務應用提供集中化的外部配置支持，分為server端和client端。 server端為分佈式配置中心，是一個獨立的微服務應用；client端為分佈式系統中的基礎設置或微服務應用，通過指定配置中心來管理相關的配置。 Spring Cloud Config 構建的配置中心，除了適用於 Spring 構建的應用外，也可以在任何其他語言構建的應用中使用， 默認採用 Git 存儲配置信息，支持對配置信息的版本管理。

## 本示例主要內容

- 配置中心演示client端和server端實現
- 配置文件放在git
- 版本切換（test、pro、dev）

## Spring Cloud Config 特點

- 提供server端和client端支持(Spring Cloud Config Server和Spring Cloud Config Client);
- 集中式管理分佈式環境下的應用配置;
- 基於Spring環境，實現了與Spring應用無縫集成;
- 可用於任何語言開發的程序;
- 默認實現基於Git倉庫(也支持SVN)，從而可以進行配置的版本管理；同時也支持配置從本地文件或數據庫讀取。

## 代碼構建

### server端實現

#### 1.pom.xml添加maven依賴

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
```

#### 2.application.yml配置

```yaml
server:
  port: 8001
spring:
  application:
    name: configSvr
  cloud:
    config:
      server:
        git:
          uri: https://gitlab.com/drd-ctbc/aigo.git
          searchPaths: /configMgr/config/
          force-pull: true
```

#### 3.ConfigSvrApplication.java啟動類

```java
@EnableConfigServer
@SpringBootApplication
public class ConfigSvrApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigSvrApplication.class, args);
    }
}
```

### client端實現

#### 1.pom.xml添加maven依賴

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

#### 2.bootstrap.properties配置文件

```properties
spring.cloud.config.name=aigo-config
spring.cloud.config.profile=test
spring.cloud.config.uri=http://localhost:8001/
spring.cloud.config.label=master
```

- spring.application.name：對應{application}部分
- spring.cloud.config.profile：對應{profile}部分
- spring.cloud.config.label：對應git的分支。如果配置中心使用的是本地存儲，則該參數無用
- spring.cloud.config.uri：配置中心的具體地址(sever端地址)
- spring.cloud.config.discovery.service-id：指定配置中心的service-id，便於擴展為高可用配置集群。

    特別注意：Spring Cloud 構建於 Spring Boot 之上，在 Spring Boot 中有兩種上下文，一種是 bootstrap, 另外一種是 application, bootstrap 是應用程序的父上下文，也就是說 bootstrap 加載優先於 applicaton。 bootstrap 主要用於從額外的資源來加載配置信息，還可以在本地外部配置文件中解密屬性。這兩個上下文共用一個環境，​​它是任何Spring應用程序的外部屬性的來源。 bootstrap 裡面的屬性會優先加載，它們默認也不能被本地相同配置覆蓋。

#### 3.application.properties配置文件

```properties
spring.application.name=configCli
server.port=8002
```

## 運行示例

### 1.首先在git上面創建一個文件夾config-repo用來存放配置文件，我們創建以下三個配置文件：
      
      // 開發環境
      aigo-config-dev.properties 內容為：aigo.hello=dev config
      // 測試環境
      aigo-config-test.properties 內容為：aigo.hello=test config
      // 生產環境
      aigo-config-pro.properties 內容為：aigo.hello=pro config
      
根據上面構建的代碼指定的項目地址為：https://gitlab.com/drd-ctbc/aigo.git 目錄為： /configMgr/config/

### 2.分別運行server端和client端

找到ConfigSvrApplication.java、ConfigCliApplication.java分別運行

### 3.測試server端

直接訪問：http://localhost:8001/aigo-config/dev

我們看到成功返回了開發配置文件信息

```json
{
name: "aigo-config",
profiles: [
"dev"
],
label: null,
version: "6053b4c1c2343ac27e822b2a9b60c6343be72f96",
state: null,
propertySources: [
{
name: "https://gitlab.com/drd-ctbc/aigo.git/configMgr/config/aigo-config-dev.properties",
source: {
aigo.hello: "dev config"
}
}
]
}
```

訪問：http://localhost:8001/aigo-config/test、http://localhost:8001/aigo-config/pro，相應的會返回測試及正式環境的配置

倉庫中的配置文件會被轉換成web接口，訪問可以參照以下的規則：

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties

以aigo-config-dev.properties為例子，它的application是aigo-config，profile是dev。 client會根據填寫的參數來選擇讀取對應的配置。

### 4.測試client端

訪問：http://localhost:8002/hello 我們發現界面成功返回了 test config，說明測試配置文件client端讀取成功了

我們修改bootstrap.properties配置的spring.cloud.config.profile的值為dev，重啟client端，訪問：http://localhost:8002/hello 這時候界面返回 dev config，表示開發配置訪問成功。
