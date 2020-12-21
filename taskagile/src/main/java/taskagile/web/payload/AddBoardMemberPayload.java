package taskagile.web.payload;

import taskagile.domain.application.command.AddBoardMemberCommand;
import taskagile.domain.model.board.BoardId;

public class AddBoardMemberPayload {

  private String usernameOrEmailAddress;

  public AddBoardMemberCommand toCommand(BoardId boardId) {
    return new AddBoardMemberCommand(boardId, usernameOrEmailAddress);
  }

  public void setUsernameOrEmailAddress(String usernameOrEmailAddress) {
    this.usernameOrEmailAddress = usernameOrEmailAddress;
  }
}
