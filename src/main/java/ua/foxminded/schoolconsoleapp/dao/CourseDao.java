package ua.foxminded.schoolconsoleapp.dao;


import java.util.List;
import java.util.Optional;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;

public interface CourseDao extends CrudDao<Course> {

  boolean checkStudentEnrolledInCourse(int studentId, int courseId);

  void enrollStudentToCourse(Student student, Course course);

  void removeStudentFromCourse(Student student, Course course);

  Optional<Course> getCourseIdByName(String courseName);

  List<Course> getEnrolledCoursesForStudent(int studentId);
}
