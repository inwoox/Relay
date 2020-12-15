package taskagile.infrastructure.repository;

import taskagile.domain.model.team.Team;
import taskagile.domain.model.team.TeamRepository;
import taskagile.domain.model.user.UserId;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


// 보다시피 이 구현체는 findTeamsByUserId 메서드만 구현하고, HibernateSupport로부터 save() 메서드를 상속 받는다
// save를 따로 구현할 필요가 없다.
@Repository
public class HibernateTeamRepository extends HibernateSupport<Team> implements TeamRepository {

  HibernateTeamRepository(EntityManager entityManager) {
    super(entityManager);
  }

  @Override
  public List<Team> findTeamsByUserId(UserId userId) {
    String sql = " SELECT t.* FROM team t WHERE t.user_id = :userId " + " UNION " + " ( "
        + "   SELECT t.* FROM team t, board b, board_member bm "
        + "   WHERE t.id = b.team_id AND bm.board_id = b.id AND bm.user_id = :userId " + " ) ";
    NativeQuery<Team> query = getSession().createNativeQuery(sql, Team.class);
    query.setParameter("userId", userId.value());
    return query.list();
  }
}
