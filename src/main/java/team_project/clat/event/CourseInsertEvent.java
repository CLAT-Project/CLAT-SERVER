package team_project.clat.event;

import org.springframework.context.ApplicationEvent;

public class CourseInsertEvent extends ApplicationEvent {
  private String courseCode;
  private String courseName;
  private String room;
  private String startDate;
  private String endDate;
  private String dayOfWeek;

  public CourseInsertEvent(Object source, String courseCode, String courseName, String room, String startDate, String endDate, String dayOfWeek) {
    super(source);
    this.courseCode = courseCode;
    this.courseName = courseName;
    this.room = room;
    this.startDate = startDate;
    this.endDate = endDate;
    this.dayOfWeek = dayOfWeek;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public String getCourseName() {
    return courseName;
  }

  public String getRoom() {
    return room;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }
}
