package taskagile.domain.model.team;

import taskagile.domain.model.user.UserId;

import java.util.List;

public interface TeamRepository {

  List<Team> findTeamsByUserId(UserId userId);

  void save(Team team);

  Team findById(TeamId teamId);
}
