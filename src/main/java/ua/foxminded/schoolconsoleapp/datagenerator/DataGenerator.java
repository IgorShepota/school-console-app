package ua.foxminded.schoolconsoleapp.datagenerator;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class DataGenerator {

  private static final String[] FIRST_NAMES = {
      "James", "Sarah", "Robert", "Emily", "Michael", "Jessica", "William", "Amanda", "David",
      "Ashley", "Brian", "Jennifer", "John", "Nicole", "Matthew", "Elizabeth", "Joseph", "Laura",
      "Charles", "Karen"
  };
  private static final String[] LAST_NAMES = {
      "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
      "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia",
      "Martinez", "Robinson"
  };

  private static final String[] COURSES = {
      "Mathematics", "Biology", "Chemistry", "Physics",
      "History", "Literature", "Geography", "Computer Science", "Art", "Music"
  };

  public String generateGroupName() {
    Random random = new Random();
    char letter1 = (char) ('A' + random.nextInt(26));
    char letter2 = (char) ('A' + random.nextInt(26));
    int number = random.nextInt(100);
    return String.format("%c%c-%02d", letter1, letter2, number);
  }

  public String getRandomFirstName() {
    return FIRST_NAMES[new Random().nextInt(FIRST_NAMES.length)];
  }

  public String getRandomLastName() {
    return LAST_NAMES[new Random().nextInt(LAST_NAMES.length)];
  }

  public String[] getCourses() {
    return COURSES;
  }
}
