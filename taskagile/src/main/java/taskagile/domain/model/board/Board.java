package taskagile.domain.model.board;

import taskagile.domain.common.model.AbstractBaseEntity;
import taskagile.domain.model.team.TeamId;
import taskagile.domain.model.user.UserId;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "board")
public class Board extends AbstractBaseEntity {

  private static final long serialVersionUID = -7789177855101967490L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 128, unique = true)
  private String name;

  @Column(name = "description", nullable = false, length = 256, unique = true)
  private String description;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "team_id") // 개인 보드는 어떤 팀에도 속하지 않기 때문에 teamId 필드를 Long 값으로 정의할 필요가 있다.
  private Long teamId;

  @Column(name = "archived")
  private boolean archived;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false)
  private Date createdDate;

  // 팩토리 메서드에서 보드를 생성한 사용자와 보드가 속하는 팀의 래핑된 ID를 전달한다.
  public static Board create(UserId userId, String name, String description, TeamId teamId) {
    Board board = new Board();
    board.userId = userId.value();
    board.name = name;
    board.description = description;
    // teamId가 0보다 크면, teamId를 넣고, 아닐 경우 null 값을 넣는다.
    board.teamId = teamId.isValid() ? teamId.value() : null; // 개인 보드를 생성할 경우 teamId는 null 값이다.
    board.archived = false;
    board.createdDate = new Date();
    return board;
  }

  public BoardId getId() {
    return new BoardId(id);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public UserId getUserId() {
    return new UserId(userId);
  }

  public TeamId getTeamId() {
    return teamId == null ? new TeamId(0) : new TeamId(teamId);
  }

  public boolean isPersonal() {
    return teamId == null;
  }

  public boolean isArchived() {
    return archived;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Board))
      return false;
    Board board = (Board) o;
    return userId == board.userId && teamId == board.teamId && Objects.equals(name, board.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, userId, teamId);
  }

  @Override
  public String toString() {
    return "Board{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", userId="
        + userId + ", teamId=" + teamId + ", archived=" + archived + ", createdDate=" + createdDate + '}';
  }
}
