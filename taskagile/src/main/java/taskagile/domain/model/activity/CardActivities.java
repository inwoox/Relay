package taskagile.domain.model.activity;

import taskagile.domain.model.attachment.event.CardAttachmentAddedEvent;
import taskagile.domain.model.card.Card;
import taskagile.domain.model.card.event.CardAddedEvent;
import taskagile.domain.model.card.event.CardDescriptionChangedEvent;
import taskagile.domain.model.card.event.CardTitleChangedEvent;
import taskagile.domain.model.user.UserId;
import taskagile.utils.IpAddress;

public class CardActivities {

  public static Activity from(Card card, UserId userId, String comment, IpAddress ipAddress) {
    String detail = ActivityDetail.blank()
      .add("comment", comment)
      .toJson();
    return Activity.from(userId, card.getId(), card.getBoardId(), ActivityType.ADD_COMMENT, detail, ipAddress);
  }

  public static Activity from(CardAddedEvent event) {
    String detail = ActivityDetail.blank()
      .add("cardTitle", event.getCardTitle())
      .toJson();
    return Activity.from(event.getUserId(), event.getCardId(), event.getBoardId(), ActivityType.ADD_CARD,
      detail, event.getIpAddress());
  }

  public static Activity from(CardAttachmentAddedEvent event) {
    String detail = ActivityDetail.blank()
      .add("cardTitle", event.getCardTitle())
      .add("attachmentId", event.getAttachmentId().value())
      .add("fileName", event.getFileName())
      .toJson();
    return Activity.from(event.getUserId(), event.getCardId(), event.getBoardId(), ActivityType.ADD_ATTACHMENT,
      detail, event.getIpAddress());
  }

  public static Activity from(CardDescriptionChangedEvent event) {
    String detail = ActivityDetail.blank()
      .add("cardTitle", event.getCardTitle())
      .add("newDescription", event.getNewDescription())
      .add("oldDescription", event.getOldDescription())
      .toJson();
    return Activity.from(event.getUserId(), event.getCardId(), event.getBoardId(), ActivityType.CHANGE_CARD_DESCRIPTION,
      detail, event.getIpAddress());
  }

  public static Activity from(CardTitleChangedEvent event) {
    String detail = ActivityDetail.blank()
      .add("newTitle", event.getNewTitle())
      .add("oldTitle", event.getOldTitle())
      .toJson();
    return Activity.from(event.getUserId(), event.getCardId(), event.getBoardId(), ActivityType.CHANGE_CARD_TITLE,
      detail, event.getIpAddress());
  }
}
