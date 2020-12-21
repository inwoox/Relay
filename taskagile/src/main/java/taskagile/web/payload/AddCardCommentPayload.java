package taskagile.web.payload;

import taskagile.domain.application.command.AddCardCommentCommand;
import taskagile.domain.model.card.CardId;

public class AddCardCommentPayload {

  private String comment;

  public AddCardCommentCommand toCommand(CardId cardId) {
    return new AddCardCommentCommand(cardId, comment);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
