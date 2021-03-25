package taskagile.domain.model.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import taskagile.domain.common.security.PasswordEncryptor;

public class RegistrationManagementTests {

  private UserRepository repositoryMock;
  private PasswordEncryptor passwordEncryptorMock;
  private RegistrationManagement instance;

  @Before // 테스트 전에 목을 생성하고, RegistrationManagement 인스턴스를 생성한다
  public void setUp() {
    repositoryMock = mock(UserRepository.class);
    passwordEncryptorMock = mock(PasswordEncryptor.class);
    instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
  }

  @Test(expected = UsernameExistsException.class) // 이 테스트는 이 에러가 발생해야한다.
  public void register_existedUsername_shouldFail() throws RegistrationException {
    String username = "existUsername";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    String firstName = "Existing";
    String lastName = "User";
    
    // 이미 존재하는 사용자임을 알려주고자 빈 객체를 반환한다, 하나의 유저를 리포에서 반환했으므로 에러가 발생 되어야한다.
    when(repositoryMock.findByUsername(username)).thenReturn(new User());
    instance.register(username, emailAddress, firstName, lastName, password);
  }

  @Test(expected = EmailAddressExistsException.class)
  public void register_existedEmailAddress_shouldFail() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "exist@taskagile.com";
    String password = "MyPassword!";
    String firstName = "Sunny";
    String lastName = "Hu";
    // We just return an empty user object to indicate an existing user
    when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(new User());
    instance.register(username, emailAddress, firstName, lastName, password);
  }

  @Test // 이메일 주소가 소문자로 잘 변환되는지 테스트한다.
  public void register_uppercaseEmailAddress_shouldSucceedAndBecomeLowercase() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "Sunny@TaskAgile.com";
    String password = "MyPassword!";
    String firstName = "Sunny";
    String lastName = "Hu";
    instance.register(username, emailAddress, firstName, lastName, password);
    User userToSave = User.create(username, emailAddress.toLowerCase(), firstName, lastName, password);
    verify(repositoryMock).save(userToSave);
  }

  @Test // 새로운 유저를 잘 등록하는지 테스트한다.
  public void register_newUser_shouldSucceed() throws RegistrationException {
	  // 새로운 유저 객체 생성
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    String encryptedPassword = "EncryptedPassword";
    String firstName = "Sunny";
    String lastName = "Hu";
    User newUser = User.create(username, emailAddress, firstName, lastName, encryptedPassword);

    // 목의 행동을 정의한다 / 사실 이렇게 하지 않아도, null을 반환할 것이다. 행동을 명시하기 위해 행동을 정의
    when(repositoryMock.findByUsername(username)).thenReturn(null);
    when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(null);
    doNothing().when(repositoryMock).save(newUser);
    
    // encrypt 메서드는 테스트에서 지정한 암호화된 비밀번호를 반환한다.
    when(passwordEncryptorMock.encrypt(password)).thenReturn("EncryptedPassword");

    // 유저 등록
    User savedUser = instance.register(username, emailAddress, firstName, lastName, password);
    
    // 목에 있는 메서드가 순서대로 호출되는지 검증하기 위해 InOrder API를 사용한다.
    InOrder inOrder = inOrder(repositoryMock);
    inOrder.verify(repositoryMock).findByUsername(username);
    inOrder.verify(repositoryMock).findByEmailAddress(emailAddress);
    inOrder.verify(repositoryMock).save(newUser);
    verify(passwordEncryptorMock).encrypt(password);
    
    // 마지막으로 저장된 비밀 번호가 평문이 아닌 암호화된 버전인지 검증
    assertEquals("Saved user's password should be encrypted", encryptedPassword, savedUser.getPassword());
  }
}
