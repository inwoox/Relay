package taskagile.web.payload;

import taskagile.domain.application.command.ChangeCardPositionsCommand;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.card.CardPosition;

import java.util.List;

public class ChangeCardPositionsPayload {

  private long boardId;
  private List<CardPosition> cardPositions;

  public ChangeCardPositionsCommand toCommand() {
    return new ChangeCardPositionsCommand(new BoardId(boardId), cardPositions);
  }

  public void setBoardId(long boardId) {
    this.boardId = boardId;
  }

  public void setCardPositions(List<CardPosition> cardPositions) {
    this.cardPositions = cardPositions;
  }
}
