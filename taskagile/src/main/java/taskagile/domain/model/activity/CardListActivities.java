package taskagile.domain.model.activity;

import taskagile.domain.model.cardlist.event.CardListAddedEvent;

public class CardListActivities {

  public static Activity from(CardListAddedEvent event) {
    String detail = ActivityDetail.blank()
      .add("cardListId", event.getCardListId().value())
      .add("cardListName", event.getCardListName())
      .toJson();
    return Activity.from(event.getUserId(), event.getBoardId(), ActivityType.ADD_BOARD,
      detail, event.getIpAddress());
  }
}
