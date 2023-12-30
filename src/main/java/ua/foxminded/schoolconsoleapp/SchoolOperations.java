package ua.foxminded.schoolconsoleapp;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolconsoleapp.consolemanager.ConsoleManager;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Group;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.service.dao.CourseService;
import ua.foxminded.schoolconsoleapp.service.dao.GroupService;
import ua.foxminded.schoolconsoleapp.service.dao.StudentService;
import ua.foxminded.schoolconsoleapp.validator.Validator;
import ua.foxminded.schoolconsoleapp.validator.exception.ValidationException;

@Service
public class SchoolOperations {

  private final ConsoleManager consoleManager;
  private final Validator validator;
  private final StudentService studentService;
  private final GroupService groupService;
  private final CourseService courseService;

  @Autowired
  public SchoolOperations(ConsoleManager consoleManager, Validator validator,
      StudentService studentService,
      GroupService groupService, CourseService courseService) {
    this.consoleManager = consoleManager;
    this.validator = validator;
    this.studentService = studentService;
    this.groupService = groupService;
    this.courseService = courseService;
  }

  public void findGroupsWithLessOrEqualStudent() {
    consoleManager.print("Insert maximum amount of students.");

    try {
      int maxStudents = consoleManager.parseInput(consoleManager.readLine());

      validator.validateNumber(maxStudents);

      List<Group> groupsWithLessOrEqualsStudents =
          groupService.findGroupsWithLessOrEqualStudent(maxStudents);

      if (groupsWithLessOrEqualsStudents.isEmpty()) {
        consoleManager.print("There are no groups with " + maxStudents + " or fewer students.");
      }

      groupsWithLessOrEqualsStudents.stream()
          .map(Group::getGroupName)
          .forEach(consoleManager::print);

    } catch (ValidationException e) {
      consoleManager.print(e.getMessage());
    }

  }

  public void findStudentsByGroupName() {
    consoleManager.print("Enter the course name.");
    String courseName = consoleManager.readLine();

    Optional<Course> courseId = courseService.getCourseIdByName(courseName);

    if (!courseId.isPresent()) {
      consoleManager.print("Course with the name '" + courseName + "' does not exist.");
      return;
    }

    List<Student> enrolledStudents = studentService.findStudentsByCourseName(
        courseName);

    if (enrolledStudents.isEmpty()) {
      consoleManager.print("No student is enrolled in this course.");
      return;
    }

    enrolledStudents.forEach(
        student -> consoleManager.print(student.getFirstName() + " " + student.getLastName()));
  }

  public void addNewStudent() {

    try {
      consoleManager.print("Inset first name.");
      String firstName = consoleManager.readLine();

      if (firstName.isEmpty()) {
        consoleManager.print("Invalid input. You have entered an empty string.");
        return;
      }

      consoleManager.print("Insert last name.");
      String lastName = consoleManager.readLine();

      if (lastName.isEmpty()) {
        consoleManager.print("Invalid input. You have entered an empty string.");
        return;
      }

      consoleManager.print("Insert group ID (1 to 10):");
      int groupId = consoleManager.parseInput(consoleManager.readLine());

      validator.validateNumber(groupId);

      if (groupId > 10) {
        consoleManager.print("Group ID should be from 1 to 10.");
        return;
      }

      Student newStudent = Student.builder()
          .withFirstName(firstName)
          .withLastName(lastName)
          .withGroupId(groupId)
          .build();

      studentService.addStudent(newStudent);
      consoleManager.print("New student added.");

    } catch (ValidationException e) {
      consoleManager.print(e.getMessage());
    }
  }

  public void deleteStudent() {

    try {
      consoleManager.print(
          "Enter the ID number of the student you want to delete from the list.");
      int studentId = consoleManager.parseInput(consoleManager.readLine());

      validator.validateNumber(studentId);

      if (studentService.deleteStudent(studentId)) {
        consoleManager.print(
            "Student with ID " + studentId + " has been successfully deleted.");
      } else {
        consoleManager.print("No student found with ID " + studentId + ". No deletion performed.");
      }

    } catch (ValidationException e) {
      consoleManager.print(e.getMessage());
    }
  }

  public void enrollStudentToCourse() {

    try {
      consoleManager.print("Enter the student ID");
      int studentId = consoleManager.parseInput(consoleManager.readLine());

      validator.validateNumber(studentId);

      if (!studentService.getStudentById(studentId).isPresent()) {
        consoleManager.print("Student with ID " + studentId + " does not exist.");
        return;
      }

      List<Course> enrolledCourses = courseService.getEnrolledCoursesForStudent(studentId);
      validator.validateEnrolledCourses(enrolledCourses, studentId);

      StringBuilder result = new StringBuilder(
          "Enrolled courses for student with ID " + studentId + ":\n");
      enrolledCourses.forEach(
          course -> result.append(course.getCourseName()).append("\n"));

      consoleManager.print(result.toString().replaceAll("\n", System.lineSeparator()));

      String courseName = getCourseNameFromInput();
      Optional<Course> courseOptional = getCourseByName(courseName);

      if (!courseOptional.isPresent()) {
        return;
      }

      int courseId = courseOptional.map(Course::getId).orElse(-1);

      if (courseService.checkStudentEnrolledInCourse(studentId, courseId)) {
        consoleManager.print(
            "Student with ID " + studentId + " is already enrolled in this course.");
        return;
      }

      courseService.enrollStudentToCourse(studentId, courseId);
      consoleManager.print("Student successfully added to the course.");

    } catch (ValidationException e) {
      consoleManager.print(e.getMessage());
    }
  }

  public void removeStudentFromCourse() {

    try {
      consoleManager.print("Enter the student ID");
      int studentId = consoleManager.parseInput(consoleManager.readLine());

      validator.validateNumber(studentId);

      if (!studentService.getStudentById(studentId).isPresent()) {
        consoleManager.print("Student with ID " + studentId + " does not exist.");
        return;
      }

      List<Course> enrolledCourses = courseService.getEnrolledCoursesForStudent(studentId);
      validator.validateEnrolledCourses(enrolledCourses, studentId);

      StringBuilder result = new StringBuilder(
          "Enrolled courses for student with ID " + studentId + ":\n");
      enrolledCourses.forEach(
          course -> result.append(course.getCourseName()).append("\n"));

      consoleManager.print(result.toString().replaceAll("\n", System.lineSeparator()));

      String courseName = getCourseNameFromInput();
      Optional<Course> courseOptional = getCourseByName(courseName);

      if (!courseOptional.isPresent()) {
        return;
      }

      int courseId = courseOptional.map(Course::getId).orElse(-1);

      if (!courseService.checkStudentEnrolledInCourse(studentId, courseId)) {
        consoleManager.print(
            "Student with ID " + studentId + " is not enrolled in the " + courseName + " course");
        return;
      }

      courseService.removeStudentFromCourse(studentId, courseId);
      consoleManager.print("Student successfully removed from the course.");

    } catch (ValidationException e) {
      consoleManager.print(e.getMessage());
    }
  }

  private String getCourseNameFromInput() {
    consoleManager.print("Enter a course name");
    return consoleManager.readLine();
  }

  private Optional<Course> getCourseByName(String courseName) {
    Optional<Course> courseOptional = courseService.getCourseIdByName(courseName);
    if (!courseOptional.isPresent()) {
      consoleManager.print("Course with the name '" + courseName + "' does not exist.");
    }
    return courseOptional;
  }

}
