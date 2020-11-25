package app.messages;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageRepository {
  // log4j를 통한 로깅
  private final static Log log = LogFactory.getLog(MessageRepository.class);
  private NamedParameterJdbcTemplate jdbcTemplate;

  // NamedParameterJdbcTemplate를 사용하여, "?" 플레이어 홀더 대신 이름을 지정한 매개변수 사용
  public MessageRepository(DataSource dataSource) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  // 데이터베이스에 메시지를 저장
  // JDBC API 위의 추상화 계층을 가지는 스프링 JDBC 사용 (jdbcTemplate)
  // KEY 생성 인스턴스 , 파라미터 담을 인스턴스, SQL, SQL 실행, 메시지 리턴
  public Message saveMessage(Message message) {
    // KEY 생성
    GeneratedKeyHolder holder = new GeneratedKeyHolder();
    // SQL 파라미터
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("text", message.getText());
    params.addValue("createdDate", message.getCreatedDate());
    // SQL
    String insertSQL = "INSERT INTO messages (id, text, created_date) VALUE (null, :text, :createdDate)";
    try {
      // SQL 실행
      this.jdbcTemplate.update(insertSQL, params, holder);
    } catch (DataAccessException e) {
      log.info("Failed to save message", e);
      return null;
    }
    // 메시지 리턴
    return new Message(holder.getKey().intValue(), message.getText(), message.getCreatedDate());

    // 저수준 JDBC API 사용하면, (JDBC 드라이버로 데이터베이스에 직접 연결)
    // dataSource를 통해 연결을 가져와, 그 연결을 가지고 SQL 실행
    // SQL에 영향을 받는 수를 체크하여, next() 메서드로 하나씩 넘기면서 메시지 생성
    // 연결을 닫아줄 코드들 작성

  }
}
