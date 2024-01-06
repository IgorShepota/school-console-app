package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.dao.exception.DataBaseSqlRuntimeException;
import ua.foxminded.schoolconsoleapp.dao.mappers.CourseMapper;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@Repository
@RequiredArgsConstructor
public class CourseDaoImpl implements CourseDao {

  private static final String ADD_COURSE_QUERY = "INSERT INTO courses(course_name) VALUES (?);";
  private static final String CHECK_STUDENT_ON_COURSE_BY_ID_QUERY =
      "SELECT EXISTS (SELECT 1 FROM student_courses WHERE student_id = ? AND course_id = ?)";
  private static final String ADD_STUDENT_TO_COURSE_QUERY =
      "INSERT INTO student_courses (student_id, course_id) "
          + "VALUES (?, ?);";
  private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?;";
  private static final String FIND_ALL_QUERY = "SELECT * FROM courses;";
  private static final String FIND_ALL_WITH_PAGINATION_QUERY = "SELECT * FROM courses ORDER BY course_id LIMIT ? OFFSET ?;";
  private static final String FIND_COURSE_ID_BY_COURSE_NAME_QUERY = "SELECT course_id, course_name "
      + "FROM courses WHERE course_name = ?;";
  private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET course_name = ? WHERE course_id = ?;";
  private static final String DELETE_STUDENT_FROM_COURSE_QUERY = "DELETE FROM student_courses "
      + "WHERE student_id = ? AND course_id = ?;";
  private static final String DELETE_COURSE_BY_ID_QUERY =
      "DELETE FROM student_courses WHERE course_id = ?;"
          + "DELETE FROM courses WHERE course_id = ?;";
  private static final String GET_ENROLLED_COURSES_QUERY =
      "SELECT courses.course_name, courses.course_id FROM courses "
          + "JOIN student_courses ON courses.course_id = student_courses.course_id "
          + "WHERE student_courses.student_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final CourseMapper courseMapper;

  @Override
  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    try {
      Boolean isEnrolled = jdbcTemplate.queryForObject(CHECK_STUDENT_ON_COURSE_BY_ID_QUERY,
          Boolean.class, studentId, courseId);
      return Boolean.TRUE.equals(isEnrolled);
    } catch (DataAccessException e) {
      throw new DataBaseSqlRuntimeException(
          "Error occurred while checking if the student with ID " + studentId
              + " is enrolled in course with ID " + courseId + ".", e);
    }
  }

  @Override
  public void enrollStudentToCourse(int studentId, int courseId) {
    try {
      jdbcTemplate.update(ADD_STUDENT_TO_COURSE_QUERY, studentId, courseId);
    } catch (DataAccessException e) {
      throw new DataBaseSqlRuntimeException(
          "Error occurred while adding student to the course.", e);
    }
  }

  @Override
  public void removeStudentFromCourse(int studentId, int courseId) {
    try {
      jdbcTemplate.update(DELETE_STUDENT_FROM_COURSE_QUERY, studentId, courseId);
    } catch (DataAccessException e) {
      throw new DataBaseSqlRuntimeException(
          "Error occurred while removing student from the course.", e);
    }
  }

  @Override
  public Optional<Course> getCourseIdByName(String courseName) {
    try {
      Course course = jdbcTemplate.queryForObject(FIND_COURSE_ID_BY_COURSE_NAME_QUERY,
          courseMapper, courseName);
      return Optional.ofNullable(course);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    } catch (DataAccessException e) {
      throw new DataBaseSqlRuntimeException("Error while getting course ID by name.", e);
    }
  }

  @Override
  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    try {
      return jdbcTemplate.query(GET_ENROLLED_COURSES_QUERY, courseMapper, studentId);
    } catch (DataAccessException e) {
      throw new DataBaseSqlRuntimeException(
          "Error while retrieving enrolled courses for student with ID " + studentId, e);
    }
  }

  @Override
  public void save(Course course) {
    jdbcTemplate.update(ADD_COURSE_QUERY, course.getCourseName());
  }

  @Override
  public Optional<Course> findById(Integer id) {
    try {
      Course course = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, courseMapper, id);

      return Optional.ofNullable(course);

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Course> findAll() {
    return jdbcTemplate.query(FIND_ALL_QUERY, new CourseMapper());
  }

  @Override
  public List<Course> findAll(Integer page, Integer itemsPerPage) {
    int offset = (page - 1) * itemsPerPage;
    return jdbcTemplate.query(FIND_ALL_WITH_PAGINATION_QUERY, courseMapper, itemsPerPage,
        offset);
  }

  @Override
  public void update(Course course) {
    jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getCourseName(), course.getId());
  }

  @Override
  public boolean deleteById(Integer id) {
    return jdbcTemplate.update(DELETE_COURSE_BY_ID_QUERY, id, id) > 0;
  }

}
