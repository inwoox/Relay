package taskagile.web.payload;

import taskagile.domain.application.command.ChangeCardTitleCommand;
import taskagile.domain.model.card.CardId;

public class ChangeCardTitlePayload {

  private String title;

  public ChangeCardTitleCommand toCommand(long cardId) {
    return new ChangeCardTitleCommand(new CardId(cardId), title);
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
