package ua.foxminded.schoolconsoleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = {"classpath:db/sql/test_tables.sql",
    "classpath:db/sql/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
class CourseRepositoryTest {

  @Container
  protected static final PostgreSQLContainer<?> postgresqlContainer =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("test-db")
          .withUsername("root")
          .withPassword("test");

  @Autowired
  private CourseRepository courseRepository;

  @Test
  void checkStudentEnrolledInCourseShouldReturnCorrectCountIfDataCorrect() {
    int studentId = 1;
    int courseId = 1;

    Long count = courseRepository.checkStudentEnrolledInCourse(studentId, courseId);

    assertThat(count).isNotNegative();
  }

  @Test
  void findByCourseNameShouldReturnCorrectCourseIfDataCorrect() {
    String courseName = "Mathematics";

    Optional<Course> course = courseRepository.findByCourseName(courseName);

    assertThat(course).isPresent();
    assertThat(course.get().getCourseName()).isEqualTo(courseName);
  }

  @Test
  void getEnrolledCoursesForStudentShouldReturnCorrectCoursesIfDataCorrect() {
    int studentId = 1;

    List<Course> courses = courseRepository.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).isNotEmpty();
  }

}
