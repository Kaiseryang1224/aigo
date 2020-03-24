# Spring Cloud Gateway 服務網關 (apiGateway)

API 主流網關有NGINX、ZUUL、Spring Cloud Gateway、Linkerd等；Spring Cloud Gateway構建於 Spring 5+，基於 Spring Boot 2.x 響應式的、非阻塞式的 API。同時，它支持 websockets，和 Spring 框架緊密集成，用來代替服務網關Zuul，開發體驗相對來說十分不錯。

Spring Cloud Gateway 是 Spring Cloud 微服務平台的一個子項目，屬於 Spring 開源社區，依賴名叫：spring-cloud-starter-gateway。
Zuul 是 Netflix 公司的開源項目，Spring Cloud 在 Netflix 項目中也已經集成了 Zuul，依賴名叫：spring-cloud-starter-netflix-zuul。

## API 網關

API 網關出現的原因是微服務架構的出現，不同的微服務一般會有不同的網絡地址，而外部客戶端可能需要調用多個服務的接口才能完成一個業務需求，如果讓客戶端直接與各個微服務通信，會有以下的問題：

- 客戶端會多次請求不同的微服務，增加了客戶端的複雜性。
- 存在跨域請求，在一定場景下處理相對複雜。
- 認證複雜，每個服務都需要獨立認證。
- 難以重構，隨著項目的迭代，可能需要重新劃分微服務。例如，可能將多個服務合併成一個或者將一個服務拆分成多個。如果客戶端直接與微服務通信，那麼重構將會很難實施。
- 某些微服務可能使用了防火牆 / 瀏覽器不友好的協議，直接訪問會有一定的困難。

以上這些問題可以藉助 API 網關解決。 API 網關是介於客戶端和服務器端之間的中間層，所有的外部請求都會先經過 API 網關這一層。也就是說，API 的實現方面更多的考慮業務邏輯，而安全、性能、監控可以交由 API 網關來做，這樣既提高業務靈活性又不缺安全性。

## 使用 API 網關後的優點如下：

- 易於監控。可以在網關收集監控數據並將其推送到外部系統進行分析。
- 易於認證。可以在網關上進行認證，然後再將請求轉發到後端的微服務，而無須在每個微服務中進行認證。
- 減少了客戶端與各個微服務之間的交互次數。

## Spring Cloud Gateway的特徵

- Java 8
- Spring Framework 5
- Spring Boot 2
- 動態路由
- 內置到Spring Handler映射中的路由匹配
- 基於HTTP請求的路由匹配 (Path, Method, Header, Host, etc…​)
- 過濾器作用於匹配的路由
- 過濾器可以修改下游HTTP請求和HTTP響應 (Add/Remove Headers, Add/Remove Parameters, Rewrite Path, Set Path, Hystrix, etc…​)
- 通過API或配置驅動
- 支持Spring Cloud DiscoveryClient配置路由，與服務發現與註冊配合使用

## 快速上手

Spring Cloud Gateway 網關路由有兩種配置方式：

- 在配置文件 yml 中配置
- 通過@Bean自定義 RouteLocator，在啟動主類 Application 中配置

這兩種方式是等價的，建議使用 yml 方式進配置。

### 1.pom.xml Maven依賴

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.9.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Greenwich.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!--Spring Cloud Gateway 是使用 netty+webflux 實現因此不需要再引入 web 模塊-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

### 2.application.yml配置

```yaml
server:
  port: 8080
spring:
  cloud:
    gateway:
      routes:
        - id: hrbot # 我們自定義的路由 ID，保持唯一
          uri: http://localhost:8081/api/hrbot/query # 目標服務地址
          predicates: # 路由條件，Predicate 接受一個輸入參數，返回一個布爾值結果。該接口包含多種默認方法來將
            - Path=/hrbot/query
```
上面這段配置的意思是，配置了一個id 為hrbot 的路由規則，當訪問地址http://localhost:8080/query時會自動轉發到地址：http://hrbot:8080/query

### 3.配置完成啟動項目

在瀏覽器訪問或是postman進行測試
