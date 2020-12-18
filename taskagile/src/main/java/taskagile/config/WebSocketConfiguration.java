package taskagile.config;

import taskagile.web.socket.WebSocketRequestDispatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


// 스프링의 웹소켓 구현체는 Tomcat, Jetty, Undertow와 같은 다른 웹소켓 런타임 위에서 하나의 추상 계층을 제공
// 스프링의 추상화 덕분에 웹소켓 서버를 생성하는 것은 WebSocketHandler 인터페이스를 구현하는 것만큼 간단하다.
// 또한 바로 상속해서 사용할 수 있는 TextWebSocketHandler, BinaryWebSocketHandler 구현체 클래스를 제공한다.


// WebSocketHandler를 구현하는 것 외에, 웹소켓 서버를 어떻게 부트스트랩할지 스프링이 알 수 있도록 환경설정을 제공해야한다.
// WebSocketConfigurer 인터페이스를 구현해서 환경설정을 할 수 있다


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  private WebSocketRequestDispatcher requestDispatcher;

  public WebSocketConfiguration(WebSocketRequestDispatcher requestDispatcher) {
    this.requestDispatcher = requestDispatcher;
  }

  // /rt 경로에 requestDispatcher를 등록하기 때문에, /rt 경로로 전송된 요청은 WebSocketRequestDispatcher가 처리한다.
  // SockJS는 웹소켓으로 클라이언트와 서버 간 실시간 연결을 제공한다.
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(requestDispatcher, "/rt").setAllowedOrigins("*").withSockJS();
  }
}
