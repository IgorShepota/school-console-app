package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        Student.builder().id(1).firstName("John").lastName("Doe").build(),
        Student.builder().id(2).firstName("Jane").lastName("Smith").build()
    );

    when(studentDao.findStudentsByCourseName(courseName)).thenReturn(mockStudents);

    List<Student> students = studentService.findStudentsByCourseName(courseName);

    assertThat(students).hasSize(2)
        .isEqualTo(mockStudents);
  }

  @Test
  void addStudentShouldWorkCorrectlyIfStudentEntityCorrect() {
    Student mockStudent = Student.builder()
        .id(1)
        .firstName("John")
        .lastName("Doe")
        .build();

    studentService.addStudent(mockStudent);

    verify(studentDao).save(mockStudent);
  }

  @Test
  void getStudentByIdShouldWorkCorrectlyIfStudentExists() {
    Integer studentId = 1;
    Student mockStudent = Student.builder()
        .id(studentId)
        .firstName("John")
        .lastName("Doe")
        .build();

    when(studentDao.findById(studentId)).thenReturn(Optional.of(mockStudent));

    Optional<Student> result = studentService.getStudentById(studentId);

    assertThat(result).isPresent();
    assertThat(mockStudent).isEqualTo(result.get());
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
        Student.builder().id(1).firstName("John").lastName("Doe").build(),
        Student.builder().id(2).firstName("Jane").lastName("Smith").build()
    );

    when(studentDao.findAll()).thenReturn(mockStudents);

    List<Student> students = studentService.getAllStudents();

    assertThat(students).hasSize(2)
        .isEqualTo(mockStudents);
  }

  @Test
  void getAllStudentsWithPaginationShouldReturnCorrectData() {
    List<Student> mockStudents = Collections.singletonList(
        Student.builder().id(1).firstName("John").lastName("Doe").build()
    );

    when(studentDao.findAll(1, 1)).thenReturn(mockStudents);

    List<Student> students = studentService.getAllStudents(1, 1);

    assertThat(students).hasSize(1)
        .isEqualTo(mockStudents);
  }

  @Test
  void updateStudentShouldCallDaoUpdateMethod() {
    Student student = Student.builder()
        .id(1)
        .firstName("John")
        .lastName("Doe")
        .build();

    studentService.updateStudent(student);

    verify(studentDao).update(student);
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForDeleteStudent")
  void deleteStudentShouldTestAllScenarios(Integer studentId, boolean deleteCoursesResult,
      boolean deleteStudentResult, boolean expectedResult) {
    when(studentDao.deleteAllStudentCourses(studentId)).thenReturn(deleteCoursesResult);
    when(studentDao.deleteById(studentId)).thenReturn(deleteStudentResult);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> provideArgumentsForDeleteStudent() {
    return Stream.of(
        Arguments.of(1, true, true, true),
        Arguments.of(999, true, false, true),
        Arguments.of(1, false, true, true),
        Arguments.of(1, false, false, false)
    );
  }

}
