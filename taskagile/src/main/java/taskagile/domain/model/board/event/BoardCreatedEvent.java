package taskagile.domain.model.board.event;

import taskagile.domain.common.event.DomainEvent;
import taskagile.domain.model.board.Board;

public class BoardCreatedEvent extends DomainEvent {

  private static final long serialVersionUID = -8698981115023240376L;

  private Board board;

  public BoardCreatedEvent(Object source, Board board) {
    super(source);
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }
}
