package taskagile.domain.model.cardlist.events;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.model.cardlist.CardList;

public class CardListAddedEvent extends DomainEvent {

  private static final long serialVersionUID = -877934435476435188L;

  private CardList cardList;

  public CardListAddedEvent(Object source, CardList cardList) {
    super(source);
    this.cardList = cardList;
  }

  public CardList getCardList() {
    return cardList;
  }
}
