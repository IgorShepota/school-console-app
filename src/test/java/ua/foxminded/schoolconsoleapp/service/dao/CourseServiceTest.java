package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.repository.CourseRepository;
import ua.foxminded.schoolconsoleapp.repository.StudentRepository;
import ua.foxminded.schoolconsoleapp.repository.exception.DataBaseSqlRuntimeException;

@SpringBootTest
class CourseServiceTest {

  @MockBean
  private CourseRepository courseRepository;

  @MockBean
  private StudentRepository studentRepository;

  @Autowired
  private CourseService courseService;

  @Test
  void checkStudentEnrolledInCourseShouldReturnTrueIfEnrolled() {
    int studentId = 1, courseId = 1;
    when(courseRepository.checkStudentEnrolledInCourse(studentId, courseId)).thenReturn(1L);

    boolean enrolled = courseService.checkStudentEnrolledInCourse(studentId, courseId);

    assertThat(enrolled).isTrue();
  }

  @Test
  void checkStudentEnrolledInCourseShouldReturnFalseIfNotEnrolled() {
    int studentId = 1, courseId = 1;
    when(courseRepository.checkStudentEnrolledInCourse(studentId, courseId)).thenReturn(0L);

    boolean enrolled = courseService.checkStudentEnrolledInCourse(studentId, courseId);

    assertThat(enrolled).isFalse();
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

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));

    courseService.enrollStudentToCourse(studentId, courseName);

    ArgumentCaptor<Student> studentCaptor = forClass(Student.class);
    ArgumentCaptor<Course> courseCaptor = forClass(Course.class);

    verify(studentRepository).save(studentCaptor.capture());
    verify(courseRepository).save(courseCaptor.capture());

    Student savedStudent = studentCaptor.getValue();
    Course savedCourse = courseCaptor.getValue();

    assertThat(savedStudent.getCourses()).contains(course);
    assertThat(savedCourse.getStudents()).contains(student);
  }

  @Test
  void enrollStudentToCourseShouldThrowExceptionWhenStudentNotFound() {
    int studentId = 1;
    String courseName = "Mathematics";

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Student with ID " + studentId + " does not exist.");
  }

  @Test
  void enrollStudentToCourseShouldThrowExceptionWhenCourseNotFound() {
    int studentId = 1;
    String courseName = "Mathematics";

    Student student = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .withCourses(new ArrayList<>())
        .build();

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Course with the name '" + courseName + "' does not exist.");
  }

  @Test
  void enrollStudentToCourseShouldThrowExceptionWhenStudentAlreadyEnrolled() {
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

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));
    when(courseRepository.checkStudentEnrolledInCourse(studentId, course.getId())).thenReturn(1L);

    assertThatThrownBy(() -> courseService.enrollStudentToCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining(
            "Student with ID " + studentId + " is already enrolled in the course '" + courseName
                + "'.");
  }

  @Test
  void removeStudentFromCourseShouldWorkCorrectly() {
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

    student.getCourses().add(course);
    course.getStudents().add(student);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));
    when(courseRepository.checkStudentEnrolledInCourse(studentId, course.getId())).thenReturn(1L);

    courseService.removeStudentFromCourse(studentId, courseName);

    ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
    ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);

    verify(studentRepository).save(studentCaptor.capture());
    verify(courseRepository).save(courseCaptor.capture());

    Student updatedStudent = studentCaptor.getValue();
    Course updatedCourse = courseCaptor.getValue();

    assertThat(updatedStudent.getCourses()).doesNotContain(course);
    assertThat(updatedCourse.getStudents()).doesNotContain(student);
  }

  @Test
  void removeStudentFromCourseShouldThrowExceptionWhenStudentNotFound() {
    int studentId = 1;
    String courseName = "Mathematics";

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Student with ID " + studentId + " does not exist.");
  }

  @Test
  void removeStudentFromCourseShouldThrowExceptionWhenCourseNotFound() {
    int studentId = 1;
    String courseName = "Mathematics";
    Student student = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .withCourses(new ArrayList<>())
        .build();

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining("Course with the name '" + courseName + "' does not exist.");
  }

  @Test
  void removeStudentFromCourseShouldThrowExceptionWhenStudentNotEnrolled() {
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

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));
    when(courseRepository.checkStudentEnrolledInCourse(studentId, course.getId())).thenReturn(0L);

    assertThatThrownBy(() -> courseService.removeStudentFromCourse(studentId, courseName))
        .isInstanceOf(DataBaseSqlRuntimeException.class)
        .hasMessageContaining(
            "Student with ID " + studentId + " is not enrolled in the course '" + courseName
                + "'.");
  }

  @Test
  void getCourseIdByNameShouldReturnCorrectDataIfExists() {
    String courseName = "Mathematics";
    Course mockCourse = Course.builder().withId(1).withCourseName(courseName).build();
    when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(mockCourse));

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

    when(courseRepository.getEnrolledCoursesForStudent(studentId)).thenReturn(mockCourses);

    List<Course> courses = courseService.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void addCourseShouldCallDaoSaveMethodWhenAdding() {
    Course course = Course.builder().withId(1).withCourseName("Mathematics").build();

    courseService.addCourse(course);

    verify(courseRepository).save(course);
  }

  @Test
  void getCourseByIdShouldReturnCorrectDataIfExists() {
    Integer courseId = 1;
    Course mockCourse = Course.builder().withId(courseId).withCourseName("Mathematics").build();
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

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

    when(courseRepository.findAll()).thenReturn(mockCourses);

    List<Course> courses = courseService.getAllCourses();

    assertThat(courses).hasSize(2)
        .isEqualTo(mockCourses);
  }

  @Test
  void getAllCoursesWithPaginationShouldReturnCorrectCoursesWhenPageGiven() {
    List<Course> mockCourses = Collections.singletonList(
        Course.builder().withId(1).withCourseName("Mathematics").build()
    );
    Page<Course> pageOfCourses = new PageImpl<>(mockCourses);

    when(courseRepository.findAll(any(Pageable.class))).thenReturn(pageOfCourses);

    List<Course> courses = courseService.getAllCourses(1, 1);

    assertThat(courses).hasSize(1)
        .isEqualTo(mockCourses);
    verify(courseRepository).findAll(PageRequest.of(0, 1));
  }

  @Test
  void updateCourseShouldCallDaoUpdateMethodWhenUpdating() {
    Course course = Course.builder().withId(1).withCourseName("Mathematics").build();

    courseService.updateCourse(course);

    verify(courseRepository).save(course);
  }

  @Test
  void deleteCourseShouldReturnTrueWhenCourseExists() {
    Integer courseId = 1;

    when(courseRepository.existsById(courseId)).thenReturn(true);

    boolean result = courseService.deleteCourse(courseId);

    assertThat(result).isTrue();
    verify(courseRepository).deleteById(courseId);
  }

  @Test
  void deleteCourseShouldReturnFalseWhenCourseDoesNotExist() {
    Integer courseId = 1;

    when(courseRepository.existsById(courseId)).thenReturn(false);

    boolean result = courseService.deleteCourse(courseId);

    assertThat(result).isFalse();
    verify(courseRepository, never()).deleteById(courseId);
  }

}
