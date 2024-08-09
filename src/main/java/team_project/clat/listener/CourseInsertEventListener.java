package team_project.clat.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team_project.clat.domain.Course;
import team_project.clat.event.CourseInsertEvent;
import team_project.clat.repository.CourseRepository;

@Component
public class CourseInsertEventListener {

  @Autowired
  private CourseRepository courseRepository;

  @EventListener
  public void handleCourseCreatedEvent(CourseInsertEvent event) {
    Course course = new Course(
            event.getCourseCode(),
            event.getCourseName(),
            event.getRoom(),
            event.getStartDate(),
            event.getEndDate(),
            event.getDayOfWeek()
    );

    courseRepository.save(course);

    System.out.println("Course created with code: " + event.getCourseCode());
  }
}
