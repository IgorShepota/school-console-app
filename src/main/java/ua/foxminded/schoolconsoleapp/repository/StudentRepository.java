package ua.foxminded.schoolconsoleapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

  @Query("SELECT s FROM Student s JOIN s.courses c Where c.courseName = :courseName")
  List<Student> findStudentsByCourseName(String courseName);

}
