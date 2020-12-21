package taskagile.domain.application.impl;

import taskagile.domain.application.CardListService;
import taskagile.domain.application.command.AddCardListCommand;
import taskagile.domain.application.command.ChangeCardListPositionsCommand;
import taskagile.domain.common.event.DomainEventPublisher;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.cardlist.CardList;
import taskagile.domain.model.cardlist.CardListRepository;
import taskagile.domain.model.cardlist.event.CardListAddedEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CardListServiceImpl implements CardListService {

  private CardListRepository cardListRepository;
  private DomainEventPublisher domainEventPublisher;

  public CardListServiceImpl(CardListRepository cardListRepository,
                             DomainEventPublisher domainEventPublisher) {
    this.cardListRepository = cardListRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  @Override
  public List<CardList> findByBoardId(BoardId boardId) {
    return cardListRepository.findByBoardId(boardId);
  }

  @Override
  public CardList addCardList(AddCardListCommand command) {
    CardList cardList = CardList.create(command.getBoardId(),
      command.getUserId(), command.getName(), command.getPosition());

    cardListRepository.save(cardList);
    domainEventPublisher.publish(new CardListAddedEvent(cardList, command));
    return cardList;
  }

  @Override
  public void changePositions(ChangeCardListPositionsCommand command) {
    cardListRepository.changePositions(command.getCardListPositions());
  }
}
