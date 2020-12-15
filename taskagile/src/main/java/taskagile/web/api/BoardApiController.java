package taskagile.web.api;

import taskagile.domain.application.BoardService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.board.Board;
import taskagile.domain.model.user.SimpleUser;
import taskagile.web.payload.CreateBoardPayload;
import taskagile.web.result.ApiResult;
import taskagile.web.result.CreateBoardResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BoardApiController {

  private BoardService boardService;

  public BoardApiController(BoardService boardService) {
    this.boardService = boardService;
  }

  // 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여,
  // CurrentUser 어노테이션을 만들고, 이 어노테이션을 사용하여 현재 사용자를 가져온다.

  // 스프링은 SimpleUser의 인스턴스를 메서드로 전달하여, 현재 사용자의 ID를 가져오는데 사용한다.
  // 그리고 CreateBoardPayload 인스턴스를 통해 전달받은 페이로드 정보를 가지고, CreateBoardCommand를 생성한다.

  // 서비스에 의해 보드가 생성되면 새로 생성된 보드의 정보를 프론트엔드로 다시 보내기 위해 RepsonseEntity를 생성한다.
  // ResponseEntity를 생성하기 위해 CreateBoardResult 클래스를 활용한다.
  @PostMapping("/api/boards")
  public ResponseEntity<ApiResult> createBoard(@RequestBody CreateBoardPayload payload,
      @CurrentUser SimpleUser currentUser) {
    Board board = boardService.createBoard(payload.toCommand(currentUser.getUserId()));
    return CreateBoardResult.build(board);
  }
}
