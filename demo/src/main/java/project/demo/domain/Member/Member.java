package project.demo.domain.User;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;

@Getter
@Setter
public class Member {

    @Id
    @Column
    @GeneratedValue
    private long memberInfoSeq;

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

    @OneToMany(mappedBy = "memberInfo" ,fetch = FetchType.LAZY)
    private List<MemberAuthMap> memberAuthoritiesMappingList;
}
