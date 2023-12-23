package ua.foxminded.schoolconsoleapp.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

  @Override
  public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Student.builder()
        .withId(resultSet.getInt("student_id"))
        .withGroupId(resultSet.getInt("group_id"))
        .withFirstName(resultSet.getString("first_name"))
        .withLastName(resultSet.getString("last_name"))
        .build();
  }

}
