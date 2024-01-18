package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Repository
public class StudentDaoImpl implements StudentDao {

  private static final String FIND_STUDENTS_BY_COURSE_NAME_QUERY = "SELECT s FROM Student s JOIN s.courses c Where c.courseName = :courseName";
  private static final String FIND_ALL_STUDENTS_QUERY = "SELECT s FROM Student s";
  private static final String DELETE_STUDENT_QUERY = "DELETE FROM Student WHERE id = :id";

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Student> findStudentsByCourseName(String courseName) {
    TypedQuery<Student> query = entityManager.createQuery(FIND_STUDENTS_BY_COURSE_NAME_QUERY,
        Student.class);
    query.setParameter("courseName", courseName);
    return query.getResultList();
  }

  @Override
  public void save(Student student) {
    entityManager.persist(student);
  }

  @Override
  public Optional<Student> findById(Integer id) {
    Student student = entityManager.find(Student.class, id);
    return Optional.ofNullable(student);
  }

  @Override
  public List<Student> findAll() {
    return entityManager.createQuery(FIND_ALL_STUDENTS_QUERY, Student.class).getResultList();
  }

  @Override
  public List<Student> findAll(Integer page, Integer itemsPerPage) {
    TypedQuery<Student> query = entityManager.createQuery(FIND_ALL_STUDENTS_QUERY, Student.class);
    query.setFirstResult((page - 1) * itemsPerPage);
    query.setMaxResults(itemsPerPage);
    return query.getResultList();
  }

  @Override
  public void update(Student student) {
    entityManager.merge(student);
  }

  @Override
  public boolean deleteById(Integer id) {
    Query query = entityManager.createQuery(DELETE_STUDENT_QUERY);
    query.setParameter("id", id);
    int result = query.executeUpdate();

    return result > 0;
  }

  @Override
  public void deleteAllStudentCourses(Student student) {
    student.getCourses().clear();
    entityManager.merge(student);
  }

}
