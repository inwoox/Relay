package app.messages;

import lombok.*;
import java.util.Date;

@Getter
@AllArgsConstructor
public class Message {
  private Integer id;
  private String text;
  private Date createdDate;

  @Builder
  public Message(String text) {
    this.text = text;
    this.createdDate = new Date();
  }
}
