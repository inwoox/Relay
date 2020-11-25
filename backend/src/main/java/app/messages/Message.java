package app.messages;

import lombok.*;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "messages")
@Getter
@AllArgsConstructor
@NoArgsConstructor // JPA 구현체가 데이터베이스에서 메시지를 가져올 때 객체를 재구성하므로, 기본 생성자가 필요
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "text", nullable = false, length = 128)
  private String text;

  @Column(name = "created_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Builder
  public Message(String text) {
    this.text = text;
    this.createdDate = new Date();
  }
}
