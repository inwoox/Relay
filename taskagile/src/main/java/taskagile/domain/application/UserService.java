package taskagile.domain.application;

import taskagile.domain.application.command.RegistrationCommand;
import taskagile.domain.model.user.RegistrationException;

public interface UserService {
  /** Register a new user with username, email address, and password.
   * @param command instance of <code>RegistrationCommand</code>
   * @throws RegistrationException when registration failed. **/
  void register(RegistrationCommand command) throws RegistrationException;
}
