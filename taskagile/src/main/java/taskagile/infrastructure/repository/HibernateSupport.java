package taskagile.infrastructure.repository;
import javax.persistence.EntityManager;

import org.hibernate.Session;


// 이 어플리케이션에서 하이버네이트는 리포지토리의 핵심 구현체 (대부분 리포지토리 인터페이스의 구현체에서 하이버네이트 API를 활용한다)

// 엔티티매니저에서 하이버네이트 Session을 가져오는 추상 기반 클래스를 만들고, 이 클래스를 HibernateUserRepository (구현체) 클래스에서 상속한다.
abstract class HibernateSupport {

  // 스프링 데이터 JPA가 JPA 인터페이스 중 하나인 엔티티 매니저를 인스턴스화하여 제공한다.
  EntityManager entityManager;
  
  HibernateSupport(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  
  // 이 메서드를 이용해 하이버네이트의 Session 인스턴스를 가져온다.
  Session getSession() {
    return entityManager.unwrap(Session.class);
  }

}