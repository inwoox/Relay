package taskagile.web.api;

import taskagile.domain.application.TeamService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.user.SimpleUser;
import taskagile.web.payload.CreateTeamPayload;
import taskagile.web.result.ApiResult;
import taskagile.web.result.CreateTeamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TeamApiController {

  private static final Logger log = LoggerFactory.getLogger(MeApiController.class);

  private TeamService teamService;

  public TeamApiController(TeamService teamService) {
    this.teamService = teamService;
  }

  // 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여,
  // CurrentUser 어노테이션을 만들고, 이 어노테이션을 사용하여 현재 사용자를 가져온다.

  @PostMapping("/api/teams")
  public ResponseEntity<ApiResult> createTeam(@RequestBody CreateTeamPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Team team = teamService.createTeam(payload.toCommand(currentUser.getUserId()));
    return CreateTeamResult.build(team);
  }
}
