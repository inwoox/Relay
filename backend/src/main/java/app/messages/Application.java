package app.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// spring-boot-starter-web 의존성을 추가해, 스프링부트를 통해 스프링 MVC를 사용한다.
// 스프링 MVC를 사용하지 않으면, 서블릿을 생성하여, URL 패턴에 매핑해야한다.
// 스프링 MVC를 사용하면, 클래스를 생성해, @Controller 어노테이션을 추가하고, @RequestMapping으로 특정 URI 패턴에 매핑하면 된다.

// 이 애노테이션은 이 3가지 역할을 한다. @SpringBootConfiguration, @ComponentScan, @EnableAutoConfiguration
// 1. 동일 패키지에 있는 @Component, @Service 등 애노테이션을 찾아 빈으로 등록
// 2. 자동 구성을 수행 - org.springframework.boot.autoconfigure.EnableAutoConfiguration라는 Key값이 존재하며 하위에 많은 Class를 보유
// 또한 해당 Class들은 상단에 @Configuration이라는 Annotation을 가지고 있다.(@Configuration은 Bean을 등록하는 Java 설정파일) 
// 이러한 키값을 통하여 명시된 많은 Class들이 AutoConfiguration의 대상으로, 빈으로 생성된다.
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }}

  #curl-

  X POST-d'{"text":"Hello, Spring Boot"
}' -H"Content-Type: application/json"http:// localhost:8081/messages | jq
