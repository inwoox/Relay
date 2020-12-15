package taskagile.web.api;

import taskagile.domain.application.BoardService;
import taskagile.domain.application.TeamService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.board.Board;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.user.SimpleUser;
import taskagile.web.result.ApiResult;
import taskagile.web.result.MyDataResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MeApiController {

  private TeamService teamService;
  private BoardService boardService;

  public MeApiController(TeamService teamService, BoardService boardService) {
    this.teamService = teamService;
    this.boardService = boardService;
  }

  // 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여,
  // CurrentUser 어노테이션을 만들고, 이 어노테이션을 사용하여 현재 사용자를 가져온다.

  // 현재 사용자가 접근할 수 있는 모든 보드와 팀을 가져와서, 현재 유저, 팀, 보드를 가지고 ResponseEntity를 만들어 프론트에 전달한다.
  @GetMapping("/api/me")
  public ResponseEntity<ApiResult> getMyData(@CurrentUser SimpleUser currentUser) {
    List<Team> teams = teamService.findTeamsByUserId(currentUser.getUserId());
    List<Board> boards = boardService.findBoardsByMembership(currentUser.getUserId());
    return MyDataResult.build(currentUser, teams, boards);
  }
}
