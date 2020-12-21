package taskagile.domain.common.event;

import taskagile.utils.IpAddress;

public interface TriggeredFrom {

  /**
   * Get the IP address where the request originated from
   *
   * @return an IP address
   */
  IpAddress getIpAddress();
}
