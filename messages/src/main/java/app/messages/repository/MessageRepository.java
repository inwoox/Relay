package app.messages.repository;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import app.messages.model.Message;

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
  }

  public List<Message> getMessages() {
    Session session = sessionFactory.getCurrentSession();
    String hql = "from Message"; // 하이버네이트는 이 HQL을 SQL로 번역하여 데이터베이스에 전달
    Query<Message> query = session.createQuery(hql, Message.class);
    return query.list();
  }
}
