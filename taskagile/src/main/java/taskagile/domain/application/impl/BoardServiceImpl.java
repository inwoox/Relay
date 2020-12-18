package taskagile.domain.application.impl;

import taskagile.domain.application.BoardService;
import taskagile.domain.application.command.CreateBoardCommand;
import taskagile.domain.common.event.DomainEventPublisher;
import taskagile.domain.model.board.*;
import taskagile.domain.model.board.event.BoardCreatedEvent;
import taskagile.domain.model.board.event.BoardMemberAddedEvent;
import taskagile.domain.model.user.User;
import taskagile.domain.model.user.UserFinder;
import taskagile.domain.model.user.UserId;
import taskagile.domain.model.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {
  private BoardRepository boardRepository;
  private BoardManagement boardManagement;
  private BoardMemberRepository boardMemberRepository;
  private UserFinder userFinder;
  private DomainEventPublisher domainEventPublisher;

  public BoardServiceImpl(BoardRepository boardRepository,
                          BoardManagement boardManagement,
                          BoardMemberRepository boardMemberRepository,
                          UserFinder userFinder,
                          DomainEventPublisher domainEventPublisher) {
    this.boardRepository = boardRepository;
    this.boardManagement = boardManagement;
    this.boardMemberRepository = boardMemberRepository;
    this.userFinder = userFinder;
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

  @Override
  public Board findById(BoardId boardId) {
    return boardRepository.findById(boardId);
  }

  @Override
  public List<User> findMembers(BoardId boardId) {
    return boardMemberRepository.findMembers(boardId);
  }

  @Override
  public User addMember(BoardId boardId, String usernameOrEmailAddress) throws UserNotFoundException {
    User user = userFinder.find(usernameOrEmailAddress);
    boardMemberRepository.add(boardId, user.getId());
    domainEventPublisher.publish(new BoardMemberAddedEvent(this, boardId, user));
    return user;
  }
}
