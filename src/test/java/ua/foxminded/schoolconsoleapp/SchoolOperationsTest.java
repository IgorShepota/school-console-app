package ua.foxminded.schoolconsoleapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolconsoleapp.consolemanager.ConsoleManager;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Group;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.service.dao.CourseService;
import ua.foxminded.schoolconsoleapp.service.dao.GroupService;
import ua.foxminded.schoolconsoleapp.service.dao.StudentService;
import ua.foxminded.schoolconsoleapp.validator.Validator;
import ua.foxminded.schoolconsoleapp.validator.exception.ValidationException;

@SpringBootTest
class SchoolOperationsTest {

  @MockBean
  protected StudentService studentService;

  @MockBean
  protected CourseService courseService;

  @MockBean
  protected GroupService groupService;

  @MockBean
  ConsoleManager consoleManager;

  @MockBean
  Validator validator;

  @Autowired
  SchoolOperations schoolOperations;

  @Test
  void findGroupsWithLessOrEqualStudentShouldWorkCorrectlyIfInputIsPositiveNumber() {
    String userInput = "5";
    Group group1 = Group.builder().withId(1).withGroupName("Group1").build();
    Group group2 = Group.builder().withId(2).withGroupName("Group2").build();
    List<Group> mockGroups = Arrays.asList(group1, group2);

    when(consoleManager.readLine()).thenReturn(userInput);
    when(consoleManager.parseInput(userInput)).thenReturn(5);
    when(groupService.findGroupsWithLessOrEqualStudent(5)).thenReturn(mockGroups);

    schoolOperations.findGroupsWithLessOrEqualStudent();

    verify(consoleManager).print("Insert maximum amount of students.");
    verify(consoleManager).readLine();
    verify(validator).validateNumber(5);
    verify(consoleManager).print("Group1");
    verify(consoleManager).print("Group2");
  }

  @Test
  void findGroupsWithLessOrEqualStudentShouldPrintMessageIfNoGroupsFound() {
    String userInput = "5";
    List<Group> mockEmptyGroups = Collections.emptyList();

    when(consoleManager.readLine()).thenReturn(userInput);
    when(consoleManager.parseInput(userInput)).thenReturn(5);
    when(groupService.findGroupsWithLessOrEqualStudent(5)).thenReturn(mockEmptyGroups);

    schoolOperations.findGroupsWithLessOrEqualStudent();

    verify(consoleManager).print("Insert maximum amount of students.");
    verify(consoleManager).readLine();
    verify(validator).validateNumber(5);
    verify(consoleManager).print("There are no groups with " + userInput + " or fewer students.");
  }

  @Test
  void findGroupsWithLessOrEqualStudentShouldPrintErrorMessageWhenValidationException() {
    String userInput = "-1";
    when(consoleManager.readLine()).thenReturn(userInput);
    when(consoleManager.parseInput(userInput)).thenReturn(-1);
    doThrow(new ValidationException("Invalid input. Please enter a valid number."))
        .when(validator).validateNumber(-1);

    schoolOperations.findGroupsWithLessOrEqualStudent();

    verify(consoleManager).print("Insert maximum amount of students.");
    verify(consoleManager).readLine();
    verify(consoleManager).print("Invalid input. Please enter a valid number.");
  }

  @Test
  void findStudentsByGroupNameShouldWorkCorrectlyIfCourseExists() {
    String courseName = "Math";

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .build();

    Optional<Course> courseId = Optional.of(course);

    Student student1 = Student.builder()
        .withId(1)
        .withFirstName("John")
        .withLastName("Doe")
        .build();

    Student student2 = Student.builder()
        .withId(2)
        .withFirstName("Alice")
        .withLastName("Smith")
        .build();

    List<Student> enrolledStudents = Lists.list(student1, student2);

    when(consoleManager.readLine()).thenReturn(courseName);
    when(courseService.getCourseIdByName(courseName)).thenReturn(courseId);
    when(studentService.findStudentsByCourseName(courseName)).thenReturn(enrolledStudents);

    schoolOperations.findStudentsByGroupName();

    verify(consoleManager).print("Enter the course name.");
    verify(courseService).getCourseIdByName(courseName);
    verify(studentService).findStudentsByCourseName(courseName);
    verify(consoleManager).print("John Doe");
    verify(consoleManager).print("Alice Smith");
  }

  @Test
  void findStudentsByGroupNameShouldWorkCorrectlyIfCourseDoesNotExists() {
    String courseName = "Algebra";
    Optional<Course> courseId = Optional.empty();

    when(consoleManager.readLine()).thenReturn(courseName);
    when(courseService.getCourseIdByName(courseName)).thenReturn(courseId);

    schoolOperations.findStudentsByGroupName();

    verify(consoleManager).print("Enter the course name.");
    verify(courseService).getCourseIdByName(courseName);
    verify(consoleManager).print("Course with the name '" + courseName + "' does not exist.");
  }

  @Test
  void findStudentsByGroupNameShouldWorkCorrectlyIfZeroStudentsAssigned() {
    String courseName = "Math";

    Course course = Course.builder()
        .withId(1)
        .withCourseName(courseName)
        .build();

    Optional<Course> courseId = Optional.of(course);

    List<Student> enrolledStudents = new ArrayList<>();

    when(consoleManager.readLine()).thenReturn(courseName);
    when(courseService.getCourseIdByName(courseName)).thenReturn(courseId);
    when(studentService.findStudentsByCourseName(courseName)).thenReturn(enrolledStudents);

    schoolOperations.findStudentsByGroupName();

    verify(consoleManager).print("Enter the course name.");
    verify(courseService).getCourseIdByName(courseName);
    verify(studentService).findStudentsByCourseName(courseName);
    verify(consoleManager).print("No student is enrolled in this course.");
  }

  @Test
  void addNewStudentShouldWorkCorrectlyWithCorrectData() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("Doe")
        .thenReturn("1");

    when(consoleManager.parseInput("1")).thenReturn(1);

    Group group = Group.builder()
        .withId(1)
        .withGroupName("Group1")
        .build();

    when(groupService.getGroupById(1)).thenReturn(Optional.of(group));

    schoolOperations.addNewStudent();

    ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
    verify(studentService).addStudent(studentCaptor.capture());
    Student capturedStudent = studentCaptor.getValue();

    assertThat(capturedStudent.getFirstName()).isEqualTo("John");
    assertThat(capturedStudent.getLastName()).isEqualTo("Doe");
    assertThat(capturedStudent.getOwnerGroup()).isEqualTo(group);

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(consoleManager).print("New student added.");
  }

  @Test
  void addNewStudentShouldWorkCorrectlyWithEmptyFirstName() {
    when(consoleManager.readLine())
        .thenReturn("");

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Invalid input. You have entered an empty string.");
  }

  @Test
  void addNewStudentShouldWorkCorrectlyWithEmptyLastName() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("");

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Invalid input. You have entered an empty string.");
  }

  @Test
  void addNewStudentShouldWorkCorrectlyWithEmptyGroupId() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("Doe")
        .thenReturn("");

    when(consoleManager.parseInput("")).thenReturn(-1);
    doThrow(new ValidationException("Input is empty. Please enter a number."))
        .when(validator).validateNumber(-1);

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(consoleManager).print("Input is empty. Please enter a number.");
  }

  @Test
  void addNewStudentShouldWorkCorrectlyIfGroupIdMoreThen10() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("Doe")
        .thenReturn("12");
    when(consoleManager.parseInput("12")).thenReturn(12);

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(consoleManager).print("Group ID should be from 1 to 3.");
  }

  @Test
  void addNewStudentShouldHandleValidationExceptionIfDataIncorrect() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("Doe")
        .thenReturn("invalid");
    when(consoleManager.parseInput("invalid")).thenReturn(-2);
    doThrow(new ValidationException("Invalid input. Please enter a valid number."))
        .when(validator).validateNumber(-2);

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(consoleManager).print("Invalid input. Please enter a valid number.");
  }

  @Test
  void addNewStudentShouldNotAddIfGroupNotFound() {
    when(consoleManager.readLine())
        .thenReturn("John")
        .thenReturn("Doe")
        .thenReturn("3");
    when(consoleManager.parseInput("3")).thenReturn(3);

    when(groupService.getGroupById(3)).thenReturn(Optional.empty());

    schoolOperations.addNewStudent();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(consoleManager).print("Group with ID 3 not found.");

    verify(studentService, never()).addStudent(any(Student.class));
  }

  @Test
  void deleteStudentShouldWorkCorrectlyIfStudentExists() {
    int studentId = 45;
    when(consoleManager.readLine())
        .thenReturn("45");
    when(consoleManager.parseInput("45")).thenReturn(studentId);
    when(studentService.deleteStudent(studentId)).thenReturn(true);

    schoolOperations.deleteStudent();

    verify(consoleManager).print(
        "Enter the ID number of the student you want to delete from the list.");
    verify(studentService).deleteStudent(studentId);
    verify(consoleManager).print(
        "Student with ID " + studentId + " has been successfully deleted.");
  }

  @Test
  void deleteStudentShouldWorkCorrectlyIfEmptyInput() {
    when(consoleManager.readLine()).thenReturn("");
    when(consoleManager.parseInput("")).thenReturn(-1);
    doThrow(new ValidationException("Input is empty. Please enter a number."))
        .when(validator).validateNumber(-1);

    schoolOperations.deleteStudent();

    verify(consoleManager).print(
        "Enter the ID number of the student you want to delete from the list.");
    verify(consoleManager).print("Input is empty. Please enter a number.");
  }

  @Test
  void deleteStudentShouldWorkCorrectlyIfStudentNotExists() {
    int studentId = 45;
    when(consoleManager.readLine())
        .thenReturn("45");
    when(consoleManager.parseInput("45")).thenReturn(studentId);
    when(studentService.deleteStudent(studentId)).thenReturn(false);

    schoolOperations.deleteStudent();

    verify(consoleManager).print(
        "Enter the ID number of the student you want to delete from the list.");
    verify(studentService).deleteStudent(studentId);
    verify(consoleManager).print(
        "No student found with ID " + studentId + ". No deletion performed.");
  }

  @Test
  void enrollStudentToCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    String courseName = "Math";

    Student student = Student.builder()
        .withId(studentId)
        .withFirstName("John")
        .withLastName("Doe")
        .withOwnerGroup(Group.builder().withId(1).withGroupName("Group1").build())
        .build();

    Course newCourse = Course.builder()
        .withId(2)
        .withCourseName(courseName)
        .build();

    when(consoleManager.readLine()).thenReturn(newCourse.getCourseName());
    when(consoleManager.parseInput(anyString())).thenReturn(student.getId());
    when(studentService.getStudentById(student.getId())).thenReturn(Optional.of(student));
    when(courseService.getCourseIdByName(newCourse.getCourseName())).thenReturn(
        Optional.of(newCourse));
    when(courseService.getEnrolledCoursesForStudent(student.getId())).thenReturn(
        Collections.emptyList());
    when(courseService.checkStudentEnrolledInCourse(student.getId(),
        newCourse.getId())).thenReturn(false);

    schoolOperations.enrollStudentToCourse();

    verify(consoleManager).print("Student successfully added to the course 'Math'.");
  }

  @Test
  void enrollStudentToCourseShouldNotEnrollIfStudentIdIsInvalid() {
    int invalidStudentId = 0;

    when(consoleManager.readLine()).thenReturn(String.valueOf(invalidStudentId));
    when(consoleManager.parseInput(String.valueOf(invalidStudentId))).thenReturn(invalidStudentId);
    doThrow(new ValidationException("Invalid input. Please enter a valid number."))
        .when(validator).validateNumber(invalidStudentId);

    schoolOperations.enrollStudentToCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(consoleManager).print("Invalid input. Please enter a valid number.");
  }

  @Test
  void enrollStudentToCourseShouldPrintMessageIfStudentDoesNotExist() {
    int nonExistentStudentId = 999;

    when(consoleManager.readLine()).thenReturn(String.valueOf(nonExistentStudentId));
    when(consoleManager.parseInput(String.valueOf(nonExistentStudentId))).thenReturn(
        nonExistentStudentId);
    when(studentService.getStudentById(nonExistentStudentId)).thenReturn(Optional.empty());

    schoolOperations.enrollStudentToCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(studentService).getStudentById(nonExistentStudentId);
    verify(consoleManager).print("Student with ID " + nonExistentStudentId + " does not exist.");
  }

  @Test
  void enrollStudentToCourseShouldDisplayEnrolledCoursesCorrectly() {
    int existingStudentId = 1;
    List<Course> enrolledCourses = Arrays.asList(
        Course.builder().withId(1).withCourseName("Mathematics").build(),
        Course.builder().withId(2).withCourseName("Physics").build()
    );
    String expectedCoursesList = "Enrolled courses for student with ID " + existingStudentId + ":\nMathematics\nPhysics\n";

    when(consoleManager.readLine()).thenReturn(String.valueOf(existingStudentId), "NewCourse");
    when(consoleManager.parseInput(String.valueOf(existingStudentId))).thenReturn(existingStudentId);
    when(studentService.getStudentById(existingStudentId)).thenReturn(Optional.of(new Student()));
    when(courseService.getEnrolledCoursesForStudent(existingStudentId)).thenReturn(enrolledCourses);

    schoolOperations.enrollStudentToCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(consoleManager, times(2)).readLine();
    verify(consoleManager).parseInput(String.valueOf(existingStudentId));
    verify(studentService).getStudentById(existingStudentId);
    verify(consoleManager).print(expectedCoursesList);

    verify(consoleManager).print("Enter a course name");
    verify(courseService).enrollStudentToCourse(existingStudentId, "NewCourse");
    verify(consoleManager).print("Student successfully added to the course 'NewCourse'.");
  }

  @Test
  void removeStudentFromCourseShouldSucceedWithValidStudentIdAndCourseName() {
    int existingStudentId = 1;
    String existingCourseName = "Mathematics";
    List<Course> enrolledCourses = Arrays.asList(
        Course.builder().withId(1).withCourseName("Mathematics").build(),
        Course.builder().withId(2).withCourseName("Physics").build()
    );
    String expectedCoursesList = "Enrolled courses for student with ID " + existingStudentId + ":\nMathematics\nPhysics\n";

    when(consoleManager.readLine()).thenReturn(String.valueOf(existingStudentId), existingCourseName);
    when(consoleManager.parseInput(String.valueOf(existingStudentId))).thenReturn(existingStudentId);
    when(studentService.getStudentById(existingStudentId)).thenReturn(Optional.of(new Student()));
    when(courseService.getEnrolledCoursesForStudent(existingStudentId)).thenReturn(enrolledCourses);

    schoolOperations.removeStudentFromCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(consoleManager, times(2)).readLine();
    verify(consoleManager).parseInput(String.valueOf(existingStudentId));
    verify(studentService).getStudentById(existingStudentId);
    verify(consoleManager).print(expectedCoursesList);
    verify(consoleManager).print("Enter a course name");
    verify(courseService).removeStudentFromCourse(existingStudentId, existingCourseName);
    verify(consoleManager).print("Student successfully removed from the course '" + existingCourseName + "'.");
  }

  @Test
  void removeStudentFromCourseShouldNotRemoveIfStudentIdIsInvalid() {
    int invalidStudentId = 0;
    String courseName = "Math";

    when(consoleManager.readLine()).thenReturn(courseName);
    when(consoleManager.parseInput(anyString())).thenReturn(invalidStudentId);
    doThrow(new ValidationException("Invalid input. Please enter a valid number."))
        .when(validator).validateNumber(invalidStudentId);

    schoolOperations.removeStudentFromCourse();

    verify(courseService, never()).removeStudentFromCourse(anyInt(), anyString());
    verify(consoleManager).print("Invalid input. Please enter a valid number.");
  }

  @Test
  void removeStudentFromCourseShouldNotProceedIfStudentNotFound() {
    int nonExistentStudentId = 999;
    String courseName = "Mathematics";

    when(consoleManager.readLine()).thenReturn(String.valueOf(nonExistentStudentId));
    when(consoleManager.parseInput(String.valueOf(nonExistentStudentId))).thenReturn(nonExistentStudentId);
    when(studentService.getStudentById(nonExistentStudentId)).thenReturn(Optional.empty());

    schoolOperations.removeStudentFromCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(consoleManager).readLine();
    verify(studentService).getStudentById(nonExistentStudentId);
    verify(consoleManager).print("Student with ID " + nonExistentStudentId + " does not exist.");

    verify(consoleManager, never()).print("Enter a course name");
    verify(courseService, never()).removeStudentFromCourse(nonExistentStudentId, courseName);
    verify(consoleManager, never()).print("Student successfully removed from the course '" + courseName + "'.");
  }

}
