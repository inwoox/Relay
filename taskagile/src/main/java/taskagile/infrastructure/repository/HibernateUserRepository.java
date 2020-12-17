package taskagile.infrastructure.repository;

import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserId;
import taskagile.domain.model.user.UserRepository;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class HibernateUserRepository extends HibernateSupport<User> implements UserRepository {

  // HibernateSupport의 생성자를 호출하여, 엔티티 매니저를 주입 받는다.
  public HibernateUserRepository(EntityManager entityManager) {
    super(entityManager);
  }

  // 불필요하게 중복되는 코드가 존재할 수 있지만, 장점은 인프라 구현의 세부사항을 도메인 모델과 분리할 수 있다.
  @Override
  public User findByUsername(String username) {
    Query<User> query = getSession().createQuery("from User where username = :username", User.class);
    query.setParameter("username", username);
    return query.uniqueResult();
  }

  @Override
  public User findByEmailAddress(String emailAddress) {
    Query<User> query = getSession().createQuery("from User where emailAddress = :emailAddress", User.class);
    query.setParameter("emailAddress", emailAddress);
    return query.uniqueResult();
  }

  @Override
  public User findById(UserId userId) {
    Query<User> query = getSession().createQuery("from User where id = :id", User.class);
    query.setParameter("id", userId.value());
    return query.uniqueResult();
  }
}
