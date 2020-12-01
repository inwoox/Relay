package taskagile.web.api.authenticate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.Getter;
import lombok.Setter;
import taskagile.utils.JsonUtils;


// AbstractAuthenticationProcessingFilter를 상속하여 구현하며, 인증 처리 담당
// 본래의 UsernamePasswordAuthenticationFilter 필터도 AuthenticationManager의 인스턴스에 대한 참조를 갖는 Abstract.. 를 상속한다.
// 스프링 시큐리티의 기본 인증 처리 담당 필터인 UsernamePasswordAuthenticationFilter 위치에 이 커스텀 필터를 추가하여 오버라이딩


// UsernamePasswordAuthenticationFilter 인증 처리 흐름

// 유저 요청 -> AbstractAuthenticationProcessingFilter (인증 처리 필터) -> Authentication (인증정보) 추출
// 인증 매니저 (AuthenticationManager) 인터페이스 -> 인증 매니저 구현체, 제공자 매니저 (ProviderManager) -> 인증 제공자 (AuthenticationProvider) 인터페이스
// 인증 제공자 구현체 (DaoAuthenticationProvider) 

// 인증 성공 시 성공 핸들러가 주도권, 사용자 URL 리다이렉트
// SecurityContext에 존재하는 authentication을 로그인한 사용자 정보를 포함하는 Authentication 객체로 업데이트하고,
// SecurityContextPersistenceFilter가 인증된 사용자 정보 (authentication = Object:Authentication)를 담고 있는 SecurityContext를 
// SecurityContextHolder에서 꺼내어 HttpSession에 저장 (유저 세션 ID 반환)
// 이후 매 요청마다 HttpSession에서 SecurityContext를 조회하여, SecurityContextHolder에 담았다가, 다시 SecurityContext를 꺼내서
// 새로운 요청 스레드의 ThreadLocal (스레드의 고유 저장소)에 다시 담아 요청을 처리한다.

// 인증 실패 시 SecurityContextHolder는 지워지며, 실패 핸들러가 주도권, 사용자 URL 리다이렉트


// 인증 전 Authentication 객체 - principal: "user" / credentials: "pass" / authorities: "[]" / authenticated: false
// 인증 후 Authentication 객체 - principal: Object.UserDetails / credentials: - / authorities: "[ROLE_USER]" / authenticated: true


public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

  // AuthenticationFilter가 해당 경로의 POST 요청을 처리할 것임을 명시한다.
  public AuthenticationFilter() {
    super(new AntPathRequestMatcher("/api/authentications", "POST"));
  }

  // HttpServletRequest 안에서 Authentication (인증 정보 / 여기서는 token)를 추출 및 인증 매니저에게 전달하여 검증 절차 시작

  // AuthenticationManager는 인터페이스이며, 이 인터페이스의 구현체인 ProviderManager가
  // AuthenticationProvider의 구현체 리스트를 가진다. 그래서 전달된 Authentication을 가지고, AuthenticationProvider 구현체 중에 하나가 검증을 한다.
  
  // 가장 널리 사용되는 AuthenticationProvider는 DaoAuthenticationProvider로, UserDetailsService로 데이터베이스에서 같은 사용자 명을 갖는 UserDetails를 로드
  // Authentication으로 넘겨 받은 비밀 번호가 UserDetails에 있는 비밀번호와 일치하는지 검사하는데 PasswordEncoder를 활용, 일치하면 인증 성공, 아니면 실패
  
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException, IOException {

    log.debug("Processing login request");
    String requestBody = IOUtils.toString(request.getReader());                        // 요청 바디를 읽어, 문자열로 저장 (요청 바디는 JSON 형식일 것으로 예상)
    LoginRequest loginRequest = JsonUtils.toObject(requestBody, LoginRequest.class);   // 유틸리티 클래스를 통해, JSON 문자열을 LoginRequest 인스턴스로 변환
    if (loginRequest == null || loginRequest.isInvalid()) {
      throw new InsufficientAuthenticationException("Invalid authentication request");
    }

    // 여기서는 Authentication 인터페이스의 구현체인 UsernamePasswordAuthenticationToken 인스턴스를 먼저 생성하고, 
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password);
    // 이 인스턴스를 가지고, 실제 인증을 수행하기 위해 AuthenticationManager를 호출한다.
    return this.getAuthenticationManager().authenticate(token);
  }
  
  @Getter
  @Setter
  static class LoginRequest {
    private String username;
    private String password;

    public boolean isInvalid() {	// isBlack - 공백, null 등이 있는지 체크 (있으면 true)
      return StringUtils.isBlank(username) || StringUtils.isBlank(password);  
    }

  }
}
