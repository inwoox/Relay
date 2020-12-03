package taskagile.domain.application.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import taskagile.domain.application.command.RegistrationCommand;
import taskagile.domain.common.event.DomainEventPublisher;
import taskagile.domain.common.mail.MailManager;
import taskagile.domain.common.mail.MessageVariable;
import taskagile.domain.model.user.EmailAddressExistsException;
import taskagile.domain.model.user.RegistrationException;
import taskagile.domain.model.user.RegistrationManagement;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserRepository;
import taskagile.domain.model.user.UsernameExistsException;
import taskagile.domain.model.user.event.UserRegisteredEvent;

public class UserServiceImplTests {

  private RegistrationManagement registrationManagementMock;
  private DomainEventPublisher domainEventPublisherMock;
  private MailManager mailManagerMock;
  private UserRepository userRepositoryMock;
  private UserServiceImpl instance;

  @Before
  public void setUp() {
    registrationManagementMock = mock(RegistrationManagement.class);
    domainEventPublisherMock = mock(DomainEventPublisher.class);
    mailManagerMock = mock(MailManager.class);
    userRepositoryMock = mock(UserRepository.class);
    instance = new UserServiceImpl(registrationManagementMock, domainEventPublisherMock, mailManagerMock, userRepositoryMock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void register_nullCommand_shouldFail() throws RegistrationException {
    instance.register(null);
  }

  @Test(expected = RegistrationException.class)
  public void register_existingUsername_shouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    doThrow(UsernameExistsException.class).when(registrationManagementMock)
      .register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);
  }

  @Test(expected = RegistrationException.class)
  public void register_existingEmailAddress_shouldFail() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "existing@taskagile.com";
    String password = "MyPassword!";
    doThrow(EmailAddressExistsException.class).when(registrationManagementMock)
      .register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    instance.register(command);
  }

  @Test
  public void register_validCommand_shouldSucceed() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    User newUser = User.create(username, emailAddress, password);
    when(registrationManagementMock.register(username, emailAddress, password))
      .thenReturn(newUser);
    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

    instance.register(command);

    verify(mailManagerMock).send(
      emailAddress,
      "Welcome to TaskAgile",
      "welcome.ftl",
      MessageVariable.from("user", newUser)
    );
    verify(domainEventPublisherMock).publish(new UserRegisteredEvent(newUser));
  }
}
