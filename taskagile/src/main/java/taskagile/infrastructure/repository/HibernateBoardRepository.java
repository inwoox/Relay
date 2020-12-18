package taskagile.infrastructure.repository;

import taskagile.domain.model.board.Board;
import taskagile.domain.model.board.BoardId;
import taskagile.domain.model.board.BoardRepository;
import taskagile.domain.model.user.UserId;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class HibernateBoardRepository extends HibernateSupport<Board> implements BoardRepository {

  HibernateBoardRepository(EntityManager entityManager) {
    super(entityManager);
  }

  // board 테이블과 board_member 테이블 간에 LEFT JOIN 오퍼레이션을 수행해야한다.
  // 왜냐하면 Board 엔티티와 User 엔티티 간 연관 관계를 만들기 위해 @ManyToMany 어노테이션을 활용하지 않았기 때문이다
  // 대신 보드 멤버십 정보를 저장하기 위해 BoardMember 엔티티를 생성한다.
  @Override
  public List<Board> findBoardsByMembership(UserId userId) {
    
    // board (b) 테이블과 board_member (bm) 테이블을 LEFT OUTER JOIN
    // b을 중심으로 , bm을 결합한다 (b * bm 개념) / 결합 조건은 ON 절의 id가 동일한 항목이며 / 그 중에 WHERE 절의 조건에 맞는 항목을 찾는다.
    String sql = "SELECT b.* FROM board b LEFT JOIN board_member bm ON b.id = bm.board_id WHERE bm.user_id = :userId";
    NativeQuery<Board> query = getSession().createNativeQuery(sql, Board.class);
    query.setParameter("userId", userId.value());
    return query.list();
  }

  @Override
  public Board findById(BoardId boardId) {
    Query<Board> query = getSession().createQuery("from Board where id = :id", Board.class);
    query.setParameter("id", boardId.value());
    return query.uniqueResult();
  }

}
