package taskagile.web.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import taskagile.domain.application.command.RegisterCommand;

@Getter
@Setter
public class RegistrationPayload {  

  // Size 어노테이션은 null 값을 유효한 값으로 간주한다. 때문에 @NotNull 어노테이션을 각 필드에 추가한다.
  @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
  @NotNull
  private String username;

  @Email(message = "Email address should be valid")
  @Size(max = 100, message = "Email address must not be more than 100 characters")
  @NotNull
  private String emailAddress;

  @Size(min = 1, max = 45, message = "First name must be between 1 and 45 characters")
  @NotNull
  private String firstName;

  @Size(min = 1, max = 45, message = "Last name must be between 1 and 45 characters")
  @NotNull
  private String lastName;

  @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
  @NotNull
  private String password;

  public RegisterCommand toCommand() {	// RegistrationPayload에서 받은 값들을 가지고, RegistrationCommand 인스턴스를 생성한다
    return new RegisterCommand(this.username, this.emailAddress, this.firstName, this.lastName, this.password);
  }
}
