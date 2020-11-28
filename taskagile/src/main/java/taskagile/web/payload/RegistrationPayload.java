package taskagile.web.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import taskagile.domain.application.commands.RegistrationCommand;

@Getter
@Setter
public class RegistrationPayload {  // 세개의 필드를 가진다. 

  // Size 어노테이션은 null 값을 유효한 값으로 간주한다. 때문에 @NotNull 어노테이션을 각 필드에 추가한다.
  @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
  @NotNull
  private String username;

  @Email(message = "Email address should be valid")
  @Size(max = 100, message = "Email address must not be more than 100 characters")
  @NotNull
  private String emailAddress;

  @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
  @NotNull
  private String password;

  public RegistrationCommand toCommand() {
    return new RegistrationCommand(this.username, this.emailAddress, this.password);
  }
}
