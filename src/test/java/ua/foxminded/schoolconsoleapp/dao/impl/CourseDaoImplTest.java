package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.foxminded.schoolconsoleapp.dao.TestBase;
import ua.foxminded.schoolconsoleapp.dao.exception.DataBaseSqlRuntimeException;
import ua.foxminded.schoolconsoleapp.dao.mappers.CourseMapper;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@ExtendWith(MockitoExtension.class)
class CourseDaoImplTest extends TestBase {


  @Test
  @Tag("database")
  void checkStudentEnrolledInCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    int courseId = 1;

    assertThat(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).isTrue();
  }

  @Test
  void checkStudentEnrolledInCourseShouldThrowExceptionIfDatabaseErrorOccurs() {
    int studentId = 1;
    int courseId = 1;

    when(mockJdbcTemplate.queryForObject(anyString(), any(Class.class), anyInt(), anyInt()))
        .thenThrow(new DataIntegrityViolationException(
            "Error occurred while checking if the student with ID " + studentId
                + " is enrolled in course with ID " + courseId + "."));

    assertThatThrownBy(() -> mockCourseDao.checkStudentEnrolledInCourse(studentId, courseId))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Error occurred while checking if the student with ID " + studentId
            + " is enrolled in course with ID " + courseId + ".");
  }


  @Test
  @Tag("database")
  void enrollStudentToCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 3;
    int courseId = 1;

    courseDao.enrollStudentToCourse(studentId, courseId);

    assertThat(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).isTrue();
  }

  @Test
  void enrollStudentToCourseShouldThrowExceptionIfDatabaseErrorOccurs() {
    int studentId = 3;
    int courseId = 1;

    when(mockJdbcTemplate.update(anyString(), anyInt(), anyInt()))
        .thenThrow(new DataIntegrityViolationException(
            "Error occurred while adding student to the course."));

    assertThatThrownBy(() -> mockCourseDao.enrollStudentToCourse(studentId, courseId))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Error occurred while adding student to the course.");
  }

  @Test
  @Tag("database")
  void removeStudentFromCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    int courseId = 1;

    courseDao.removeStudentFromCourse(studentId, courseId);

    assertThat(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).isFalse();
  }

  @Test
  void removeStudentFromCourseShouldThrowExceptionIfDatabaseErrorOccurs() {
    int studentId = 1;
    int courseId = 1;

    when(mockJdbcTemplate.update(anyString(), anyInt(), anyInt()))
        .thenThrow(new DataIntegrityViolationException(
            "Error occurred while removing student from the course."));

    assertThatThrownBy(() -> mockCourseDao.removeStudentFromCourse(studentId, courseId))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Error occurred while removing student from the course.");
  }

  @Test
  @Tag("database")
  void getCourseIdByNameShouldWorkCorrectlyIfCourseExists() {
    String courseName = "Art";
    int courseId = 9;

    assertThat(courseDao.getCourseIdByName(courseName)).isPresent()
        .hasValueSatisfying(retrieved -> assertThat(retrieved.getId()).isEqualTo(courseId));
  }

  @Test
  @Tag("database")
  void getCourseIdByNameShouldWorkCorrectlyIfCourseNotExists() {
    String courseName = "Algebra";

    assertThat(courseDao.getCourseIdByName(courseName)).isNotPresent();
  }

  @Test
  void getCourseIdByNameShouldThrowSQLExceptionIfDatabaseErrorOccurs() {
    String courseName = "Art";

    when(mockJdbcTemplate.queryForObject(anyString(), any(CourseMapper.class), eq(courseName)))
        .thenThrow(new DataIntegrityViolationException("Error while getting course ID by name."));

    assertThatThrownBy(() -> mockCourseDao.getCourseIdByName(courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Error while getting course ID by name.");
  }

  @Test
  @Tag("database")
  void getEnrolledCoursesForStudentShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    List<Course> courses = courseDao.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).isNotEmpty()
        .hasSize(1)
        .allSatisfy(course -> assertThat(course.getCourseName()).isEqualTo("Mathematics"));
  }

  @Test
  void getEnrolledCoursesForStudentShouldThrowSQLExceptionIfDatabaseErrorOccurs() {
    int studentId = 1;

    when(mockJdbcTemplate.query(anyString(), any(CourseMapper.class), eq(studentId)))
        .thenThrow(new DataIntegrityViolationException(
            "Error while retrieving enrolled courses for student with ID " + studentId));

    assertThatThrownBy(() -> mockCourseDao.getEnrolledCoursesForStudent(studentId))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining(
            "Error while retrieving enrolled courses for student with ID " + studentId);
  }

  @Test
  @Tag("database")
  void saveShouldWorkCorrectlyWhileCourseEntityIsCorrect() {
    Course course = Course.builder()
        .withId(11)
        .withCourseName("Algebra")
        .build();

    courseDao.save(course);

    assertThat(courseDao.findById(course.getId())).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(course.getId());
          assertThat(retrieved.getCourseName()).isEqualTo(course.getCourseName());
        });
  }


  @Test
  @Tag("database")
  void findByIdShouldWorkCorrectlyIfCourseExists() {
    int courseIdToFind = 1;

    assertThat(courseDao.findById(courseIdToFind)).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(1);
          assertThat(retrieved.getCourseName()).isEqualTo("Mathematics");
        });
  }

  @Test
  void findByIdShouldWorkCorrectlyIfCourseNoFound() {
    int nonExistentId = 50;

    when(mockJdbcTemplate.queryForObject(anyString(), any(CourseMapper.class), eq(nonExistentId)))
        .thenThrow(new EmptyResultDataAccessException(1));

    Optional<Course> result = mockCourseDao.findById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  @Tag("database")
  void findAllShouldWorkCorrectlyIfCoursesExist() {
    List<Course> courses = courseDao.findAll();

    assertThat(courses).isNotEmpty()
        .hasSize(10)
        .allSatisfy(course -> {
          assertThat(course.getId()).isNotNull();
          assertThat(course.getCourseName()).isNotNull();
        });
  }

  @Test
  @Tag("database")
  void findAllWithPaginationShouldWorkCorrectlyIfStudentsExist() {
    List<Course> courses = courseDao.findAll(1, 2);

    assertThat(courses).isNotEmpty()
        .hasSize(2)
        .allMatch(course ->
            (course.getId() == 1 || course.getId() == 2) &&
                (course.getCourseName().equals("Mathematics") || course.getCourseName()
                    .equals("Biology"))
        );
  }

  @Test
  @Tag("database")
  void updateShouldWorkCorrectlyIfCourseExists() {
    Course course = Course.builder()
        .withId(1)
        .withCourseName("Algebra")
        .build();

    courseDao.update(course);

    assertThat(courseDao.findById(course.getId())).isPresent()
        .hasValueSatisfying(updatedCourse -> {
          assertThat(updatedCourse.getId()).isEqualTo(course.getId());
          assertThat(updatedCourse.getCourseName()).isEqualTo(course.getCourseName());
        });
  }

  @Test
  @Tag("database")
  void deleteShouldWorkCorrectlyIfCourseExists() {
    int courseId = 1;
    courseDao.deleteById(courseId);

    assertThat(courseDao.findById(courseId)).isNotPresent();
  }

  @Test
  @Tag("database")
  void deleteShouldWorkCorrectlyIfCourseNotExists() {
    int courseId = 50;

    assertThat(courseDao.deleteById(courseId)).isFalse();
  }

}
