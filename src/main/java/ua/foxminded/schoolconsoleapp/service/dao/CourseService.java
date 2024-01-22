package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.repository.CourseRepository;
import ua.foxminded.schoolconsoleapp.repository.StudentRepository;
import ua.foxminded.schoolconsoleapp.repository.exception.DataBaseSqlRuntimeException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;

  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    Long enrolled = courseRepository.checkStudentEnrolledInCourse(studentId, courseId);
    log.info("Checked if student with ID {} is enrolled in course with ID {}: {}", studentId,
        courseId, enrolled == 1);
    return enrolled == 1;
  }

  @Transactional
  public void enrollStudentToCourse(int studentId, String courseName) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(
            () -> new EntityNotFoundException(
                "Student with ID " + studentId
                    + " does not exist."));

    Course course = courseRepository.findByCourseName(courseName)
        .orElseThrow(() -> new DataBaseSqlRuntimeException(
            "Course with the name '" + courseName
                + "' does not exist."));

    if (courseRepository.checkStudentEnrolledInCourse(studentId, course.getId()) == 1) {
      throw new DataBaseSqlRuntimeException(
          "Student with ID " + studentId + " is already enrolled in the course '" + courseName
              + "'.");
    }

    student.getCourses().add(course);
    course.getStudents().add(student);

    studentRepository.save(student);
    courseRepository.save(course);

    log.info("Enrolled student with ID {} to course '{}'", studentId, courseName);
  }

  @Transactional
  public void removeStudentFromCourse(int studentId, String courseName) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(
            () -> new EntityNotFoundException(
                "Student with ID " + studentId
                    + " does not exist."));

    Course course = courseRepository.findByCourseName(courseName)
        .orElseThrow(() -> new DataBaseSqlRuntimeException(
            "Course with the name '" + courseName
                + "' does not exist."));

    if (courseRepository.checkStudentEnrolledInCourse(studentId, course.getId()) == 0) {
      throw new DataBaseSqlRuntimeException(
          "Student with ID " + studentId + " is not enrolled in the course '" + courseName + "'.");
    }

    student.getCourses().remove(course);
    course.getStudents().remove(student);

    studentRepository.save(student);
    courseRepository.save(course);

    log.info("Removed student with ID {} from course '{}'", studentId, courseName);
  }

  public Optional<Course> getCourseIdByName(String courseName) {
    log.info("Retrieving course ID by name: {}", courseName);
    return courseRepository.findByCourseName(courseName);
  }

  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    log.info("Retrieving courses for student with ID {}", studentId);
    return courseRepository.getEnrolledCoursesForStudent(studentId);
  }

  @Transactional
  public void addCourse(Course course) {
    log.info("Adding new course: {}", course);
    courseRepository.save(course);
  }

  public Optional<Course> getCourseById(Integer id) {
    log.info("Retrieving course by ID: {}", id);
    return courseRepository.findById(id);
  }

  public List<Course> getAllCourses() {
    log.info("Retrieving all courses");
    return courseRepository.findAll();
  }

  public List<Course> getAllCourses(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all courses with pagination: page {}, itemsPerPage {}", page,
        itemsPerPage);
    Pageable pageable = Pageable.ofSize(itemsPerPage).withPage(page - 1);

    return courseRepository.findAll(pageable).getContent();
  }

  @Transactional
  public void updateCourse(Course course) {
    log.info("Updating course: {}", course);
    courseRepository.save(course);
  }

  @Transactional
  public boolean deleteCourse(Integer id) {
    if (courseRepository.existsById(id)) {
      courseRepository.deleteById(id);
      log.info("Course with id {} was successfully deleted.", id);
      return true;
    } else {
      log.info("Course with id {} not found.", id);
      return false;
    }
  }

}
