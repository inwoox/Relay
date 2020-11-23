package app.messages;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class); // Config를 포함해, 스프링 컨테이너 생성
    MessageService messageService = context.getBean(MessageService.class); // 컨테이너에서 , 빈의 인스턴스를 가져온다.
    messageService.save("Hello, Spring!"); // 메시지 서비스의 저장 메서드
  }
}
