package taskagile.domain.model.board;

import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserId;

import java.util.List;

public interface BoardMemberRepository {

  void save(BoardMember boardMember);

  void add(BoardId boardId, UserId userId);

  List<User> findMembers(BoardId boardId);
}
