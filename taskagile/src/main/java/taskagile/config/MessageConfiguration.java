package taskagile.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// AMQP의 구현체인 RabbitMQ를 활용해서 활동 추적을 구현한다.
// 새로운 도메인 이벤트 발행자인 AmqpdomainEventPublisher는 도메인 이벤트를 RabbitMQ의 익스체인지로 전송한다.

// AMQP 메시지 흐름
// 발행자 -> 익스체인지 -> 큐 -> 소비자

@Configuration
public class MessageConfiguration {

  // 익스체인지는 하나 또는 여러 개의 큐로 바인드 되는 팬-아웃 익스체인지
  // 이 메서드에서 ta.domain.events라는 영속성 FanoutExchange를 생성
  @Bean
  public FanoutExchange domainEventsExchange() {
    return new FanoutExchange("ta.domain.events", true, false);
  }

  // 익스체인지가 메시지를 받으면, 받은 메시지를 자신이 알고 있는 모든 큐로, 메시지를 브로드캐스트
  // 관련 큐를 리스닝하고 있는 소비자들은 메시지를 받는다.
  // 여기에서 ta.activity.tracking라는 영속성 큐를 생성
  @Bean
  public Queue activityTrackingQueue() {
    return new Queue("ta.activity.tracking", true);
  }

  // 여기에서 활동 추적 큐를 익스체인지에 바인드한다.
  @Bean
  public Binding bindingActivityTracking(FanoutExchange exchange, Queue activityTrackingQueue) {
    return BindingBuilder.bind(activityTrackingQueue).to(exchange);
  }
}
