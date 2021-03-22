package project.securitydemo.Member;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_member_authorities_mapping")
@Getter
@Setter
@ToString
public class MemberAuthoritiesMapping {

    @Id
    @Column
    @GeneratedValue
    private long memberAuthMapSeq;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_seq", referencedColumnName = "memberSeq")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_auth_seq", referencedColumnName="memberAuthSeq")
    private MemberAuth memberAuth;

    @Column
    @CreationTimestamp
    private LocalDate registerDate;

}