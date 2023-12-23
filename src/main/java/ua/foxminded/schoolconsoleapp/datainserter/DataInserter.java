package ua.foxminded.schoolconsoleapp.datainserter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.datagenerator.DataGenerator;

@Component
public class DataInserter {

  private final DataGenerator dataGenerator;

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public DataInserter(JdbcTemplate jdbcTemplate, DataGenerator dataGenerator) {
    this.dataGenerator = dataGenerator;
    this.jdbcTemplate = jdbcTemplate;
  }

  public boolean isDataInitialized() {
    String checkGroups = "SELECT EXISTS (SELECT 1 FROM groups)";
    String checkStudents = "SELECT EXISTS (SELECT 1 FROM students)";
    String checkCourses = "SELECT EXISTS (SELECT 1 FROM courses)";

    boolean groupsExist = jdbcTemplate.queryForObject(checkGroups, Boolean.class);
    boolean studentsExist = jdbcTemplate.queryForObject(checkStudents, Boolean.class);
    boolean coursesExist = jdbcTemplate.queryForObject(checkCourses, Boolean.class);

    return groupsExist && studentsExist && coursesExist;
  }

  @Transactional
  public void insertGroups() {
    String sql = "INSERT INTO groups (group_name) VALUES (?);";
    List<Object[]> batchArgs = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      String groupName = dataGenerator.generateGroupName();
      batchArgs.add(new Object[] { groupName });
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
  }

  @Transactional
  public void insertCourses() {
    String sql = "INSERT INTO courses (course_name) VALUES (?);";
    String[] courseNames = dataGenerator.getCourses();

    List<Object[]> batchArgs = new ArrayList<>();
    for (String courseName : courseNames) {
      batchArgs.add(new Object[] { courseName });
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
  }

  @Transactional
  public void insertStudents() {
    String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
    List<Object[]> batchArgs = new ArrayList<>();
    for (int i = 0; i < 200; i++) {
      String firstName = dataGenerator.getRandomFirstName();
      String lastName = dataGenerator.getRandomLastName();
      batchArgs.add(new Object[]{firstName, lastName});
    }
    jdbcTemplate.batchUpdate(sql, batchArgs);
  }

  @Transactional
  public void assignStudentsToGroups() {
    List<Integer> groupIds = jdbcTemplate.queryForList("SELECT group_id FROM groups",
        Integer.class);
    List<Object[]> batchArgs = new ArrayList<>();
    Random random = new Random();

    for (int i = 1; i <= 200; i++) {
      if (random.nextInt(100) < 90) {
        Integer randomGroupIndex = random.nextInt(groupIds.size());
        batchArgs.add(new Object[]{groupIds.get(randomGroupIndex), i});
      }
    }

    jdbcTemplate.batchUpdate("UPDATE students SET group_id = ? WHERE student_id = ?", batchArgs);
  }

  @Transactional
  public void assignCoursesToStudents() {
    List<Integer> studentsIds = retrieveStudentsIds();
    List<Integer> coursesIds = retrieveCoursesIds();
    List<Object[]> batchArgs = new ArrayList<>();
    Random random = new Random();

    for (int studentId : studentsIds) {
      int numberOfCourses = random.nextInt(3) + 1;
      List<Integer> randomCourseIndices = new ArrayList<>();
      while (randomCourseIndices.size() < numberOfCourses) {
        int randomIndex = random.nextInt(coursesIds.size());
        if (!randomCourseIndices.contains(randomIndex)) {
          randomCourseIndices.add(randomIndex);
        }
      }

      for (int randomIndex : randomCourseIndices) {
        batchArgs.add(new Object[]{studentId, coursesIds.get(randomIndex)});
      }
    }

    jdbcTemplate.batchUpdate("INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)",
        batchArgs);
  }

  private List<Integer> retrieveStudentsIds() {
    return jdbcTemplate.queryForList("SELECT student_id FROM students", Integer.class);
  }

  private List<Integer> retrieveCoursesIds() {
    return jdbcTemplate.queryForList("SELECT course_id FROM courses", Integer.class);
  }

}
