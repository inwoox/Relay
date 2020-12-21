package taskagile.domain.model.card.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.common.event.TriggeredBy;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.card.CardId;

public abstract class CardDomainEvent extends DomainEvent {

  private static final long serialVersionUID = 8301463735426628027L;

  private CardId cardId;
  private String cardTitle;
  private BoardId boardId;

  public CardDomainEvent(CardId cardId, String cardTitle, BoardId boardId, TriggeredBy triggeredBy) {
    super(triggeredBy);
    this.cardId = cardId;
    this.cardTitle = cardTitle;
    this.boardId = boardId;
  }

  public CardId getCardId() {
    return cardId;
  }

  public String getCardTitle() {
    return cardTitle;
  }

  public BoardId getBoardId() {
    return boardId;
  }
}
