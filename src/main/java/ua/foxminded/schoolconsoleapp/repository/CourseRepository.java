package ua.foxminded.schoolconsoleapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.foxminded.schoolconsoleapp.entitу.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

  @Query("SELECT COUNT(c) FROM Course c JOIN c.students s WHERE s.id = :studentId AND c.id = :courseId")
  Long checkStudentEnrolledInCourse(int studentId, int courseId);

  Optional<Course> findByCourseName(String courseName);

  @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
  List<Course> getEnrolledCoursesForStudent(int studentId);

}
