package ua.foxminded.schoolconsoleapp.dao;


import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolconsoleapp.entitу.Course;

public interface CourseDao extends CrudDao<Course> {

  boolean checkStudentEnrolledInCourse(int studentId, int courseId);

  void enrollStudentToCourse(int studentId, int courseId);

  void removeStudentFromCourse(int studentId, int courseId);

  Optional<Course> getCourseIdByName(String courseName);

  List<Course> getEnrolledCoursesForStudent(int studentId);
}
