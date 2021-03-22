package project.securitydemo.Member;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class MemberInfo{


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
    private List<MemberAuthoritiesMapping> memberAuthoritiesMappingList;

}