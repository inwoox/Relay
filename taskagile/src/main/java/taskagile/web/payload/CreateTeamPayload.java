package taskagile.web.payload;

import taskagile.domain.application.command.CreateTeamCommand;
import taskagile.domain.model.user.UserId;

public class CreateTeamPayload {

  private String name;

  public CreateTeamCommand toCommand(UserId userId) {
    return new CreateTeamCommand(userId, name);
  }

  public void setName(String name) {
    this.name = name;
  }
}
