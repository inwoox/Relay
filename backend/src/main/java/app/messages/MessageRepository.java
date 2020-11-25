package app.messages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageRepository {
  // log4j를 통한 로깅
  private final static Log log = LogFactory.getLog(MessageRepository.class);
  private SessionFactory sessionFactory;

  public MessageRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  // 하이버네이트 ORM (JPA 구현체) 사용 / spring-data-jpa를 사용하면 이마저도 필요 없다.
  public Message saveMessage(Message message) {
    // openSession()으로는 DataSource에서 JDBC 커넥션을 얻게 되므로, 오류시
    // 트랜잭션 어드바이저가 획득한 연결과 다르기 때문에, getCurrentSession()으로 변경
    Session session = sessionFactory.getCurrentSession(); // openSession()
    session.save(message);
    return message;

    // 스프링 JDBC 사용
    // JDBC API 위의 추상화 계층을 가지는 스프링 JDBC 사용 (jdbcTemplate)
    // KEY 생성 인스턴스 , 파라미터 담을 인스턴스, SQL, SQL 실행, 메시지 리턴

    // 저수준 JDBC API 사용하면, (JDBC 드라이버로 데이터베이스에 직접 연결)
    // dataSource를 통해 연결을 가져와, 그 연결을 가지고 SQL 실행
    // SQL에 영향을 받는 수를 체크하여, next() 메서드로 하나씩 넘기면서 메시지 생성
    // 연결을 닫아줄 코드들 작성

  }
}
