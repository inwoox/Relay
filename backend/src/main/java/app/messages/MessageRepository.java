package app.messages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class MessageRepository {
  private final static Log log = LogFactory.getLog(MessageRepository.class);

  // 데이터베이스에 메시지를 저장
  public void saveMessage(Message message) {
    log.info("Saved message: " + message.getText());
  }
}
