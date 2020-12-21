package taskagile.web.api;

import taskagile.domain.application.BoardService;
import taskagile.domain.application.CardListService;
import taskagile.domain.application.CardService;
import taskagile.domain.application.TeamService;
import taskagile.domain.application.command.AddBoardMemberCommand;
import taskagile.domain.application.command.CreateBoardCommand;
import taskagile.domain.common.file.FileUrlCreator;
import taskagile.domain.model.board.Board;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.card.Card;
import taskagile.domain.model.cardlist.CardList;
import taskagile.domain.model.team.Team;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserNotFoundException;
import taskagile.web.payload.AddBoardMemberPayload;
import taskagile.web.payload.CreateBoardPayload;
import taskagile.web.result.ApiResult;
import taskagile.web.result.BoardResult;
import taskagile.web.result.CreateBoardResult;
import taskagile.web.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BoardApiController extends AbstractBaseController {

  private BoardService boardService;
  private TeamService teamService;
  private CardListService cardListService;
  private CardService cardService;
  private FileUrlCreator fileUrlCreator;

  public BoardApiController(BoardService boardService,
                            TeamService teamService,
                            CardListService cardListService,
                            CardService cardService,
                            FileUrlCreator fileUrlCreator) {
    this.boardService = boardService;
    this.teamService = teamService;
    this.cardListService = cardListService;
    this.cardService = cardService;
    this.fileUrlCreator = fileUrlCreator;
  }

  // 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여,
  // CurrentUser 어노테이션을 만들고, 이 어노테이션을 사용하여 현재 사용자를 가져온다.

  // 스프링은 SimpleUser의 인스턴스를 메서드로 전달하여, 현재 사용자의 ID를 가져오는데 사용한다.
  // 그리고 CreateBoardPayload 인스턴스를 통해 전달받은 페이로드 정보를 가지고, CreateBoardCommand를 생성한다.

  // 서비스에 의해 보드가 생성되면 새로 생성된 보드의 정보를 프론트엔드로 다시 보내기 위해 RepsonseEntity를 생성한다.
  // ResponseEntity를 생성하기 위해 CreateBoardResult 클래스를 활용한다.
  @PostMapping("/api/boards")
  public ResponseEntity<ApiResult> createBoard(@RequestBody CreateBoardPayload payload,
                                               HttpServletRequest request) {
    CreateBoardCommand command = payload.toCommand();
    addTriggeredBy(command, request);

    Board board = boardService.createBoard(command);
    return CreateBoardResult.build(board);
  }

  @GetMapping("/api/boards/{boardId}")
  public ResponseEntity<ApiResult> getBoard(@PathVariable("boardId") long rawBoardId) {
    BoardId boardId = new BoardId(rawBoardId);
    Board board = boardService.findById(boardId);
    if (board == null) {
      return Result.notFound();
    }
    List<User> members = boardService.findMembers(boardId);

    Team team = null;
    if (!board.isPersonal()) {
      team = teamService.findById(board.getTeamId());
    }

    List<CardList> cardLists = cardListService.findByBoardId(boardId);
    List<Card> cards = cardService.findByBoardId(boardId);

    return BoardResult.build(team, board, members, cardLists, cards, fileUrlCreator);
  }

  @PostMapping("/api/boards/{boardId}/members")
  public ResponseEntity<ApiResult> addMember(@PathVariable("boardId") long rawBoardId,
                                             @RequestBody AddBoardMemberPayload payload,
                                             HttpServletRequest request) {
    BoardId boardId = new BoardId(rawBoardId);
    Board board = boardService.findById(boardId);
    if (board == null) {
      return Result.notFound();
    }

    try {
      AddBoardMemberCommand command = payload.toCommand(boardId);
      addTriggeredBy(command, request);

      User member = boardService.addMember(command);

      ApiResult apiResult = ApiResult.blank()
        .add("id", member.getId().value())
        .add("shortName", member.getInitials());
      return Result.ok(apiResult);
    } catch (UserNotFoundException e) {
      return Result.failure("No user found");
    }
  }
}
