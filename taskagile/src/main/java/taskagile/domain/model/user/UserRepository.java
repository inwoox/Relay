package taskagile.domain.model.user;


// 스프링 데이터 JPA를 사용하기 위해 @Repository / extends JpaRepository<User, Long>을 붙이면, 
// 스프링 데이터 JPA가 자동으로 인터페이스 구현체 (하이버네이트 구현체)를 생성하게 할 수 있다. / 하지만 리포지토리 인터페이스가 깔끔하기를 원하고, 
// 마이바티스 , 스프링 JDBC 같은 다른 구현체로의 전환이 자유롭고 싶다면 스프링 데이터 JPA를 사용하지 않고, 리포지토리를 그대로 유지한
// 그리고 인프라와 관련된 구현을 도메인 모델로부터 분리하기 위해, 이 인터페이스의 구현은 infrastructure.repository 패키지에서 구현한다.
public interface UserRepository {
  User findByUsername(String username);
  User findByEmailAddress(String emailAddress);
  void save(User user);
  User findById(UserId userId);
}
