package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.entitу.Student;
import ua.foxminded.schoolconsoleapp.repository.StudentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

  private final StudentRepository studentRepository;

  public List<Student> findStudentsByCourseName(String courseName) {
    log.info("Searching for students enrolled in course: {}", courseName);
    return studentRepository.findStudentsByCourseName(courseName);
  }

  @Transactional
  public void addStudent(Student student) {
    log.info("Adding new student: {}", student);
    studentRepository.save(student);
  }

  public Optional<Student> getStudentById(Integer id) {
    log.info("Retrieving student by ID: {}", id);
    return studentRepository.findById(id);
  }

  public List<Student> getAllStudents() {
    log.info("Retrieving all students");
    return studentRepository.findAll();
  }

  public List<Student> getAllStudents(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all students with pagination: page {}, itemsPerPage {}", page,
        itemsPerPage);
    Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.by("id"));
    Page<Student> studentPage = studentRepository.findAll(pageable);

    return studentPage.getContent();
  }

  @Transactional
  public void updateStudent(Student student) {
    log.info("Updating student: {}", student);
    studentRepository.save(student);
  }

  @Transactional
  public boolean deleteStudent(Integer id) {
    if (studentRepository.existsById(id)) {
      studentRepository.deleteById(id);
      log.info("Student with ID {} was successfully deleted.", id);
      return true;
    } else {
      log.info("Student with ID {} not found.", id);
      return false;
    }
  }

}
