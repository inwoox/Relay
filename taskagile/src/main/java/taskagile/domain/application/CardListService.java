package taskagile.domain.application;

import taskagile.domain.application.command.AddCardListCommand;
import taskagile.domain.application.command.ChangeCardListPositionsCommand;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.cardlist.CardList;

import java.util.List;

public interface CardListService {

  /**
   * Find card lists of a board
   *
   * @param boardId id of the board
   * @return a list of card list instance or an empty list if none found
   */
  List<CardList> findByBoardId(BoardId boardId);

  /**
   * Add card list
   *
   * @param command the command instance
   * @return the newly added card list
   */
  CardList addCardList(AddCardListCommand command);

  /**
   * Change card list positions
   *
   * @param command the command instance
   */
  void changePositions(ChangeCardListPositionsCommand command);
}
