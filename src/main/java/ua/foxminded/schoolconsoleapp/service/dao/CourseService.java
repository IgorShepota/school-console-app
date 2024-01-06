package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

  private final CourseDao courseDao;

  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    boolean enrolled = courseDao.checkStudentEnrolledInCourse(studentId, courseId);
    log.info("Checked if student with ID {} is enrolled in course with ID {}: {}", studentId, courseId, enrolled);
    return enrolled;
  }

  public void enrollStudentToCourse(int studentId, int courseId) {
    courseDao.enrollStudentToCourse(studentId, courseId);
    log.info("Enrolled student with ID {} to course with ID {}", studentId, courseId);
  }

  public void removeStudentFromCourse(int studentId, int courseId) {
    courseDao.removeStudentFromCourse(studentId, courseId);
    log.info("Removed student with ID {} from course with ID {}", studentId, courseId);
  }

  public Optional<Course> getCourseIdByName(String courseName) {
    log.info("Retrieving course ID by name: {}", courseName);
    return courseDao.getCourseIdByName(courseName);
  }

  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    log.info("Retrieving courses for student with ID {}", studentId);
    return courseDao.getEnrolledCoursesForStudent(studentId);
  }

  public void addCourse(Course course) {
    log.info("Adding new course: {}", course);
    courseDao.save(course);
  }

  public Optional<Course> getCourseById(Integer id) {
    log.info("Retrieving course by ID: {}", id);
    return courseDao.findById(id);
  }

  public List<Course> getAllCourses() {
    log.info("Retrieving all courses");
    return courseDao.findAll();
  }

  public List<Course> getAllCourses(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all courses with pagination: page {}, itemsPerPage {}", page, itemsPerPage);
    return courseDao.findAll(page, itemsPerPage);
  }

  public void updateCourse(Course course) {
    log.info("Updating course: {}", course);
    courseDao.update(course);
  }

  public boolean deleteCourse(Integer id) {
    boolean isDeleted = courseDao.deleteById(id);
    log.info("Deleting course with ID {}: {}", id, isDeleted ? "success" : "failure");
    return isDeleted;
  }

}
