package taskagile.web.payload;

import taskagile.domain.application.command.ChangeCardDescriptionCommand;
import taskagile.domain.model.card.CardId;

public class ChangeCardDescriptionPayload {

  private String description;

  public ChangeCardDescriptionCommand toCommand(long cardId) {
    return new ChangeCardDescriptionCommand(new CardId(cardId), description);
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
