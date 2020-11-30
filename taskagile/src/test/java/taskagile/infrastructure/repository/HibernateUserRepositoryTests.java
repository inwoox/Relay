package taskagile.infrastructure.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest				 // 오직 JPA 컴포넌트를 테스트하기 위한 용도 (기본적으로 임베디드 인 메모리 데이터베이스를 활용한다 / 여기서는 H2를 사용)
public class HibernateUserRepositoryTests {

  // HibernateUserRepository 인스턴스가 테스트 클래스에 자동으로 주입되도록, 내부 설정 클래스를 활용
  // UserRepo를 스프링 데이터 JPA 방식으로 사용하면, @DataJpaTest 어노테이션으로 인해 스프링이 자동으로 리포지토리의 구현체를 생성한 다음 인스턴스화하기 때문에, 설정 클래스 필요 X
  @TestConfiguration  		 
  public static class UserRepositoryTestContextConfiguration {
    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
      return new HibernateUserRepository(entityManager);
    }
  }

  // 위에서 생성한 인스턴스를 주입한다.
  @Autowired
  private UserRepository repository;

  // 이번 테스트에서는 목을 생성할 필요가 없다. 데이터베이스와의 모든 상호작용은 인 메모리 데이터베이스를 상대로 수행된다.
  @Test(expected = PersistenceException.class)
  public void save_nullUsernameUser_shouldFail() {
    User inavlidUser = User.create(null, "sunny@taskagile.com", "MyPassword!");
    repository.save(inavlidUser);
  }

  @Test(expected = PersistenceException.class)
  public void save_nullEmailAddressUser_shouldFail() {
    User inavlidUser = User.create("sunny", null, "MyPassword!");
    repository.save(inavlidUser);
  }

  @Test(expected = PersistenceException.class)
  public void save_nullPasswordUser_shouldFail() {
    User inavlidUser = User.create("sunny", "sunny@taskagile.com", null);
    repository.save(inavlidUser);
  }

  @Test
  public void save_validUser_shouldSuccess() {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    assertNotNull("New user's id should be generated", newUser.getId());
    assertNotNull("New user's created date should be generated", newUser.getCreatedDate());
    assertEquals(username, newUser.getUsername());
    assertEquals(emailAddress, newUser.getEmailAddress());
    assertEquals("", newUser.getFirstName());
    assertEquals("", newUser.getLastName());
  }

  @Test
  public void save_usernameAlreadyExist_shouldFail() {
    // Create already exist user
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User alreadyExist = User.create(username, emailAddress, "MyPassword!");
    repository.save(alreadyExist);

    try {
      User newUser = User.create(username, "new@taskagile.com", "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void save_emailAddressAlreadyExist_shouldFail() {
    // Create already exist user
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User alreadyExist = User.create(username, emailAddress, "MyPassword!");
    repository.save(alreadyExist);

    try {
      User newUser = User.create("new", emailAddress, "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
    String emailAddress = "sunny@taskagile.com";
    User user = repository.findByEmailAddress(emailAddress);
    assertNull("No user should by found", user);
  }

  @Test
  public void findByEmailAddress_exist_shouldReturnResult() {
    String emailAddress = "sunny@taskagile.com";
    String username = "sunny";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    User found = repository.findByEmailAddress(emailAddress);
    assertEquals("Username should match", username, found.getUsername());
  }

  @Test
  public void findByUsername_notExist_shouldReturnEmptyResult() {
    String username = "sunny";
    User user = repository.findByUsername(username);
    assertNull("No user should by found", user);
  }

  @Test
  public void findByUsername_exist_shouldReturnResult() {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    User found = repository.findByUsername(username);
    assertEquals("Email address should match", emailAddress, found.getEmailAddress());
  }
}
