package taskagile.domain.model.card;
import taskagile.domain.model.board.BoardId;
import java.util.List;

public interface CardRepository {

  Card findById(CardId cardId);
  List<Card> findByBoardId(BoardId boardId);
  void save(Card card);
  void changePositions(List<CardPosition> cardPositions);
}
