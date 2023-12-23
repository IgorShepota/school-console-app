package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.dao.mappers.StudentMapper;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Repository
public class StudentDaoImpl implements StudentDao {

  private static final String ADD_STUDENT_QUERY =
      "INSERT INTO students(first_name, last_name, group_id) VALUES (?, ?, ?);";
  private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?;";
  private static final String FIND_ALL_QUERY = "SELECT * FROM students;";
  private static final String FIND_ALL_WITH_PAGINATION_QUERY = "SELECT * FROM students ORDER BY student_id LIMIT ? OFFSET ?;";
  private static final String FIND_STUDENTS_BY_COURSE_NAME_QUERY =
      "SELECT * FROM students " +
          "JOIN student_courses ON students.student_id = student_courses.student_id " +
          "JOIN courses ON student_courses.course_id = courses.course_id " +
          "WHERE courses.course_name = ?;";
  private static final String UPDATE_STUDENT_QUERY = "UPDATE students "
      + "SET first_name = ?, last_name = ?, group_id = ? "
      + "WHERE student_id = ?;";
  private static final String DELETE_STUDENT_BY_ID_QUERY =
      "DELETE FROM student_courses WHERE student_id = ?;"
          + "DELETE FROM students WHERE student_id = ?;";

  private final JdbcTemplate jdbcTemplate;
  private final StudentMapper studentMapper;

  @Autowired
  public StudentDaoImpl(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.studentMapper = studentMapper;
  }

  @Override
  public List<Student> findStudentsByCourseName(String courseName) {
    return jdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NAME_QUERY, studentMapper, courseName);
  }


  @Override
  public void save(Student student) {
    Object[] params = {student.getFirstName(), student.getLastName(), student.getGroupId()};
    jdbcTemplate.update(ADD_STUDENT_QUERY, params);
  }

  @Override
  public Optional<Student> findById(Integer id) {
    try {
      Student student = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, studentMapper, id);

      return Optional.ofNullable(student);

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Student> findAll() {
    return jdbcTemplate.query(FIND_ALL_QUERY, new StudentMapper());
  }

  @Override
  public List<Student> findAll(Integer page, Integer itemsPerPage) {
    int offset = (page - 1) * itemsPerPage;
    return jdbcTemplate.query(FIND_ALL_WITH_PAGINATION_QUERY, studentMapper, itemsPerPage,
        offset);
  }

  @Override
  public void update(Student student) {
    jdbcTemplate.update(UPDATE_STUDENT_QUERY, student.getFirstName(), student.getLastName(),
        student.getGroupId(), student.getId());
  }

  @Override
  public boolean deleteById(Integer id) {
    return jdbcTemplate.update(DELETE_STUDENT_BY_ID_QUERY, id, id) > 0;
  }

}
