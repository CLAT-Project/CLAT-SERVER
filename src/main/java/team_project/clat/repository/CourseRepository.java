package team_project.clat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_project.clat.domain.Course;


public interface CourseRepository extends JpaRepository<Course, Long> {
}
