package taskagile.domain.model.user.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.common.event.TriggeredFrom;
import taskagile.domain.model.user.User;

public class UserRegisteredEvent extends DomainEvent {

  private static final long serialVersionUID = 2580061707540917880L;

  public UserRegisteredEvent(User user, TriggeredFrom triggeredFrom) {
    super(user.getId(), triggeredFrom);
  }

  @Override
  public String toString() {
    return "UserRegisteredEvent{userId=" + getUserId() + '}';
  }
}
