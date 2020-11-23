package app.messages;

public class MessageService {
  private MessageRepository messageRepo;

  // 데이터베이스를 주입하여, 서비스를 초기화
  public MessageService(MessageRepository messageRepo) {
    this.messageRepo = messageRepo;
  }

  // 데이터베이스에 메시지를 저장하는 서비스
  public void save(String text) {
    this.messageRepo.saveMessage(new Message(text));
  }

}
