package taskagile.domain.application;

import taskagile.domain.application.command.RegistrationCommand;
import taskagile.domain.model.user.RegistrationException;

public interface UserService {

  /**
   * Register a new user with username, email address, and password.
   *
   * @param command instance of <code>RegistrationCommand</code>
   * @throws RegistrationException when registration failed. Possible reasons are:
   *                               1) Username already exists
   *                               2) Email address already exists.
   */
  void register(RegistrationCommand command) throws RegistrationException;
}
