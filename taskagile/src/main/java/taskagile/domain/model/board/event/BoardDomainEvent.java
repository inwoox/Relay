package taskagile.domain.model.board.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.common.event.TriggeredBy;
import taskagile.domain.model.board.BoardId;

public abstract class BoardDomainEvent extends DomainEvent {

  private static final long serialVersionUID = -147308556973863979L;

  private BoardId boardId;

  public BoardDomainEvent(BoardId boardId, TriggeredBy triggeredBy) {
    super(triggeredBy);
    this.boardId = boardId;
  }

  public BoardId getBoardId() {
    return boardId;
  }
}
