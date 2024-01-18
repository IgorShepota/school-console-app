package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.dao.exception.DataBaseSqlRuntimeException;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

  private final CourseDao courseDao;
  private final StudentDao studentDao;

  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    boolean enrolled = courseDao.checkStudentEnrolledInCourse(studentId, courseId);
    log.info("Checked if student with ID {} is enrolled in course with ID {}: {}", studentId,
        courseId, enrolled);
    return enrolled;
  }

  @Transactional
  public void enrollStudentToCourse(int studentId, String courseName) {
    Optional<Student> studentOpt = studentDao.findById(studentId);
    Optional<Course> courseOpt = courseDao.getCourseIdByName(courseName);

    if (!studentOpt.isPresent()) {
      throw new EntityNotFoundException("Student with ID " + studentId + " does not exist.");
    }

    if (!courseOpt.isPresent()) {
      throw new DataBaseSqlRuntimeException(
          "Course with the name '" + courseName + "' does not exist.");
    }

    Student student = studentOpt.get();
    Course course = courseOpt.get();

    if (student.getCourses().contains(course)) {
      throw new DataBaseSqlRuntimeException(
          "Student with ID " + studentId + " is already enrolled in the course '" + courseName
              + "'.");
    }

    courseDao.enrollStudentToCourse(student, course);

    log.info("Enrolled student with ID {} to course '{}'", studentId, courseName);
  }

  @Transactional
  public void removeStudentFromCourse(int studentId, String courseName) {
    Optional<Student> studentOpt = studentDao.findById(studentId);
    Optional<Course> courseOpt = courseDao.getCourseIdByName(courseName);

    if (!studentOpt.isPresent()) {
      throw new EntityNotFoundException("Student with ID " + studentId + " does not exist.");
    }

    if (!courseOpt.isPresent()) {
      throw new DataBaseSqlRuntimeException(
          "Course with the name '" + courseName + "' does not exist.");
    }

    Student student = studentOpt.get();
    Course course = courseOpt.get();

    if (!student.getCourses().contains(course)) {
      throw new DataBaseSqlRuntimeException(
          "Student with ID " + studentId + " is not enrolled in the course '" + courseName + "'.");
    }

    courseDao.removeStudentFromCourse(student, course);

    log.info("Removed student with ID {} from course '{}'", studentId, courseName);
  }

  public Optional<Course> getCourseIdByName(String courseName) {
    log.info("Retrieving course ID by name: {}", courseName);
    return courseDao.getCourseIdByName(courseName);
  }

  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    log.info("Retrieving courses for student with ID {}", studentId);
    return courseDao.getEnrolledCoursesForStudent(studentId);
  }

  @Transactional
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
    log.info("Retrieving all courses with pagination: page {}, itemsPerPage {}", page,
        itemsPerPage);
    return courseDao.findAll(page, itemsPerPage);
  }

  @Transactional
  public void updateCourse(Course course) {
    log.info("Updating course: {}", course);
    courseDao.update(course);
  }

  @Transactional
  public boolean deleteCourse(Integer id) {
    boolean isDeleted = courseDao.deleteById(id);
    log.info("Deleting course with ID {}: {}", id, isDeleted ? "success" : "failure");
    return isDeleted;
  }

}
