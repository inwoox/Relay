package taskagile.domain.model.user;

import org.springframework.stereotype.Component;

import taskagile.domain.common.security.PasswordEncryptor;


// 도메인 서비스 : 예를 들어 RegistrationManagement, 도메인 객체에 자연스럽게 어울리지 못하는 비즈니스 로직을 캡슐화,
// 여기에서 말하는 비즈니스 로직은 리포지토리에 포함되는 일반적인 CRUD 연산이 아니다.

// 이 클래스가 관련된 비즈니스 규칙을 캡슐화하기 때문에, register 메서드를 호출하는 클라이언트가 비즈니스 규칙에 대해 신경 쓸 필요가 없다.
// 나중에 회원 등록 프로세스에 더 많은 규칙을 추가해야할 때 이 클래스만 변경하면 된다.


// 애플리케이션 코어(비즈니스 로직)와 애플리케이션 코어의 클라이언트 사이에서 경계를 설정하는 애플리케이션 서비스는 인터페이스 (예: UserService)를 활용하는 것이 좋다.
// 컨트롤러가 실체 구현체(UserServiceImpl)를 직접 참조하는 것을 원하지 않기 때문이다. 직접 참조하면 구현체에 대한 확장성이 떨어진다.
// RegistrationManagement 같은 도메인 서비스는 애플리케이션 코어에서 경계를 선언할 필요가 없다.


// 이런 도메인 서비스는 , 도메인 객체에 자연스럽게 어울리지 못하는 비즈니스 로직을 캡슐화한다.
// 여기서는 PasswordEncryptor의 패스워드 암호화 같은 로직을 같이 담아서 캡슐화한다.

@Component
public class RegistrationManagement {

  private UserRepository repository;
  private PasswordEncryptor passwordEncryptor;

  public RegistrationManagement(UserRepository repository, PasswordEncryptor passwordEncryptor) {
    this.repository = repository;
    this.passwordEncryptor = passwordEncryptor;
  }

  
  // 회원가입의 비즈니스 로직 : 이미 존재하는 사용자, 이메일 주소 등록 불가 / 비밀번호 암호화 / 저장소에 사용자 저장
  public User register(String username, String emailAddress, String firstName, String lastName, String password)
    throws RegistrationException {
	
	// 유저 검색에, 인프라 서비스인 repository에 의존. 같은 사용자명 또는 이메일 주소를 가지는 다른 사용자가 있으면, 에러를 던진다.
	// 이 에러들은 Exception을 상속한 RegistrationException을 상속하여 구현한다.
    User existingUser = repository.findByUsername(username);
    if (existingUser != null) {
      throw new UsernameExistsException();
    }

    existingUser = repository.findByEmailAddress(emailAddress.toLowerCase());
    if (existingUser != null) {
      throw new EmailAddressExistsException();
    }

    String encryptedPassword = passwordEncryptor.encrypt(password);
    User newUser = User.create(username, emailAddress.toLowerCase(), firstName, lastName, encryptedPassword);
    repository.save(newUser);
    return newUser;
  }
}
