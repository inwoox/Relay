package taskagile.domain.common.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 실제 비밀 번호 암호화 로직이 다른 곳으로 위임됨을 나타낸다 (스프링 시큐리티의 PasswordEncoder로 암호화 로직이 위임된다)
@Component
public class PasswordEncryptorDelegator implements PasswordEncryptor {

	// SecurityConfiguration에 정의하여 구현한 PasswordEncoder 빈을 주입 받는다.
	// encrypt 메서드 호출시 주입받은 이 BCryptPasswordEncoder 빈을 사용해 암호화를 한다. 
	
	// 도메인서비스인 RegistrationManagement에서 passwordEncryptor.encrypt와 같이 암호화를 수행하는데,
	// 이때 passwordEncryptor 인터페이스의 구현체인 PasswordEncryptorDelegator의 encrypt를 수행하게 된다.
	
	// 그래서 결과적으로 주입 받은 BCryptPasswordEncoder 빈을 사용해 암호화를 한다. 
	// 패스워드가 암호화되면 길이가 길어지므로, 이것을 고려하여 user 테이블의 password column의 크기를 설정한다. 

    private PasswordEncoder passwordEncoder;
    public PasswordEncryptorDelegator(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

    @Override
    public String encrypt(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}
