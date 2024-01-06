package ua.foxminded.schoolconsoleapp.validator;

import java.util.List;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.validator.exception.ValidationException;

@Component
public class Validator {

  public void validateEnrolledCourses(List<Course> enrolledCourses, int studentId) {
    if (enrolledCourses.isEmpty()) {
      throw new ValidationException(
          "Student with ID " + studentId + " is not enrolled in any courses.");
    }
  }

  public void validateNumber(int number) {
    if (number == -1) {
      throw new ValidationException("Input is empty. Please enter a number.");
    } else if (number < 0) {
      throw new ValidationException("Invalid input. Please enter a valid number.");
    }
  }
}
