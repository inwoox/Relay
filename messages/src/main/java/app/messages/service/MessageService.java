package app.messages.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.messages.model.Message;
import app.messages.repository.MessageRepository;
import app.messages.security.SecurityCheck;

@Component
public class MessageService {
  private MessageRepository messageRepo;
  private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

  // 생성자 기반 주입
  // @Autowired 애노테이션 생략 가능 (생략해도 알아서 Repo 빈을 찾아 Svc에 주입)
  // Msg Repo 빈은 Msg Svc 빈에 생성자를 통해 주입
  public MessageService(MessageRepository messageRepo) {
    this.messageRepo = messageRepo;
  }

  @Transactional(readOnly = true)
  public List<Message> getMessages() {
    return messageRepo.getMessages();
  }

  // 데이터베이스에 메시지를 저장하는 서비스
  @SecurityCheck
  @Transactional(noRollbackFor = { UnsupportedOperationException.class }) // 이 에러는 롤백하지 않음
  public Message save(String text) {
    logger.debug("[Transaction] {} - saved", text);
    return messageRepo.saveMessage(new Message(text));
  }
}
