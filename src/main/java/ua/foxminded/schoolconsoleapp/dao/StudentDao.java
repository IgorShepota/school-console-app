package ua.foxminded.schoolconsoleapp.dao;


import java.util.List;
import ua.foxminded.schoolconsoleapp.entitу.Student;

public interface StudentDao extends CrudDao<Student> {

  List<Student> findStudentsByCourseName(String courseName);

}
