package taskagile.web.payload;

import taskagile.domain.application.command.ChangeCardListPositionsCommand;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.cardlist.CardListPosition;

import java.util.List;

public class ChangeCardListPositionsPayload {

  private long boardId;
  private List<CardListPosition> cardListPositions;

  public ChangeCardListPositionsCommand toCommand() {
    return new ChangeCardListPositionsCommand(new BoardId(boardId), cardListPositions);
  }

  public void setBoardId(long boardId) {
    this.boardId = boardId;
  }

  public void setCardListPositions(List<CardListPosition> cardListPositions) {
    this.cardListPositions = cardListPositions;
  }
}
