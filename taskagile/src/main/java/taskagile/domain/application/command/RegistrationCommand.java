package taskagile.domain.application.command;
import org.springframework.util.Assert;

import lombok.Getter;

@Getter
public class RegistrationCommand {
  private String username;
  private String emailAddress;
  private String password;

  public RegistrationCommand(String username, String emailAddress, String password) {
    Assert.hasText(username, "Parameter `username` must not be empty");			// username이 빈 값이면, 메시지를 담은 에러를 발생시킨다.
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be empty");
    Assert.hasText(password, "Parameter `password` must not be empty");

    this.username = username;
    this.emailAddress = emailAddress;
    this.password = password;
  }

  // 테스트에서 모키토가 두 개의 (RegistrationPayload를 가지고 만든) RegistrationCommand 인스턴스를 비교할 때 두 메서드를 활용한다.
  // 두 메서드가 없으면 RegistrationCommand 인스턴스가 같은지 비교할 때 객체의 메모리 주소를 활용, 메모리 주소가 다르기 때문에 테스트는 실패
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistrationCommand that = (RegistrationCommand) o;
    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
    return password != null ? password.equals(that.password) : that.password == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RegistrationCommand{" +
      "username='" + username + '\'' +
      ", emailAddress='" + emailAddress + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}
