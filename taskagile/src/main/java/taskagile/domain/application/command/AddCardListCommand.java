package taskagile.domain.application.command;

import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.user.UserId;

public class AddCardListCommand {

  private UserId userId;
  private String name;
  private BoardId boardId;
  private int position;

  public AddCardListCommand(BoardId boardId, UserId userId, String name, int position) {
    this.boardId = boardId;
    this.userId = userId;
    this.name = name;
    this.position = position;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public UserId getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }
}
