package taskagile.domain.application;

import taskagile.domain.application.command.CreateTeamCommand;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.team.TeamId;
import taskagile.domain.model.user.UserId;

import java.util.List;

public interface TeamService {

  List<Team> findTeamsByUserId(UserId userId);

  Team createTeam(CreateTeamCommand command);

  Team findById(TeamId teamId);
}
