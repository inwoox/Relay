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
  // 세터 기반 / 메서드 기반 주입
  // @Required
  // public void setRepository(MessageRepository repo) {
  // this.messageRepo = repo;
  // }
  // @Autowired
  // public void prepare(MessageRepository repo) {
  // this.messageRepo = repo;
  // }
  // 필드 기반 주입
  // @Autowired
  // private MessageRepository repo;

  // 데이터베이스에 메시지를 저장하는 서비스
  public void save(String text) {
    this.messageRepo.saveMessage(new Message(text));
  }

}
