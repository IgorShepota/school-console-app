package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.CourseDao;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Repository
@RequiredArgsConstructor
public class CourseDaoImpl implements CourseDao {

  private static final String CHECK_STUDENT_ON_COURSE_BY_ID_QUERY = "SELECT COUNT(c) FROM Course c JOIN c.students s WHERE s.id = :studentId AND c.id = :courseId";
  private static final String FIND_COURSE_ID_BY_COURSE_NAME_QUERY = "SELECT c FROM Course c WHERE c.courseName = :courseName";
  private static final String GET_ENROLLED_COURSES_QUERY = "SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId";
  private static final String FIND_ALL_QUERY = "SELECT c FROM Course c";
  private static final String DELETE_COURSE_QUERY = "DELETE FROM Course WHERE id = :id";


  @PersistenceContext
  EntityManager entityManager;

  @Override
  public boolean checkStudentEnrolledInCourse(int studentId, int courseId) {
    Long count = entityManager.createQuery(CHECK_STUDENT_ON_COURSE_BY_ID_QUERY, Long.class)
        .setParameter("studentId", studentId)
        .setParameter("courseId", courseId)
        .getSingleResult();
    return count > 0;
  }

  @Override
  public void enrollStudentToCourse(Student student, Course course) {
    student.getCourses().add(course);
    course.getStudents().add(student);

    entityManager.merge(student);
    entityManager.merge(course);
  }

  @Override
  public void removeStudentFromCourse(Student student, Course course) {
    student.getCourses().remove(course);
    course.getStudents().remove(student);

    entityManager.merge(student);
    entityManager.merge(course);
  }


  @Override
  public Optional<Course> getCourseIdByName(String courseName) {
    TypedQuery<Course> query = entityManager.createQuery(FIND_COURSE_ID_BY_COURSE_NAME_QUERY,
        Course.class);
    query.setParameter("courseName", courseName);
    List<Course> results = query.getResultList();
    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }

  @Override
  public List<Course> getEnrolledCoursesForStudent(int studentId) {
    TypedQuery<Course> query = entityManager.createQuery(GET_ENROLLED_COURSES_QUERY, Course.class);
    query.setParameter("studentId", studentId);
    return query.getResultList();
  }

  @Override
  public void save(Course course) {
    entityManager.persist(course);
  }

  @Override
  public Optional<Course> findById(Integer id) {
    Course course = entityManager.find(Course.class, id);
    return Optional.ofNullable(course);
  }

  @Override
  public List<Course> findAll() {
    return entityManager.createQuery(FIND_ALL_QUERY, Course.class).getResultList();
  }

  @Override
  public List<Course> findAll(Integer page, Integer itemsPerPage) {
    TypedQuery<Course> query = entityManager.createQuery(FIND_ALL_QUERY, Course.class);
    query.setFirstResult((page - 1) * itemsPerPage);
    query.setMaxResults(itemsPerPage);
    return query.getResultList();
  }

  @Override
  public void update(Course course) {
    entityManager.merge(course);
  }

  @Override
  public boolean deleteById(Integer id) {
    Query query = entityManager.createQuery(DELETE_COURSE_QUERY);
    query.setParameter("id", id);
    int result = query.executeUpdate();

    return result > 0;
  }

}
