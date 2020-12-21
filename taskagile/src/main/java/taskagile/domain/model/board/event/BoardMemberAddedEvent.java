package taskagile.domain.model.board.event;

import taskagile.domain.common.event.TriggeredBy;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserId;

public class BoardMemberAddedEvent extends BoardDomainEvent {

  private static final long serialVersionUID = -8979992986207557039L;

  private UserId memberUserId;
  private String memberName;

  public BoardMemberAddedEvent(BoardId boardId, User addedUser, TriggeredBy triggeredBy) {
    super(boardId, triggeredBy);
    this.memberUserId = addedUser.getId();
    this.memberName = addedUser.getFirstName() + " " + addedUser.getLastName();
  }

  public UserId getMemberUserId() {
    return memberUserId;
  }

  public String getMemberName() {
    return memberName;
  }

  @Override
  public String toString() {
    return "BoardMemberAddedEvent{" +
      "boardId=" + getBoardId() +
      ", memberUserId=" + memberUserId +
      ", memberName='" + memberName + '\'' +
      '}';
  }
}
