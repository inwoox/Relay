package taskagile.domain.application.impl;

import taskagile.domain.application.TeamService;
import taskagile.domain.application.command.CreateTeamCommand;
import taskagile.domain.common.event.DomainEventPublisher;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.team.TeamId;
import taskagile.domain.model.team.TeamRepository;
import taskagile.domain.model.team.event.TeamCreatedEvent;
import taskagile.domain.model.user.UserId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

// 애플리케이션 서비스는 가능한 작게 유지하고, 이들이 비즈니스 로직을 포함하지 않게 한다.
// 비즈니스 로직은 도메인 모델과 도메인 서비스에 있어야한다. 

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

  private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

  private TeamRepository teamRepository;
  private DomainEventPublisher domainEventPublisher;

  public TeamServiceImpl(TeamRepository teamRepository, DomainEventPublisher domainEventPublisher) {
    this.teamRepository = teamRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  @Override
  public List<Team> findTeamsByUserId(UserId userId) {
    return teamRepository.findTeamsByUserId(userId);
  }

  // 오직 애플리케이션 서비스에서만 command를 사용한다. 커맨드는 도메인 모델 또는 도메인 서비스에서는 활용하지 않는다.
  // command는 애플리케이션 서비스의 API 계약의 일부이고, 나중에 변경될 수 있기 때문이다

  // 애플리케이션 서비스는 도메인에 의존해야하며, 반대는 X
  // 도메인이 애플리케이션 서비스에 의존하지 않기 위해서는 도메인에서 command를 쓰지 않는다.
  @Override
  public Team createTeam(CreateTeamCommand command) {
    Team team = Team.create(command.getName(), command.getUserId()); // 팀 생성, 저장, 생성 이벤트 발행
    teamRepository.save(team);                                       // 여기서는 Team 도메인 모델에 있는 create를 사용한다
    domainEventPublisher.publish(new TeamCreatedEvent(team, command));
    return team;
  }

  @Override
  public Team findById(TeamId teamId) {
    return teamRepository.findById(teamId);
  }
}
