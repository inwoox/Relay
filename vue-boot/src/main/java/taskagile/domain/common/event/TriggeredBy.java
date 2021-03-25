package taskagile.domain.common.event;

import taskagile.domain.model.user.UserId;
import taskagile.utils.IpAddress;

public interface TriggeredBy {
  UserId getUserId();
  IpAddress getIpAddress();
}
