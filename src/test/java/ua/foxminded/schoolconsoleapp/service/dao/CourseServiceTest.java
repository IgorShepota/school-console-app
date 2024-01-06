package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@SpringBootTest
class CourseServiceTest {

  @MockBean
  private CourseDao courseDao;

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
  void enrollStudentToCourseShouldCallDaoMethodWhenEnrolling() {
    int studentId = 1, courseId = 1;

    courseService.enrollStudentToCourse(studentId, courseId);

    verify(courseDao).enrollStudentToCourse(studentId, courseId);
  }

  @Test
  void removeStudentFromCourseShouldCallDaoMethodWhenRemoving() {
    int studentId = 1, courseId = 1;

    courseService.removeStudentFromCourse(studentId, courseId);

    verify(courseDao).removeStudentFromCourse(studentId, courseId);
  }

  @Test
  void getCourseIdByNameShouldReturnCorrectDataIfExists() {
    String courseName = "Mathematics";
    Course mockCourse = Course.builder().id(1).courseName(courseName).build();
    when(courseDao.getCourseIdByName(courseName)).thenReturn(Optional.of(mockCourse));

    Optional<Course> course = courseService.getCourseIdByName(courseName);

    assertThat(course).isPresent()
        .contains(mockCourse);
  }

  @Test
  void getEnrolledCoursesForStudentShouldReturnCorrectCourses() {
    int studentId = 1;
    List<Course> mockCourses = Arrays.asList(
        Course.builder().id(1).courseName("Mathematics").build(),
        Course.builder().id(2).courseName("Physics").build()
    );

    when(courseDao.getEnrolledCoursesForStudent(studentId)).thenReturn(mockCourses);

    List<Course> courses = courseService.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void addCourseShouldCallDaoSaveMethodWhenAdding() {
    Course course = Course.builder().id(1).courseName("Mathematics").build();

    courseService.addCourse(course);

    verify(courseDao).save(course);
  }

  @Test
  void getCourseByIdShouldReturnCorrectDataIfExists() {
    Integer courseId = 1;
    Course mockCourse = Course.builder().id(courseId).courseName("Mathematics").build();
    when(courseDao.findById(courseId)).thenReturn(Optional.of(mockCourse));

    Optional<Course> course = courseService.getCourseById(courseId);

    assertThat(course).isPresent()
        .contains(mockCourse);
  }

  @Test
  void getAllCoursesShouldReturnAllCoursesIfExists() {
    List<Course> mockCourses = Arrays.asList(
        Course.builder().id(1).courseName("Mathematics").build(),
        Course.builder().id(2).courseName("Physics").build()
    );

    when(courseDao.findAll()).thenReturn(mockCourses);

    List<Course> courses = courseService.getAllCourses();

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void getAllCoursesWithPaginationShouldReturnCorrectCoursesWhenPageGiven() {
    List<Course> mockCourses = Collections.singletonList(
        Course.builder().id(1).courseName("Mathematics").build()
    );

    when(courseDao.findAll(1, 1)).thenReturn(mockCourses);

    List<Course> courses = courseService.getAllCourses(1, 1);

    assertThat(courses).hasSize(1).
        isEqualTo(mockCourses);
  }

  @Test
  void updateCourseShouldCallDaoUpdateMethodWhenUpdating() {
    Course course = Course.builder().id(1).courseName("Mathematics").build();

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
