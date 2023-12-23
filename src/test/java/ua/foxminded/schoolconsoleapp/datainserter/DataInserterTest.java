package ua.foxminded.schoolconsoleapp.datainserter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.schoolconsoleapp.datagenerator.DataGenerator;


@ExtendWith(MockitoExtension.class)
class DataInserterTest {

  @Mock
  private DataGenerator dataGenerator;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private DataInserter dataInserter;


  @Test
  void isDataInitializedShouldReturnTrueWhenDataIsInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(true);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isTrue();
  }

  @Test
  void isDataInitializedShouldReturnFalseWhenDataIsNotInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(false);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(false);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(false);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isFalse();
  }

  @Test
  void isDataInitializedShouldReturnFalseWhenOnlyGroupsInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(false);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(false);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isFalse();
  }

  @Test
  void isDataInitializedShouldReturnFalseWhenOnlyGroupsAndCoursesInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(false);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(true);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isFalse();
  }

  @Test
  void isDataInitializedShouldReturnFalseWhenOnlyGroupsAndStudentsInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(false);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isFalse();
  }

  @Test
  void isDataInitializedShouldReturnFalseWhenOnlyStudentsAndCoursesInitialized() {
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM groups)",
        Boolean.class)).thenReturn(false);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM students)",
        Boolean.class)).thenReturn(true);
    when(jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM courses)",
        Boolean.class)).thenReturn(true);

    boolean isInitialized = dataInserter.isDataInitialized();

    assertThat(isInitialized).isFalse();
  }

  @Test
  void insertGroupsShouldWorkCorrectlyWhenDataCorrect() {
    int numberOfGroups = 10;
    when(dataGenerator.generateGroupName()).thenReturn("Group1", "Group2", "Group3", "Group4",
        "Group5",
        "Group6", "Group7", "Group8", "Group9", "Group10");

    dataInserter.insertGroups();

    verify(dataGenerator, times(numberOfGroups)).generateGroupName();
    verify(jdbcTemplate, times(1)).batchUpdate(anyString(), any(List.class));
  }

  @Test
  void insertCoursesShouldWorkCorrectlyWhenDataCorrect() {
    String[] mockCourseNames = new String[]{"Math", "Physics", "Chemistry"};
    when(dataGenerator.getCourses()).thenReturn(mockCourseNames);

    dataInserter.insertCourses();

    verify(dataGenerator, times(1)).getCourses();
    verify(jdbcTemplate, times(1)).batchUpdate(eq("INSERT INTO courses (course_name) VALUES (?);"),
        argThat((ArgumentMatcher<List<Object[]>>) argument -> argument.size()
            == mockCourseNames.length));
  }

  @Test
  void insertStudentsShouldWorkCorrectlyWhenDataCorrect() {
    when(dataGenerator.getRandomFirstName()).thenReturn("John", "Jane");
    when(dataGenerator.getRandomLastName()).thenReturn("Doe", "Doe");

    dataInserter.insertStudents();

    verify(dataGenerator, times(200)).getRandomFirstName();
    verify(dataGenerator, times(200)).getRandomLastName();
    verify(jdbcTemplate, times(1)).batchUpdate(
        eq("INSERT INTO students (first_name, last_name) VALUES (?, ?)"),
        argThat((ArgumentMatcher<List<Object[]>>) argument -> argument.size() == 200));
  }

  @Test
  void assignStudentsToGroupsShouldWorkCorrectlyIfDataCorrect() {
    List<Integer> mockGroupIds = Arrays.asList(1, 2, 3);
    when(jdbcTemplate.queryForList(anyString(), eq(Integer.class))).thenReturn(mockGroupIds);

    dataInserter.assignStudentsToGroups();

    verify(jdbcTemplate, times(1)).queryForList(eq("SELECT group_id FROM groups"), eq(Integer.class));
    verify(jdbcTemplate, times(1)).batchUpdate(eq("UPDATE students SET group_id = ? WHERE student_id = ?"),
        argThat((ArgumentMatcher<List<Object[]>>) argument -> argument.size() <= 200));
  }

  @Test
  void assignCoursesToStudentsShouldWorkCorrectlyIfDataCorrect() {
    List<Integer> mockStudentIds = Arrays.asList(1, 2, 3, 4);
    List<Integer> mockCourseIds = Arrays.asList(101, 102, 103);
    when(jdbcTemplate.queryForList(eq("SELECT student_id FROM students"), eq(Integer.class)))
        .thenReturn(mockStudentIds);
    when(jdbcTemplate.queryForList(eq("SELECT course_id FROM courses"), eq(Integer.class)))
        .thenReturn(mockCourseIds);

    dataInserter.assignCoursesToStudents();

    verify(jdbcTemplate, times(1)).queryForList(eq("SELECT student_id FROM students"), eq(Integer.class));
    verify(jdbcTemplate, times(1)).queryForList(eq("SELECT course_id FROM courses"), eq(Integer.class));
    verify(jdbcTemplate, times(1)).batchUpdate(eq("INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)"),
        argThat((ArgumentMatcher<List<Object[]>>) argument ->
            argument.size() >= mockStudentIds.size() && argument.size() <= mockStudentIds.size() * 3));
  }

}
