package taskagile.domain.application;

import org.springframework.security.core.userdetails.UserDetailsService;

import taskagile.domain.application.command.RegisterCommand;
import taskagile.domain.model.user.RegistrationException;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserId;

// *** 인터페이스 ***

// 예를 들어 컨트롤러에서 UserService 인터페이스에 있는 메서드를 호출한다.
// 컨트롤러는 UserService가 어떻게 구현되어 있는지 알 필요가 없으며, 단지 호출하면 된다.
// UserService는 이 인터페이스를 구현하고, 빈으로 등록되어 인스턴스화된 UserService 구현체 클래스에게 처리를 위임한다.
// 이로써 느슨하게 결합된다.


// UsernamePasswordAuthenticationFilter의 인증 과정에서, 실제적으로 인증을 처리하는 AuthenticationProvider 구현체가
// UserDetailsService에게 UserDetails 인스턴스를 로드하라고 요청하며, UserDetails이 존재하면,
// Authentication 인스턴스에 있는 비밀번호(사용자가 입력한 비밀번호)와 UserDetails 인스턴스의 비밀번호 중에 일치하는 것이 있는지 
// PasswordEncoder를 활용하여 확인한다. (PasswordEncoder는 SecurityConfiguration 클래스에서 빈으로 등록하고, 회원 등록시 암호화 및 비밀번호 일치 확인시 복호화에 사용된다)
// 비밀번호가 일치하면 인증된 것으로 여긴다.


// UserDetailsService
// DB에서 유저 정보를 가져오는 역할을 한다.
// UserDetailsService 인터페이스에서 DB에서 유저정보를 불러오는 중요한 메소드는 loadUserByUsername() 메소드이다.
// loadUserByUsername()메소드에서 Custom으로 생성한 UserDetails 형태로 사용자의 정보를 받아오면 된다.

// UserDetails
// 사용자의 정보를 담는 인터페이스

public interface UserService extends UserDetailsService {
  void register(RegisterCommand command) throws RegistrationException;
  User findById(UserId userId);
}
