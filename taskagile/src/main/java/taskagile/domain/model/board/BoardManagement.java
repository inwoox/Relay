package taskagile.domain.model.board;

import taskagile.domain.model.team.TeamId;
import taskagile.domain.model.user.UserId;
import org.springframework.stereotype.Component;


// Board와 BoardMember를 사용해야하기 때문에, 비즈니스 로직을 Board 도메인에 놓지 않고, 
// BoardManagement라는 도메인 서비스를 만들고 그 안에 비즈니스 로직을 캡슐화한다.

@Component
public class BoardManagement {

  private BoardRepository boardRepository;
  private BoardMemberRepository boardMemberRepository;

  public BoardManagement(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository) {
    this.boardRepository = boardRepository;
    this.boardMemberRepository = boardMemberRepository;
  }

  public Board createBoard(UserId creatorId, String name, String description, TeamId teamId) {
    Board board = Board.create(creatorId, name, description, teamId);
    boardRepository.save(board);
    
    // 보드 생성자를 보드의 멤버로 추가한다.
    BoardMember boardMember = BoardMember.create(board.getId(), creatorId);
    boardMemberRepository.save(boardMember);
    return board;
  }
}
