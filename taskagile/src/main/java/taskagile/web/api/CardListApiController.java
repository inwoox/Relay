package taskagile.web.api;

import taskagile.domain.application.CardListService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.cardlist.CardList;
import taskagile.domain.model.user.SimpleUser;
import taskagile.web.payload.AddCardListPayload;
import taskagile.web.payload.ChangeCardListPositionsPayload;
import taskagile.web.result.AddCardListResult;
import taskagile.web.result.ApiResult;
import taskagile.web.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CardListApiController {

  private CardListService cardListService;

  public CardListApiController(CardListService cardListService) {
    this.cardListService = cardListService;
  }

  @PostMapping("/api/card-lists")
  public ResponseEntity<ApiResult> addCardList(@RequestBody AddCardListPayload payload,
                                               @CurrentUser SimpleUser currentUser) {
    CardList cardList = cardListService.addCardList(payload.toCommand(currentUser.getUserId()));
    return AddCardListResult.build(cardList);
  }

  @PostMapping("/api/card-lists/positions")
  public ResponseEntity<ApiResult> changeCardListPositions(@RequestBody ChangeCardListPositionsPayload payload) {
    cardListService.changePositions(payload.toCommand());
    return Result.ok();
  }
}
