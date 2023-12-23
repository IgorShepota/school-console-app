package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.foxminded.schoolconsoleapp.dao.TestBase;
import ua.foxminded.schoolconsoleapp.dao.mappers.StudentMapper;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@ExtendWith(MockitoExtension.class)
public class StudentDaoImplTest extends TestBase {

  @Test
  @Tag("database")
  void findStudentsByCourseNameShouldWorkCorrectlyWhenStudentsExist() {
    List<Student> students = studentDao.findStudentsByCourseName("Mathematics");
    assertThat(students).isNotEmpty();
    assertThat(students).extracting("firstName").contains("Elizabeth", "Laura");
  }

  @Test
  @Tag("database")
  void findStudentsByCourseNameShouldReturnEmptyListWhenNoStudentsAssigned() {
    List<Student> students = studentDao.findStudentsByCourseName("NonExistingCourse");
    assertThat(students).isEmpty();
  }

  @Test
  @Tag("database")
  void saveShouldWorkCorrectlyIfStudentEntityCorrect() {
    Student student = Student.builder()
        .withId(4)
        .withGroupId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentDao.save(student);

    assertThat(studentDao.findById(student.getId())).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getFirstName()).isEqualTo(student.getFirstName());
          assertThat(retrieved.getLastName()).isEqualTo(student.getLastName());
          assertThat(retrieved.getGroupId()).isEqualTo(student.getGroupId());
        });
  }

  @Test
  void findByIdShouldReturnEmptyWhenNoStudentFound() {
    int nonExistentId = 999;
    when(mockJdbcTemplate.queryForObject(anyString(), any(StudentMapper.class), eq(nonExistentId)))
        .thenThrow(new EmptyResultDataAccessException(1));

    Optional<Student> result = mockStudentDao.findById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  @Tag("database")
  void findAllShouldWorkCorrectlyIfStudentsExist() {
    List<Student> students = studentDao.findAll();

    assertThat(students).isNotEmpty()
        .hasSize(3)
        .allSatisfy(student -> {
          assertThat(student.getId()).isNotNull();
          assertThat(student.getGroupId()).isNotNull();
          assertThat(student.getFirstName()).isNotNull();
          assertThat(student.getLastName()).isNotNull();
        });
  }

  @Test
  @Tag("database")
  void findAllWithPaginationShouldWorkCorrectlyIfStudentsExist() {
    List<Student> students = studentDao.findAll(1, 2);

    assertThat(students).isNotEmpty()
        .hasSize(2)
        .allSatisfy(student -> {
          assertThat(student.getId()).isNotNull();
          assertThat(student.getGroupId()).isNotNull();
          assertThat(student.getFirstName()).isNotNull();
          assertThat(student.getLastName()).isNotNull();
        });
  }

  @Test
  @Tag("database")
  void updateShouldWorkCorrectlyIfStudentExists() {
    Student student = Student.builder()
        .withId(1)
        .withGroupId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentDao.update(student);

    assertThat(studentDao.findById(student.getId())).isPresent()
        .hasValueSatisfying(updatedStudent -> {
          assertThat(updatedStudent.getId()).isEqualTo(student.getId());
          assertThat(updatedStudent.getGroupId()).isEqualTo(student.getGroupId());
          assertThat(updatedStudent.getFirstName()).isEqualTo(student.getFirstName());
          assertThat(updatedStudent.getLastName()).isEqualTo(student.getLastName());
        });
  }

  @Test
  @Tag("database")
  void deleteShouldWorkCorrectlyIfStudentExists() {
    int studentId = 1;

    assertThat(studentDao.deleteById(studentId)).isTrue();
  }

  @Test
  @Tag("database")
  void deleteShouldWorkCorrectlyIfStudentNotExists() {
    int studentId = 200;

    assertThat(studentDao.deleteById(studentId)).isFalse();
  }

}
