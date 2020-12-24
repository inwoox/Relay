package taskagile.infrastructure.messaging;

import taskagile.domain.application.ActivityService;
import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.model.activity.DomainEventToActivityConverter;
import taskagile.domain.model.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ActivityTracker {

  private final static Logger log = LoggerFactory.getLogger(ActivityTracker.class);

  private ActivityService activityService;
  private DomainEventToActivityConverter domainEventToActivityConverter;

  public ActivityTracker(ActivityService activityService,
                         DomainEventToActivityConverter domainEventToActivityConverter) {
    this.activityService = activityService;
    this.domainEventToActivityConverter = domainEventToActivityConverter;
  }

  
  @RabbitListener(queues = "#{activityTrackingQueue.name}") // 이 어노테이션을 통해, 스프링이 자동으로 수신된 RabbitMQ 메시지를 DomainEvent 오브젝트로 변환
  public void receive(DomainEvent domainEvent) {
    log.debug("Receive domain event: " + domainEvent);

    // domainEventToActivityConverter는 ActivityService가 활동을 저장할 수 있도록, 수신한 도메인 이벤트를, 대응하는 활동으로 변환해주는 변환기
    Activity activity = domainEventToActivityConverter.toActivity(domainEvent);
    // Save the activity only when there is an activity
    // result from the domain event
    if (activity != null) {
      activityService.saveActivity(activity);
    }
  }
}
