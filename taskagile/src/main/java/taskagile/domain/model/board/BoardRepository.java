package taskagile.domain.model.board;

import taskagile.domain.model.user.UserId;

import java.util.List;

public interface BoardRepository {

  List<Board> findBoardsByMembership(UserId userId);

  Board findById(BoardId boardId);

  void save(Board board);
}
