package taskagile.domain.application;

import taskagile.domain.application.command.*;
import taskagile.domain.model.activity.Activity;
import taskagile.domain.model.attachment.Attachment;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.card.Card;
import taskagile.domain.model.card.CardId;

import java.util.List;

public interface CardService {

  List<Card> findByBoardId(BoardId boardId);

  Card findById(CardId cardId);

  List<Activity> findCardActivities(CardId cardId);

  List<Attachment> getAttachments(CardId cardId);

  Card addCard(AddCardCommand command);

  void changePositions(ChangeCardPositionsCommand command);

  void changeCardTitle(ChangeCardTitleCommand command);

  void changeCardDescription(ChangeCardDescriptionCommand command);

  Activity addComment(AddCardCommentCommand command);

  Attachment addAttachment(AddCardAttachmentCommand command);

}
