package taskagile.web.api;

import taskagile.domain.application.CardListService;
import taskagile.domain.application.command.AddCardListCommand;
import taskagile.domain.application.command.ChangeCardListPositionsCommand;
import taskagile.domain.model.cardlist.CardList;
import taskagile.web.payload.AddCardListPayload;
import taskagile.web.payload.ChangeCardListPositionsPayload;
import taskagile.web.result.AddCardListResult;
import taskagile.web.result.ApiResult;
import taskagile.web.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CardListApiController extends AbstractBaseController {

  private CardListService cardListService;

  public CardListApiController(CardListService cardListService) {
    this.cardListService = cardListService;
  }

  @PostMapping("/api/card-lists")
  public ResponseEntity<ApiResult> addCardList(@RequestBody AddCardListPayload payload,
                                               HttpServletRequest request) {
    AddCardListCommand command = payload.toCommand();
    addTriggeredBy(command, request);

    CardList cardList = cardListService.addCardList(command);
    return AddCardListResult.build(cardList);
  }

  @PostMapping("/api/card-lists/positions")
  public ResponseEntity<ApiResult> changeCardListPositions(@RequestBody ChangeCardListPositionsPayload payload,
                                                           HttpServletRequest request) {
    ChangeCardListPositionsCommand command = payload.toCommand();
    addTriggeredBy(command, request);

    cardListService.changePositions(command);
    return Result.ok();
  }
}
