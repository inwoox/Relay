package app.messages;

import org.springframework.stereotype.Component;

@Component
public class MessageService {
  private MessageRepository messageRepo;

  // 생성자 기반 주입
  // @Autowired 애노테이션 생략 가능 (생략해도 알아서 Repo 빈을 찾아 Svc에 주입)
  // Msg Repo 빈은 Msg Svc 빈에 생성자를 통해 주입
  public MessageService(MessageRepository messageRepo) {
    this.messageRepo = messageRepo;
  }

  // 데이터베이스에 메시지를 저장하는 서비스
  @SecurityCheck
  public Message save(String text) {
    return messageRepo.saveMessage(new Message(text));
  }

}
