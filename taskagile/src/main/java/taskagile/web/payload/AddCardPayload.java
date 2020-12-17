package taskagile.web.payload;

import taskagile.domain.application.command.AddCardCommand;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.cardlist.CardListId;
import taskagile.domain.model.user.UserId;

public class AddCardPayload {

  private long boardId;
  private long cardListId;
  private String title;
  private int position;

  public AddCardCommand toCommand(UserId userId) {
    return new AddCardCommand(new CardListId(cardListId), userId, title, position);
  }

  public BoardId getBoardId() {
    return new BoardId(boardId);
  }

  public void setBoardId(long boardId) {
    this.boardId = boardId;
  }

  public void setCardListId(long cardListId) {
    this.cardListId = cardListId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
