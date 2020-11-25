package app.messages.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import app.messages.web.AuditingFilter;

@Configuration // 빈을 정의하기 위한 것임을 스프링에 알려주기 위해
@ComponentScan("app.messages") // 컴포넌트를 스캔할 기본 패키지를 알려주기 위해
@EnableTransactionManagement
public class AppConfig {

  private DataSource dataSource;

  public AppConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public LocalSessionFactoryBean SessionFactory() {
    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource);
    sessionFactoryBean.setPackagesToScan("app.messages");
    return sessionFactoryBean;
  }

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

  // 앱에 PlatformTransactionManager를 이용한 선언적 트랜잭션 관리 추가
  // 하이버네이트, JDBC, JPA, JTA, JMS 각각의 요소에 맞는 플랫폼 트랜잭션 매니저의 구현체를 이용하여 트랜잭션을 관리한다.
  // 플랫폼 트랜잭션 매니저는 트랜잭션 객체를 가져오는 메서드와 커밋, 롤백 메서드를 정의하고 있는 인터페이스
  @Bean
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(SessionFactory().getObject());
    return transactionManager;
  }
}
