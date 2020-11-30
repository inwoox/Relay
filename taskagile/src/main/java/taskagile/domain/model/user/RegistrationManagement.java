package taskagile.domain.model.user;

import org.springframework.stereotype.Component;

import taskagile.domain.common.security.PasswordEncryptor;


// 도메인 서비스 : 예를 들어 RegistrationManagement, 도메인 객체에 자연스럽게 어울리지 못하는 비즈니스 로직을 캡슐화,
// 여기에서 말하는 비즈니스 로직은 리포지토리에 포함되는 일반적인 CRUD 연산이 아니다.

@Component
public class RegistrationManagement {

  private UserRepository repository;
  private PasswordEncryptor passwordEncryptor;

  public RegistrationManagement(UserRepository repository, PasswordEncryptor passwordEncryptor) {
    this.repository = repository;
    this.passwordEncryptor = passwordEncryptor;
  }

  // 도메인 객체에 자연스럽게 어울리지 못하는 비즈니스 로직
  // 회원가입의 비즈니스 로직 : 이미 존재하는 사용자, 이메일 주소 등록 불가 / 비밀번호 암호화 / 저장소에 사용자 저장
  public User register(String username, String emailAddress, String password) throws RegistrationException {
	
	// 사용자를 찾는데, 인프라 서비스에 의존한다.
    User existingUser = repository.findByUsername(username);
    if (existingUser != null) {
      throw new UsernameExistsException();
    }

    existingUser = repository.findByEmailAddress(emailAddress.toLowerCase());
    if (existingUser != null) {
      throw new EmailAddressExistsException();
    }

    String encryptedPassword = passwordEncryptor.encrypt(password);
    User newUser = User.create(username, emailAddress.toLowerCase(), encryptedPassword);
    repository.save(newUser);
    return newUser;
  }
}
