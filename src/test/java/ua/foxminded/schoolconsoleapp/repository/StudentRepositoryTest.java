package ua.foxminded.schoolconsoleapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = {"classpath:db/sql/test_tables.sql",
    "classpath:db/sql/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
class StudentRepositoryTest {

  @Container
  protected static final PostgreSQLContainer<?> postgresqlContainer =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("test-db")
          .withUsername("root")
          .withPassword("test");

  @Autowired
  private StudentRepository studentRepository;

  @Test
  void testFindStudentsByCourseNameShouldWorCorrectlyIfStudentsExist() {
    List<Student> students = studentRepository.findStudentsByCourseName("Mathematics");

    assertThat(students).hasSize(2);
    assertThat(students).extracting(Student::getFirstName)
        .containsExactlyInAnyOrder("Elizabeth", "Laura");
  }

}
