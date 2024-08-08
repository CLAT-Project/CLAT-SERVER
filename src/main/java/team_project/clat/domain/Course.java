package team_project.clat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    private String courseCode; //학수번호

    private String courseName;

    private String startDate;

    private String endDate;

    private String room; //강의실

    private String dayOfWeek; //시간표

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Student_course> studentCourseList = new ArrayList<>();


    @OneToOne(mappedBy = "course" , cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;



    public Course(String courseCode, String courseName, String room, String startDate, String endDate, String dayOfWeek) {
        this.courseCode = courseCode;
        this.dayOfWeek = dayOfWeek;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseName = courseName;
        this.room = room;
    }


    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.addCourse(this);
    }


}
