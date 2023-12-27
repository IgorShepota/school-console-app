package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@Service
public class CourseService {

  private final CourseDao courseDao;

  @Autowired
  public CourseService(CourseDao courseDao) {
    this.courseDao = courseDao;
  }

  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    return courseDao.checkStudentEnrolledInCourse(studentId, courseId);
  }

  public void enrollStudentToCourse(int studentId, int courseId) {
    courseDao.enrollStudentToCourse(studentId, courseId);
  }

  public void removeStudentFromCourse(int studentId, int courseId) {
    courseDao.removeStudentFromCourse(studentId, courseId);
  }

  public Optional<Course> getCourseIdByName(String courseName) {
    return courseDao.getCourseIdByName(courseName);
  }

  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    return courseDao.getEnrolledCoursesForStudent(studentId);
  }

  public void addCourse(Course course) {
    courseDao.save(course);
  }

  public Optional<Course> getCourseById(Integer id) {
    return courseDao.findById(id);
  }

  public List<Course> getAllCourses() {
    return courseDao.findAll();
  }

  public List<Course> getAllCourses(Integer page, Integer itemsPerPage) {
    return courseDao.findAll(page, itemsPerPage);
  }

  public void updateCourse(Course course) {
    courseDao.update(course);
  }

  public boolean deleteCourse(Integer id) {
    return courseDao.deleteById(id);
  }

}
