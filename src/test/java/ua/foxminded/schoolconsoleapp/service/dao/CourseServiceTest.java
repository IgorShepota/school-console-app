package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.dao.exception.DataBaseSqlRuntimeException;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@SpringBootTest
class CourseServiceTest {

  @MockBean
  private CourseDao courseDao;

  @MockBean
  private StudentDao studentDao;

  @Autowired
  private CourseService courseService;

  @Test
  void checkStudentEnrolledInCourseShouldReturnTrueIfEnrolled() {
    int studentId = 1, courseId = 1;
    when(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).thenReturn(true);

    boolean enrolled = courseService.checkStudentEnrolledInCourse(studentId, courseId);

    assertThat(enrolled).isTrue();
  }

  @Test
  void enrollStudentToCourseShouldEnrollValidStudentAndCourse() {
    int studentId = 1;
    String courseName = "Mathematics";

    Student student = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .withCourses(new ArrayList<>())
        .build();

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .withStudents(new ArrayList<>())
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(course));

    courseService.enrollStudentToCourse(studentId, courseName);

    verify(courseDao).enrollStudentToCourse(student, course);
  }

  @Test
  void enrollStudentToCourseShouldThrowEntityNotFoundExceptionWhenInvalidStudentId() {
    int studentId = 10;
    String courseName = "Math";

    when(studentDao.findById(studentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .isInstanceOf(EntityNotFoundException.class);

    verify(courseDao, never()).enrollStudentToCourse(any(), any());
  }

  @Test
  void enrollStudentToCourseShouldThrowDataBaseSqlRuntimeExceptionWhenInvalidCourseName() {
    int studentId = 1;
    String courseName = "Mathe";

    Student student = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .withCourses(new ArrayList<>())
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class);

    verify(courseDao, never()).enrollStudentToCourse(any(), any());
  }

  @Test
  void enrollStudentToCourseShouldThrowExceptionWhenStudentAlreadyEnrolled() {
    int studentId = 1;
    String courseName = "Mathematics";

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .build();

    Student student = Student.builder()
        .withId(studentId)
        .withCourses(Collections.singletonList(course))
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(course));

    assertThatExceptionOfType(DataBaseSqlRuntimeException.class)
        .isThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .withMessage(
            "Student with ID " + studentId + " is already enrolled in the course '" + courseName
                + "'.");
  }

  @Test
  void removeStudentFromCourseShouldSucceedWithValidStudentIdAndCourseName() {
    int studentId = 1;
    String courseName = "Mathematics";

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .build();

    Student student = Student.builder()
        .withId(studentId)
        .withCourses(Collections.singletonList(course))
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(course));

    courseService.removeStudentFromCourse(studentId, courseName);

    verify(courseDao).removeStudentFromCourse(student, course);
  }

  @Test
  void removeStudentFromCourseShouldThrowEntityNotFoundExceptionForInvalidStudentId() {
    int studentId = 1;
    String courseName = "Math";

    when(studentDao.findById(studentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void removeStudentFromCourseShouldThrowDataBaseSqlRuntimeExceptionForInvalidCourseName() {
    int studentId = 1;
    String courseName = "Math";

    Student student = Student.builder().withId(studentId).build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class);
  }

  @Test
  void removeStudentFromCourseShouldThrowExceptionWhenStudentAlreadyNotEnrolled() {
    int studentId = 1;
    String courseName = "Mathematics";

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .build();

    Student student = Student.builder()
        .withId(studentId)
        .withCourses(Collections.emptyList())
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(course));

    assertThatExceptionOfType(DataBaseSqlRuntimeException.class)
        .isThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .withMessage(
            "Student with ID " + studentId + " is not enrolled in the course '" + courseName
                + "'.");
  }

  @Test
  void getCourseIdByNameShouldReturnCorrectDataIfExists() {
    String courseName = "Mathematics";
    Course mockCourse = Course.builder().withId(1).withCourseName(courseName).build();
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(mockCourse));

    Optional<Course> course = courseService.getCourseIdByName(courseName);

    assertThat(course).isPresent()
        .contains(mockCourse);
  }

  @Test
  void getEnrolledCoursesForStudentShouldReturnCorrectCourses() {
    int studentId = 1;
    List<Course> mockCourses = Arrays.asList(
        Course.builder().withId(1).withCourseName("Mathematics").build(),
        Course.builder().withId(2).withCourseName("Physics").build()
    );

    when(courseDao.getEnrolledCoursesForStudent(studentId)).thenReturn(mockCourses);

    List<Course> courses = courseService.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void addCourseShouldCallDaoSaveMethodWhenAdding() {
    Course course = Course.builder().withId(1).withCourseName("Mathematics").build();

    courseService.addCourse(course);

    verify(courseDao).save(course);
  }

  @Test
  void getCourseByIdShouldReturnCorrectDataIfExists() {
    Integer courseId = 1;
    Course mockCourse = Course.builder().withId(courseId).withCourseName("Mathematics").build();
    when(courseDao.findById(courseId)).thenReturn(Optional.of(mockCourse));

    Optional<Course> course = courseService.getCourseById(courseId);

    assertThat(course).isPresent()
        .contains(mockCourse);
  }

  @Test
  void getAllCoursesShouldReturnAllCoursesIfExists() {
    List<Course> mockCourses = Arrays.asList(
        Course.builder().withId(1).withCourseName("Mathematics").build(),
        Course.builder().withId(2).withCourseName("Physics").build()
    );

    when(courseDao.findAll()).thenReturn(mockCourses);

    List<Course> courses = courseService.getAllCourses();

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void getAllCoursesWithPaginationShouldReturnCorrectCoursesWhenPageGiven() {
    List<Course> mockCourses = Collections.singletonList(
        Course.builder().withId(1).withCourseName("Mathematics").build()
    );

    when(courseDao.findAll(1, 1)).thenReturn(mockCourses);

    List<Course> courses = courseService.getAllCourses(1, 1);

    assertThat(courses).hasSize(1).
        isEqualTo(mockCourses);
  }

  @Test
  void updateCourseShouldCallDaoUpdateMethodWhenUpdating() {
    Course course = Course.builder().withId(1).withCourseName("Mathematics").build();

    courseService.updateCourse(course);

    verify(courseDao).update(course);
  }

  @Test
  void deleteCourseShouldReturnTrueIfCourseDeleted() {
    Integer courseId = 1;
    when(courseDao.deleteById(courseId)).thenReturn(true);

    boolean result = courseService.deleteCourse(courseId);

    assertThat(result).isTrue();
  }

  @Test
  void deleteCourseShouldReturnFalseIfCourseNotDeleted() {
    Integer courseId = 999;
    when(courseDao.deleteById(courseId)).thenReturn(false);

    boolean result = courseService.deleteCourse(courseId);

    assertThat(result).isFalse();
  }

}
