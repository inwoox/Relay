package app.messages;

import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈을 정의하기 위한 것임을 스프링에 알려주기 위해
@ComponentScan("app.messages") // 컴포넌트를 스캔할 기본 패키지를 알려주기 위해
public class AppConfig {

  // 스프링부트 애플리케이션에서 필터를 등록하려면 FilterRegistrationBean을 만들어 AppConfig에 등록한다.
  // setFilter 메서드로 Filter 설정, setOrder 메서드로 이 필터를 체인 내에 배치 (이때 순서값이 작은 것이 앞에 위치)
  // setUrlPatterns 메서드는 Filter를 등록할 구체적 경로를 지정 (이 필터는 /messages로 시작하는 요청만 처리)
  @Bean
  public FilterRegistrationBean<AuditingFilter> auditingFilterRegistrationBean() {
    FilterRegistrationBean<AuditingFilter> registration = new FilterRegistrationBean<>();
    AuditingFilter filter = new AuditingFilter();
    registration.setFilter(filter);
    registration.setOrder(Integer.MAX_VALUE);
    registration.setUrlPatterns(Arrays.asList("/messages/*"));
    return registration;
  }
}
