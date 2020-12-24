package taskagile.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import taskagile.web.api.authenticate.AuthenticationFilter;
import taskagile.web.api.authenticate.SimpleAuthenticationFailureHandler;
import taskagile.web.api.authenticate.SimpleAuthenticationSuccessHandler;
import taskagile.web.api.authenticate.SimpleLogoutSuccessHandler;



// 스프링 시큐리티 설정하기 
// WebSecurityConfigurerAdapter 클래스를 상속하고, @EnableWebSecurity 어노테이션을 적용한다.
// 이렇게 하면 스프링 시큐리티 필터 체인이 자동으로 포함 된다.


// 스프링 시큐리티는 필터를 사용해, 요청 단위 인증과 권한 부여를 수행하고, AOP를 사용해 메서드 단위 권한 부여를 수행한다.
// 진행흐름 : HTTP 요청 -> 필터 (필터 체인) -> 컨트롤러 -> AOP -> 서비스 


// 메서드 보안 (AOP 사용)

// @Secure 어노테이션 사용  : @EnableGlobalMethodSecurity(securedEnabled = true)를 설정한 MethodSecurityConfig 클래스 / 메서드에 적용시 @Secured("ROLE_ADMIN") 
// JSR-250 @RoleAllowed : @EnableGlobalMethodSecurity(jsr250Enabled = true) 를 설정한 MethodSecurityConfig 클래스 / 메서드에 적용시 @RoleAllowed("ROLE_USER")
// @PreAuthorize, @PostAuthorize 어노테이션을 통해 스프링 표현 언어를 사용
// MethodSecurityInterceptor 인터페이스의 인스턴스가 메서드의 호출을 가로채고, 접근 권한 의사 결정은 AccessDecisionManager에게 위임된다.


// 스프링 시큐리티 필터 체인  (아래를 제외하고도 다양한 내장 필터가 존재한다)

//   WebAsyncManagerIntegrationFilter : SecurityManager와 비동기 처리를 위한 WebAsyncManager 간의 통합을 제공

// * SecurityContextPersistenceFilter (인증 저장 필터): SecurityContextRepo에서 SecurityContext를 가져와서 Holder에 담아 다음 필터로 전달한다.
//   SecurityContextHolder.getContext()로 접근 가능 / 요청 성공시 Holder에서 업데이트된 SecurityContext를 꺼내 HttpSession에 저장 / 요청이 응답을 받아 나갈 때, Holder 삭제
//	 이후 매 요청마다 HttpSession에서 SecurityContext를 조회하여, 있으면 Holder에 담았다가, 요청 스레드의 Threadlocal 저장소에 담아 요청을 처리하고,
//	 HttpSession에 SecurityContext가 없으면 (예컨대 첫 요청시) 새로운 SecurityContext를 생성하여 Holder에 담아 다음 필터로 전달

// 	 HeaderWriterFilter : 현재 요청에 대한 응답에 헤더를 추가
//	 LogoutFilter :				/logout 요청이 왔을 때 로그아웃 절차를 수행

// * UsernamePasswordAuthenticationFilter (인증 수행 필터) : 실제 인증을 수행하는 필터 (AuthenticationFilter 클래스 참고)

//	 RequestCacheAwareFilter : 이전에 캐시된 요청을 복원 (하나의 인증된 요청이 보호 리소스에 접근하다 임시로 캐시 되고, 사용자 인증 후에 다시 리다이렉트 되어 돌아왔을 때, 길 안내)
//	 SecurityContextHolderAwareRequestFilter : 서블릿 API 보안 메서드를 구현한 래퍼를 활용하는 요청에 대한 책임을 가진다

//	 AnonymousAuthenticationFilter : SecurityContext에 어떤 Authentication도 존재하지 않으면, SecurityContext를 AnonymousAuthenticationToken으로 업데이트
//	 실제로 익명 사용자 인증을 수행하지는 않지만, 요청이 익명 사용자로부터 온 것임을 확실히 하기 위해 플레이스 홀더 인증 (placeholder authentication)을 SecurityContext에 저장
// 	 인증여부 구현시 isAnonymous(), isAuthenticated()로 구분하여 사용

//	 SessionManagementFilter : 동시 세션 등 여러 세션 관련 설정을 관리 / 동시 세션은 ConcurrentSessionFilter가 필터 체인에 있어야한다.
//	 HttpSecurity가 제공하는 다음 설정으로 동시 세션 관리를 활성화할 수 있다.  http.sessionManagement().maximumSessions(n);  / 기본적으로 세션 수 제한은 없다.

// * ExceptionTranslationFilter (예외 해석 필터) : 스프링 시큐리티의 예외를 해석 / AuthenticationException이거나, 
//   AccessDeniedException + 익명사용자일 경우 인증 엔드포인트를 시작 / 익명이 아니면 AccessDeniedHandler는 기본적으로 HTTP 403 응답을 전달한다.

// * FilterSecurityInterceptor (인증,권한 검사 및 접근 권한 부여) : 3개의 AccessDecisionManager의 구현체가 있고, 각각 AcessDecisionVoter 구현체 리스트를 가진다
//	 그 구현체는 RoleVoter, AuthenticationVoter이며, RoleVoter는 부여된 권한이, 요청에 접근하는데 필요한 역할을 포함하는지 확인하고, AuthenticationVoter는
//	 SecurityContext에 있는 authentication 인스턴스가 인증 됐는지 확인한다 / AccessDecisionManager의 구현체 중 AffirmativeBased는
//	 AccessDecisionVoter로부터 하나라도 긍정적인 응답을 받으면 접근 권한 부여, ConsensusBased는 긍정 표가 부정 표보다 많을 때 접근 권한 부여, UnanimousBased는
//	 부정적인 투표가 없을 때만 접근 권한을 부여한다. / 접근 권한이 부여되면 요청은 컨트롤러로 가고, 권한이 없으면 AccessDeniedException 예외가 발생하며, 예외 해석 필터로 제어 흐름이 넘어간다.

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] PUBLIC = new String[]{ "/error", "/login", "/logout", "/register", "/api/registrations" };

  // 스프링 시큐리티 규칙 
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()									// PUBLIC에 명시된 경로 외 다른 요청은 인증된 사용자만 접근할 수 있음을 명시
        .antMatchers(PUBLIC).permitAll()
        // 환경설정에서 액추에이터의 엔드포인트를 API와 분리하고 엔드 포인트에 9000번 포트를 할당한다.
        // 서버의 방화벽을 활용해서 9000번 포트가 내부 네트워크를 통해서만 접근할 수 있도록 만드는데, 아래와 같이 설정하면 어디에서든 접근할 수 있다.
        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll() 
        
        // 또한 아래와 같이 하면 ACTUATOR_ADMIN 역할을 가지는 인증된 사용자만 엔드포인트에 접근할 수 있다
        // .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole("ACTUATOR_ADMIN")
        // application.properties에서 포트 설정 제거 
        .anyRequest().authenticated()
      .and()																// 메서드 호출 체인을 http 오브젝트로 복원한다.
      	
      	// 지정된 UsernamePasswordAuthenticationFilter 필터의 순서에 커스텀 필터를 추가하여, 필터를 대체 
        // addFilterBefore (지정된 필터 앞) / addFilterAfter (지정된 필터 뒤)
        .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .formLogin()												// 애플리케이션이 폼을 기반으로 한 인증을 활용
        .loginPage("/login")								// 로그인 페이지의 경로를 명시
      .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler(logoutSuccessHandler())
      .and()
        .csrf().disable();
    
  }

  // 스프링 시큐리티 무시 규칙
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/images/**", "/favicon.ico");
  }
  
  //  실제로 유저를 인증하는 AuthenticationProvider 구현체를 변경할 때 이렇게 설정
  //  @Override 
	//  protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	//  { /* AuthenticationProvider 구현체 */ 
	//  	auth.authenticationProvider(authenticationProvider); 
	//  	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); 
	//  }


  // 커스터마이징할 필터 설정 추가 및 빈으로 등록
  @Bean
  public AuthenticationFilter authenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter();
    
    // 인증 성공 & 실패 핸들러를 따로 등록 / 등록하지 않으면 기본 핸들러
    authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
    
    // WebSecurityConfigurerAdapter이 제공하는 authenticationManagerBean() 메서드를 통해 AuthenticationManager를 가져와서 등록한다.
    authenticationFilter.setAuthenticationManager(authenticationManagerBean());
    return authenticationFilter;
  }

  // 필터에서 사용할 핸들러들과 AuthenticationProvider가 사용하는 PasswordEncoder 인터페이스 구현체 빈 생성
  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new SimpleAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleAuthenticationFailureHandler();
  }

  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() {
    return new SimpleLogoutSuccessHandler();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
