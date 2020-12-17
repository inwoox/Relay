package taskagile.web.api;

import taskagile.domain.application.CardService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.card.Card;
import taskagile.domain.model.user.SimpleUser;
import taskagile.web.payload.AddCardPayload;
import taskagile.web.payload.ChangeCardPositionsPayload;
import taskagile.web.result.AddCardResult;
import taskagile.web.result.ApiResult;
import taskagile.web.result.Result;
import taskagile.web.updater.CardUpdater;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CardApiController {

  private CardService cardService;
  private CardUpdater cardUpdater;

  public CardApiController(CardService cardService, CardUpdater cardUpdater) {
    this.cardService = cardService;
    this.cardUpdater = cardUpdater;
  }

  @PostMapping("/api/cards")
  public ResponseEntity<ApiResult> addCard(@RequestBody AddCardPayload payload,
                                           @CurrentUser SimpleUser currentUser) {
    Card card = cardService.addCard(payload.toCommand(currentUser.getUserId()));
    cardUpdater.onCardAdded(payload.getBoardId(), card);
    return AddCardResult.build(card);
  }

  @PostMapping("/api/cards/positions")
  public ResponseEntity<ApiResult> changeCardPositions(@RequestBody ChangeCardPositionsPayload payload) {
    cardService.changePositions(payload.toCommand());
    return Result.ok();
  }
}
