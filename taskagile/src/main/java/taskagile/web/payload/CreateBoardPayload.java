package taskagile.web.payload;

import taskagile.domain.application.command.CreateBoardCommand;
import taskagile.domain.model.team.TeamId;
import taskagile.domain.model.user.UserId;

public class CreateBoardPayload {

  private String name;
  private String description;
  private long teamId;

  public CreateBoardCommand toCommand() {
    return new CreateBoardCommand(name, description, new TeamId(teamId));
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

}
