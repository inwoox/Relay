package taskagile.domain.common.security;

import org.springframework.stereotype.Component;

// 실제 비밀 번호 암호화 로직이 다른 곳으로 위임됨을 나타낸다 (스프링 시큐리티의 PasswordEncoder로 암호화 로직이 위임된다)
@Component
public class PasswordEncryptorDelegator implements PasswordEncryptor {

  @Override
  public String encrypt(String rawPassword) {
    // TODO implement this
    return rawPassword;
  }
}
