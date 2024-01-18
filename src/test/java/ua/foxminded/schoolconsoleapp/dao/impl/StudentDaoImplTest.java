package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.DaoTestBase;
import ua.foxminded.schoolconsoleapp.entitу.Group;
import ua.foxminded.schoolconsoleapp.entitу.Student;

class StudentDaoImplTest extends DaoTestBase {

  @Test
  void findStudentsByCourseNameShouldWorkCorrectlyWhenStudentsExist() {
    List<Student> students = studentDao.findStudentsByCourseName("Mathematics");

    assertThat(students).isNotNull()
        .hasSize(2);
  }

  @Test
  void findStudentsByCourseNameShouldReturnEmptyListWhenNoStudentsAssigned() {
    List<Student> students = studentDao.findStudentsByCourseName("Chemistry");

    assertThat(students).isNotNull()
        .isEmpty();
  }

  @Test
  void saveShouldWorkCorrectlyIfStudentEntityCorrect() {
    Group group = entityManager.find(Group.class, 1);

    Student student = Student.builder()
        .withOwnerGroup(group)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentDao.save(student);

    assertThat(studentDao.findById(student.getId())).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(student.getId());
          assertThat(retrieved.getFirstName()).isEqualTo(student.getFirstName());
          assertThat(retrieved.getLastName()).isEqualTo(student.getLastName());
          assertThat(retrieved.getOwnerGroup()).isEqualTo(group);
        });
  }

  @Test
  void findByIdShouldWorkCorrectlyIfStudentExists() {
    int nonExistentId = 1;

    assertThat(studentDao.findById(nonExistentId)).isPresent();
  }

  @Test
  void findByIdShouldReturnEmptyWhenNoStudentFound() {
    int nonExistentId = 999;

    assertThat(studentDao.findById(nonExistentId)).isEmpty();
  }

  @Test
  void findAllShouldWorkCorrectlyIfStudentsExist() {
    List<Student> students = studentDao.findAll();

    assertThat(students).isNotEmpty()
        .hasSize(3)
        .allSatisfy(student -> {
          assertThat(student.getId()).isNotNull();
          assertThat(student.getOwnerGroup()).isNotNull();
          assertThat(student.getFirstName()).isNotNull();
          assertThat(student.getLastName()).isNotNull();
        });
  }

  @Test
  void findAllWithPaginationShouldWorkCorrectlyIfStudentsExist() {
    List<Student> students = studentDao.findAll(1, 2);

    assertThat(students).isNotEmpty()
        .hasSize(2)
        .allSatisfy(student -> {
          assertThat(student.getId()).isNotNull();
          assertThat(student.getOwnerGroup()).isNotNull();
          assertThat(student.getFirstName()).isNotNull();
          assertThat(student.getLastName()).isNotNull();
        });
  }

  @Test
  void updateShouldWorkCorrectlyIfStudentExists() {
    Group group = entityManager.find(Group.class, 1);

    Student student = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .withOwnerGroup(group)
        .build();

    studentDao.update(student);

    assertThat(studentDao.findById(student.getId())).isPresent()
        .hasValueSatisfying(updatedStudent -> {
          assertThat(updatedStudent.getId()).isEqualTo(student.getId());
          assertThat(updatedStudent.getOwnerGroup()).isEqualTo(student.getOwnerGroup());
          assertThat(updatedStudent.getFirstName()).isEqualTo(student.getFirstName());
          assertThat(updatedStudent.getLastName()).isEqualTo(student.getLastName());
        });
  }

  @Test
  void deleteByIdShouldWorkCorrectlyIfStudentExists() {
    Student student = entityManager.find(Student.class, 1);

    studentDao.deleteAllStudentCourses(student);

    assertThat(studentDao.deleteById(student.getId())).isTrue();
  }

  @Test
  void deleteByIdShouldWorkCorrectlyIfStudentNotExists() {
    int studentId = 200;

    assertThat(studentDao.deleteById(studentId)).isFalse();
  }

  @Test
  void deleteAllStudentCoursesShouldWorkCorrectlyIfStudentExists() {
    Student student = entityManager.find(Student.class, 1);

    studentDao.deleteAllStudentCourses(student);

    assertThat(student.getCourses()).isEmpty();
  }

}
