package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.repository.StudentRepository;

@SpringBootTest
class StudentServiceTest {

  @MockBean
  private StudentRepository studentRepository;

  @Autowired
  private StudentService studentService;

  @Test
  void findStudentsByCourseNameShouldWorkCorrectlyIfDataCorrect() {
    String courseName = "Mathematics";
    List<Student> mockStudents = Arrays.asList(
        Student.builder().withId(1).withFirstName("John").withLastName("Doe").build(),
        Student.builder().withId(2).withFirstName("Jane").withLastName("Smith").build()
    );

    when(studentRepository.findStudentsByCourseName(courseName)).thenReturn(mockStudents);

    List<Student> students = studentService.findStudentsByCourseName(courseName);

    assertThat(students).hasSize(2)
        .isEqualTo(mockStudents);
  }

  @Test
  void addStudentShouldWorkCorrectlyIfStudentEntityCorrect() {
    Student mockStudent = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentService.addStudent(mockStudent);

    verify(studentRepository).save(mockStudent);
  }

  @Test
  void getStudentByIdShouldWorkCorrectlyIfStudentExists() {
    Integer studentId = 1;
    Student mockStudent = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

    Optional<Student> result = studentService.getStudentById(studentId);

    assertThat(result).isPresent();
    assertThat(mockStudent).isEqualTo(result.get());
  }

  @Test
  void getStudentByIdShouldReturnEmptyIfStudentDoesNotExist() {
    Integer nonExistentId = 999;
    when(studentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    Optional<Student> result = studentService.getStudentById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  void getAllStudentsShouldReturnAllStudents() {
    List<Student> mockStudents = Arrays.asList(
        Student.builder().withId(1).withFirstName("John").withLastName("Doe").build(),
        Student.builder().withId(2).withFirstName("Jane").withLastName("Smith").build()
    );

    when(studentRepository.findAll()).thenReturn(mockStudents);

    List<Student> students = studentService.getAllStudents();

    assertThat(students).hasSize(2)
        .isEqualTo(mockStudents);
  }

  @Test
  void getAllStudentsWithPaginationShouldReturnCorrectData() {
    Student elizabeth = Student.builder()
        .withId(1)
        .withFirstName("Elizabeth")
        .withLastName("Harris")
        .build();
    Student laura = Student.builder()
        .withId(2)
        .withFirstName("Laura")
        .withLastName("Taylor")
        .build();

    List<Student> students = Arrays.asList(elizabeth, laura);
    Page<Student> studentPage = new PageImpl<>(students);

    when(studentRepository.findAll(any(Pageable.class))).thenReturn(studentPage);

    List<Student> returnedStudents = studentService.getAllStudents(1, 2);

    assertThat(returnedStudents).hasSize(2)
        .containsExactlyInAnyOrder(elizabeth, laura);

    verify(studentRepository).findAll(PageRequest.of(0, 2, Sort.by("id")));
  }

  @Test
  void updateStudentShouldCallDaoUpdateMethod() {
    Student student = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    studentService.updateStudent(student);

    verify(studentRepository).save(student);
  }

  @Test
  void deleteStudentWorkCorrectlyIfStudentExists() {
    int studentId = 1;
    boolean expectedResult = true;

    when(studentRepository.existsById(studentId)).thenReturn(true);

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isEqualTo(expectedResult);
    verify(studentRepository).deleteById(studentId);
  }

  @Test
  void deleteStudentWorkCorrectlyIfStudentNotExists() {
    int studentId = 1;
    boolean expectedResult = false;

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    boolean result = studentService.deleteStudent(studentId);

    assertThat(result).isEqualTo(expectedResult);
  }

}
