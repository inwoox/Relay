package project.demo.domain.Member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"memberAuthoritiesMappingList","memberAuthoritiesMappingList.memberAuthSeq"})
//	@Query(value =
//			"select m from MemberInfo m "
//			+ 	"join fetch m.memberAuthoritiesMappingList a "
//			+ 		"join fetch a.memberAuthoritiesCode "
//			+ "where m.memberId = :memberId" )
    Member findByMemberId(String memberId);

}