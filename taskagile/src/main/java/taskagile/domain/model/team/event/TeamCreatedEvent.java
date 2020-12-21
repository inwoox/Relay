package taskagile.domain.model.team.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.common.event.TriggeredBy;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.team.TeamId;

public class TeamCreatedEvent extends DomainEvent {

  private static final long serialVersionUID = 2714833255396717504L;

  private TeamId teamId;
  private String teamName;

  public TeamCreatedEvent(Team team, TriggeredBy triggeredBy) {
    super(triggeredBy);
    this.teamId = team.getId();
    this.teamName = team.getName();
  }

  public TeamId getTeamId() {
    return teamId;
  }

  public String getTeamName() {
    return teamName;
  }

  @Override
  public String toString() {
    return "TeamCreatedEvent{" +
      "teamId=" + teamId +
      ", teamName='" + teamName + '\'' +
      '}';
  }
}
