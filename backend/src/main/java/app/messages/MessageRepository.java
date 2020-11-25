package app.messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageRepository {
  // log4j를 통한 로깅
  private final static Log log = LogFactory.getLog(MessageRepository.class);

  private DataSource dataSource;

  public MessageRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // 데이터베이스에 메시지를 저장
  // JDBC API 사용
  public Message saveMessage(Message message) {
    Connection c = DataSourceUtils.getConnection(dataSource);
    try {
      // INSERT SQL 실행
      String insertSql = "INSERT INTO messages (id, text, created_date) VALUE (null, ?, ?)";
      PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, message.getText());
      ps.setTimestamp(2, new Timestamp(message.getCreatedDate().getTime()));
      int rowsAffected = ps.executeUpdate(); // 데이터베이스에 SQL 실행하고, 영향을 받는 row의 수를 리턴 받음

      // INSERT SQL이 적용되는 row가 1개 이상일 경우, next() 메서드를 통해 하나씩 넘기면서 메시지 생성
      if (rowsAffected > 0) {
        ResultSet result = ps.getGeneratedKeys();
        if (result.next()) {
          int id = result.getInt(1);
          return new Message(id, message.getText(), message.getCreatedDate());
        } else {
          log.error("Failed to retrive id. No row in result set");
          return null;
        }
      } else {
        log.error("Insert Fail");
        return null;
      }
    } catch (Exception e) {
      log.error("Failed to save message", e);
      try {
        c.close();
      } catch (SQLException ex) {
        log.error("Failed to close connection", ex);
      } finally {
        DataSourceUtils.releaseConnection(c, dataSource);
      }
      return null;
    }
  }
}
