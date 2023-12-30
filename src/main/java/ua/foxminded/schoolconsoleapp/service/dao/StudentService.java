package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Service
public class
StudentService {

  private final StudentDao studentDao;

  @Autowired
  public StudentService(StudentDao studentDao) {
    this.studentDao = studentDao;
  }

  public List<Student> findStudentsByCourseName(String courseName) {
    return studentDao.findStudentsByCourseName(courseName);
  }

  public void addStudent(Student student) {
    studentDao.save(student);
  }

  public Optional<Student> getStudentById(Integer id) {
    return studentDao.findById(id);
  }

  public List<Student> getAllStudents() {
    return studentDao.findAll();
  }

  public List<Student> getAllStudents(Integer page, Integer itemsPerPage) {
    return studentDao.findAll(page, itemsPerPage);
  }

  public void updateStudent(Student student) {
    studentDao.update(student);
  }

  @Transactional
  public boolean deleteStudent(Integer id) {
    boolean allStudentCoursesDeleted = studentDao.deleteAllStudentCourses(id);
    boolean studentDeleted = studentDao.deleteById(id);
    return allStudentCoursesDeleted && studentDeleted;
  }

}
