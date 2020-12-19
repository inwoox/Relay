package taskagile.web.socket.handlers;

import taskagile.web.socket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// 클라이언트에서 서버로 전송하는 메시지는 액션과 채널 두개의 매개변수를 가진다.

// 이 어노테이션을 클래스 단위로 적용하고, @Action 어노테이션을 요청을 처리할 메서드에 적용하면,
// /board/로 시작하는 채널 매개변수에 대응하는 메시지를 이 클래스에서 처리
@ChannelHandler("/board/*")
public class BoardChannelHandler {

  private static final Logger log = LoggerFactory.getLogger(BoardChannelHandler.class);

  // 액션이 subscribe, unsubscribe인 메시지를 각각의 메서드에서 처리
  @Action("subscribe")
  public void subscribe(RealTimeSession session, @ChannelValue String channel) {
    log.debug("RealTimeSession[{}] Subscribe to channel `{}`", session.id(), channel);
    SubscriptionHub.subscribe(session, channel);
  }

  @Action("unsubscribe")
  public void unsubscribe(RealTimeSession session, @ChannelValue String channel) {
    log.debug("RealTimeSession[{}] Unsubscribe from channel `{}`", session.id(), channel);
    SubscriptionHub.unsubscribe(session, channel);
  }
}
