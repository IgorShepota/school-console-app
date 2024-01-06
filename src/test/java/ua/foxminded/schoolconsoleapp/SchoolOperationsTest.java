package ua.foxminded.schoolconsoleapp;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
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
    Group group1 = Group.builder().id(1).groupName("Group1").build();
    Group group2 = Group.builder().id(2).groupName("Group2").build();
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
        .id(1)
        .courseName(courseName)
        .build();

    Optional<Course> courseId = Optional.of(course);

    Student student1 = Student.builder()
        .id(1)
        .groupId(1)
        .firstName("John")
        .lastName("Doe")
        .build();

    Student student2 = Student.builder()
        .id(2)
        .groupId(1)
        .firstName("Alice")
        .lastName("Smith")
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
        .id(1)
        .courseName(courseName)
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

    schoolOperations.addNewStudent();

    Student newStudent = Student.builder()
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    verify(consoleManager).print("Inset first name.");
    verify(consoleManager).print("Insert last name.");
    verify(consoleManager).print("Insert group ID (1 to 3):");
    verify(studentService).addStudent(newStudent);
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
    Student student = Student.builder()
        .id(1)
        .groupId(1)
        .firstName("John")
        .lastName("Doe")
        .build();

    Course newCourse = Course.builder()
        .id(2)
        .courseName("Math")
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

    verify(courseService).enrollStudentToCourse(student.getId(), newCourse.getId());
    verify(consoleManager).print("Student successfully added to the course.");
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
  void enrollStudentToCourseShouldNotEnrollIfCourseDoesNotExist() {
    int validStudentId = 1;
    String nonExistentCourseName = "NonExistentCourse";

    Student student = Student.builder()
        .id(validStudentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    when(consoleManager.readLine()).thenReturn(nonExistentCourseName);
    when(consoleManager.parseInput(anyString())).thenReturn(validStudentId);
    when(studentService.getStudentById(validStudentId)).thenReturn(Optional.of(student));
    when(courseService.getCourseIdByName(nonExistentCourseName)).thenReturn(Optional.empty());

    schoolOperations.enrollStudentToCourse();

    verify(courseService, never()).enrollStudentToCourse(anyInt(), anyInt());
    verify(consoleManager).print(
        "Course with the name '" + nonExistentCourseName + "' does not exist.");
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
  void enrollStudentToCourseShouldNotEnrollIfStudentIsAlreadyEnrolled() {
    int validStudentId = 1;
    int validCourseId = 2;
    String existingCourseName = "Math";

    Student student = Student.builder()
        .id(validStudentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    Course course = Course.builder()
        .id(validCourseId)
        .courseName(existingCourseName)
        .build();

    when(consoleManager.readLine()).thenReturn(existingCourseName);
    when(consoleManager.parseInput(anyString())).thenReturn(validStudentId);
    when(studentService.getStudentById(validStudentId)).thenReturn(Optional.of(student));
    when(courseService.getCourseIdByName(existingCourseName)).thenReturn(Optional.of(course));
    when(courseService.checkStudentEnrolledInCourse(validStudentId, validCourseId)).thenReturn(
        true);

    schoolOperations.enrollStudentToCourse();

    verify(courseService, never()).enrollStudentToCourse(validStudentId, validCourseId);
    verify(consoleManager).print(
        "Student with ID " + validStudentId + " is already enrolled in this course.");
  }

  @Test
  void enrollStudentToCourseShouldPrintEnrolledCoursesCorrectlyIfDataCorrect() {
    int studentId = 1;

    Student student = Student.builder()
        .id(studentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    List<Course> mockEnrolledCourses = Arrays.asList(
        Course.builder().id(1).courseName("Math").build(),
        Course.builder().id(2).courseName("Physics").build()
    );

    String result = "Enrolled courses for student with ID 1:\nMath\nPhysics\n";

    when(consoleManager.readLine()).thenReturn("1");
    when(consoleManager.parseInput("1")).thenReturn(studentId);
    when(studentService.getStudentById(studentId)).thenReturn(
        Optional.of(student));
    when(courseService.getEnrolledCoursesForStudent(studentId)).thenReturn(
        mockEnrolledCourses);

    schoolOperations.enrollStudentToCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(courseService).getEnrolledCoursesForStudent(studentId);
    verify(consoleManager).print(result.replaceAll("\n", System.lineSeparator()));
  }

  @Test
  void removeStudentFromCourseShouldRemoveStudentIfEnrolled() {
    int validStudentId = 1;
    int validCourseId = 2;
    String enrolledCourseName = "Math";

    Student student = Student.builder()
        .id(validStudentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    Course course = Course.builder()
        .id(validCourseId)
        .courseName(enrolledCourseName)
        .build();

    when(consoleManager.readLine()).thenReturn(enrolledCourseName);
    when(consoleManager.parseInput(anyString())).thenReturn(validStudentId);
    when(studentService.getStudentById(validStudentId)).thenReturn(Optional.of(student));
    when(courseService.getCourseIdByName(enrolledCourseName)).thenReturn(Optional.of(course));
    when(courseService.checkStudentEnrolledInCourse(validStudentId, validCourseId)).thenReturn(
        true);

    schoolOperations.removeStudentFromCourse();

    verify(courseService).removeStudentFromCourse(validStudentId, validCourseId);
    verify(consoleManager).print("Student successfully removed from the course.");
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

    verify(courseService, never()).removeStudentFromCourse(anyInt(), anyInt());
    verify(consoleManager).print("Invalid input. Please enter a valid number.");
  }


  @Test
  void removeStudentFromCourseShouldWarnIfStudentDoesNotExist() {
    int nonExistentStudentId = 500;
    String courseName = "Math";

    when(consoleManager.readLine()).thenReturn(courseName);
    when(consoleManager.parseInput(anyString())).thenReturn(nonExistentStudentId);
    when(studentService.getStudentById(nonExistentStudentId)).thenReturn(Optional.empty());

    schoolOperations.removeStudentFromCourse();

    verify(courseService, never()).removeStudentFromCourse(anyInt(), anyInt());
    verify(consoleManager).print("Student with ID " + nonExistentStudentId
        + " does not exist.");
  }

  @Test
  void removeStudentFromCourseShouldNotRemoveIfCourseDoesNotExist() {
    int validStudentId = 1;
    String nonExistentCourseName = "NonExistentCourse";

    Student student = Student.builder()
        .id(validStudentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    when(consoleManager.readLine()).thenReturn(String.valueOf(validStudentId),
        nonExistentCourseName);
    when(consoleManager.parseInput(String.valueOf(validStudentId))).thenReturn(validStudentId);
    when(studentService.getStudentById(validStudentId)).thenReturn(Optional.of(student));

    when(courseService.getCourseIdByName(nonExistentCourseName)).thenReturn(Optional.empty());

    schoolOperations.removeStudentFromCourse();

    verify(courseService, never()).removeStudentFromCourse(anyInt(), anyInt());
    verify(consoleManager).print(
        "Course with the name '" + nonExistentCourseName + "' does not exist.");
  }

  @Test
  void removeStudentFromCourseShouldExitIfStudentNotEnrolled() {
    int validStudentId = 1;
    int validCourseId = 2;
    String courseName = "Math";

    Student student = Student.builder()
        .id(validStudentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    Course course = Course.builder()
        .id(validCourseId)
        .courseName(courseName)
        .build();

    when(consoleManager.readLine()).thenReturn(courseName);
    when(consoleManager.parseInput(anyString())).thenReturn(validStudentId);
    when(studentService.getStudentById(validStudentId)).thenReturn(Optional.of(student));
    when(courseService.getCourseIdByName(courseName)).thenReturn(Optional.of(course));
    when(courseService.checkStudentEnrolledInCourse(validStudentId, validCourseId)).thenReturn(
        false);

    schoolOperations.removeStudentFromCourse();

    verify(courseService, never()).removeStudentFromCourse(anyInt(), anyInt());
    verify(consoleManager).print(
        "Student with ID " + validStudentId + " is not enrolled in the " + courseName + " course");
  }

  @Test
  void removeStudentFromCourseShouldPrintEnrolledCoursesCorrectlyIfDataCorrect() {
    int studentId = 1;

    Student student = Student.builder()
        .id(studentId)
        .firstName("John")
        .lastName("Doe")
        .groupId(1)
        .build();

    List<Course> mockEnrolledCourses = Arrays.asList(
        Course.builder().id(1).courseName("Math").build(),
        Course.builder().id(2).courseName("Physics").build()
    );

    String result = "Enrolled courses for student with ID 1:\nMath\nPhysics\n";

    when(consoleManager.readLine()).thenReturn("1");
    when(consoleManager.parseInput("1")).thenReturn(studentId);
    when(studentService.getStudentById(studentId)).thenReturn(
        Optional.of(student));
    when(courseService.getEnrolledCoursesForStudent(studentId)).thenReturn(
        mockEnrolledCourses);

    schoolOperations.removeStudentFromCourse();

    verify(consoleManager).print("Enter the student ID");
    verify(courseService).getEnrolledCoursesForStudent(studentId);
    verify(consoleManager).print(result.replaceAll("\n", System.lineSeparator()));
  }

}
