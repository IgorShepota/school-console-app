package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@SpringBootTest
class StudentServiceTest {

  @MockBean
  private StudentDao studentDao;

  @Autowired
  private StudentService studentService;

  @Test
  void findStudentsByCourseNameShouldWorkCorrectlyIfDataCorrect() {
    String courseName = "Mathematics";
    List<Student> mockStudents = Arrays.asList(
        Student.builder().withId(1).withFirstName("John").withLastName("Doe").build(),
        Student.builder().withId(2).withFirstName("Jane").withLastName("Smith").build()
    );

    when(studentDao.findStudentsByCourseName(courseName)).thenReturn(mockStudents);

    List<Student> students = studentService.findStudentsByCourseName(courseName);

    assertThat(students).hasSize(2);
    assertThat(students).isEqualTo(mockStudents);
  }

  @Test
  void addStudentShouldWorkCorrectlyIfStudentEntityCorrect() {
    Student mockStudent = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentService.addStudent(mockStudent);

    verify(studentDao).save(mockStudent);
  }

  @Test
  void getStudentByIdShouldWorkCorrectlyIfStudentExists() {
    Integer studentId = 1;
    Student mockStudent = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(mockStudent));

    Optional<Student> result = studentService.getStudentById(studentId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(mockStudent);
  }

  @Test
  void getStudentByIdShouldReturnEmptyIfStudentDoesNotExist() {
    Integer nonExistentId = 999;
    when(studentDao.findById(nonExistentId)).thenReturn(Optional.empty());

    Optional<Student> result = studentService.getStudentById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  void getAllStudentsShouldReturnAllStudents() {
    List<Student> mockStudents = Arrays.asList(
        Student.builder().withId(1).withFirstName("John").withLastName("Doe").build(),
        Student.builder().withId(2).withFirstName("Jane").withLastName("Smith").build()
    );

    when(studentDao.findAll()).thenReturn(mockStudents);

    List<Student> students = studentService.getAllStudents();

    assertThat(students).hasSize(2);
    assertThat(students).isEqualTo(mockStudents);
  }

  @Test
  void getAllStudentsWithPaginationShouldReturnCorrectData() {
    List<Student> mockStudents = Arrays.asList(
        Student.builder().withId(1).withFirstName("John").withLastName("Doe").build()
    );

    when(studentDao.findAll(1, 1)).thenReturn(mockStudents);

    List<Student> students = studentService.getAllStudents(1, 1);

    assertThat(students).hasSize(1);
    assertThat(students).isEqualTo(mockStudents);
  }

  @Test
  void updateStudentShouldCallDaoUpdateMethod() {
    Student student = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentService.updateStudent(student);

    verify(studentDao).update(student);
  }

  @Test
  void deleteStudentShouldReturnTrueIfStudentDeleted() {
    Integer studentId = 1;

    when(studentDao.deleteAllStudentCourses(studentId)).thenReturn(true);
    when(studentDao.deleteById(studentId)).thenReturn(true);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isTrue();
  }

  @Test
  void deleteStudentShouldReturnFalseIfStudentNotDeleted() {
    Integer studentId = 999;

    when(studentDao.deleteAllStudentCourses(studentId)).thenReturn(true);
    when(studentDao.deleteById(studentId)).thenReturn(false);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isTrue();
  }

  @Test
  void deleteStudentShouldReturnFalseIfOneOfOperationsIncorrect() {
    Integer studentId = 1;
    when(studentDao.deleteAllStudentCourses(studentId)).thenReturn(false);
    when(studentDao.deleteById(studentId)).thenReturn(true);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isTrue();
  }

  @Test
  void deleteStudentShouldReturnFalseIfBothOperationsIncorrect() {
    Integer studentId = 1;
    when(studentDao.deleteAllStudentCourses(studentId)).thenReturn(false);
    when(studentDao.deleteById(studentId)).thenReturn(false);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isFalse();
  }

}
