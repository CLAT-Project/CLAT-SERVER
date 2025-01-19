package team_project.clat.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team_project.clat.domain.Enum.UserStatus;
import team_project.clat.domain.Enum.UserType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본생성자 protected로
@Slf4j
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String email;
    private String password;
    private String schoolName;
    private String filePath;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    // 신고하기 와 양방향 맵핑
    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    List<Report> reportList = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    List<Message> messageList= new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Student_course> student_courseList = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<Answer> answerList = new ArrayList<>();

    @Builder
    public Member(Long id, String name, String username, String email, String password, String schoolName, String filePath, UserType userType, UserStatus userStatus) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.schoolName = schoolName;
        this.filePath = filePath;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public static Member memberSet(String name, String username, String password, String schoolName, UserType userType, String filePath, UserStatus userStatus){
        Member member = new Member();

        member.name = name;
        member.username = username;
        member.password = password;
        member.schoolName = schoolName;
        member.userType = userType;
        member.filePath = filePath;
        member.userStatus = userStatus;

        return member;
    }

    public static Member JwtMemberSet(String username, String password, String userType){
        Member member = new Member();

        member.username = username;
        member.password = password;
        member.userType = UserType.fromRole(userType);
        return member;
    }

    public void memberPasswordSet(String password){
        this.password=password;
    }

    public void memberUserStatusSet(UserStatus userStatus){
        this.userStatus = userStatus;
    }

    public void memberNameSet(String name){
        this.name = name;
    }

    public void memberEmailSet(String email){
        this.email = email;
    }

}
