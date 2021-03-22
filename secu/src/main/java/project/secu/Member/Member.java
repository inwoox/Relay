package project.demo.domain.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberPassword;

    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;

    @Column
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "member" ,fetch = FetchType.LAZY)
    private List<MemberAuthMap> memberAuthoritiesMappingList;


}
