package taskagile.domain.application;

import taskagile.domain.application.command.CreateBoardCommand;
import taskagile.domain.model.board.Board;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserId;
import taskagile.domain.model.user.UserNotFoundException;
import taskagile.domain.application.command.AddBoardMemberCommand;

import java.util.List;

public interface BoardService {
  List<Board> findBoardsByMembership(UserId userId);
  Board createBoard(CreateBoardCommand command);
  Board findById(BoardId boardId);
  List<User> findMembers(BoardId boardId);
  User addMember(AddBoardMemberCommand command) throws UserNotFoundException;
}
