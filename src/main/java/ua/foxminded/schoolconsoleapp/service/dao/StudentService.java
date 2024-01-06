package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.dao.StudentDao;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

  private final StudentDao studentDao;

  public List<Student> findStudentsByCourseName(String courseName) {
    log.info("Searching for students enrolled in course: {}", courseName);
    return studentDao.findStudentsByCourseName(courseName);
  }

  public void addStudent(Student student) {
    log.info("Adding new student: {}", student);
    studentDao.save(student);
  }

  public Optional<Student> getStudentById(Integer id) {
    log.info("Retrieving student by ID: {}", id);
    return studentDao.findById(id);
  }

  public List<Student> getAllStudents() {
    log.info("Retrieving all students");
    return studentDao.findAll();
  }

  public List<Student> getAllStudents(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all students with pagination: page {}, itemsPerPage {}", page,
        itemsPerPage);
    return studentDao.findAll(page, itemsPerPage);
  }

  public void updateStudent(Student student) {
    log.info("Updating student: {}", student);
    studentDao.update(student);
  }

  @Transactional
  public boolean deleteStudent(Integer id) {
    boolean allStudentCoursesDeleted = studentDao.deleteAllStudentCourses(id);
    boolean studentDeleted = studentDao.deleteById(id);
    boolean result = allStudentCoursesDeleted || studentDeleted;
    log.info("Attempting to delete student with ID {}: {}", id, result ? "success" : "failure");
    return result;
  }

}
