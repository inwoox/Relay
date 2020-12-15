package taskagile.domain.application.impl;

import taskagile.domain.application.BoardService;
import taskagile.domain.application.command.CreateBoardCommand;
import taskagile.domain.common.event.DomainEventPublisher;
import taskagile.domain.model.board.Board;
import taskagile.domain.model.board.BoardManagement;
import taskagile.domain.model.board.BoardRepository;
import taskagile.domain.model.board.event.BoardCreatedEvent;
import taskagile.domain.model.user.UserId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

  private BoardRepository boardRepository;
  private BoardManagement boardManagement;
  private DomainEventPublisher domainEventPublisher;

  public BoardServiceImpl(BoardRepository boardRepository, BoardManagement boardManagement,
      DomainEventPublisher domainEventPublisher) {
    this.boardRepository = boardRepository;
    this.boardManagement = boardManagement;
    this.domainEventPublisher = domainEventPublisher;
  }

  @Override
  public List<Board> findBoardsByMembership(UserId userId) {
    return boardRepository.findBoardsByMembership(userId);
  }

  // 여기서는 boardManagement 도메인 서비스에 있는 createBoard를 사용한다.
  // 도메인 객체에 자연스럽게 어울리지 못하는 비즈니스 로직이 있을 때, 그것을 같이 담아 캡슐화하여 도메인 서비스로 만든다.
  @Override
  public Board createBoard(CreateBoardCommand command) { 
    Board board = boardManagement.createBoard(command.getUserId(), command.getName(), command.getDescription(),
        command.getTeamId());
    domainEventPublisher.publish(new BoardCreatedEvent(this, board));
    return board;
  }
}
