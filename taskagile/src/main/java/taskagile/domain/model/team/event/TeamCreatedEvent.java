package taskagile.domain.model.team.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.model.team.Team;

public class TeamCreatedEvent extends DomainEvent {

  private static final long serialVersionUID = 2714833255396717504L;

  private Team team;

  public TeamCreatedEvent(Object source, Team team) {
    super(source);
    this.team = team;
  }

  public Team getTeam() {
    return team;
  }
}
