package project.demo.domain.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member_auth")
@Getter
@Setter
@ToString
public class MemberAuth {

    @Id
    @Column
    @GeneratedValue
    private long memberAuthoritiesCodeSeq;

    @Column(nullable = false)
    private String authority;

    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;

    @OneToMany(mappedBy = "memberAuth" ,fetch = FetchType.LAZY)
    private List<MemberAuthMap> memberAuthoritiesMappingList = new ArrayList<>();
}
