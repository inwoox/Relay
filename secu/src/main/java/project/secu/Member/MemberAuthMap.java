package project.demo.domain.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="member_auth_map")
@Getter
@Setter
@ToString
public class MemberAuthMap {

    @Id
    @Column
    @GeneratedValue
    private long memberAuthoritiesMappingSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq", referencedColumnName = "memberSeq")
    private Member memberSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_auth_seq", referencedColumnName="memberAuthSeq")
    private MemberAuth memberAuthSeq;

    @Column
    @CreationTimestamp
    private LocalDate registerDate;

}