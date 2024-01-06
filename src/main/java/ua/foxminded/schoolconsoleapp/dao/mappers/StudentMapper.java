package ua.foxminded.schoolconsoleapp.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.schoolconsoleapp.dao.mappers.annotation.RowMapperComponent;
import ua.foxminded.schoolconsoleapp.entitу.Student;

@RowMapperComponent
public class StudentMapper implements RowMapper<Student> {

  @Override
  public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Student.builder()
        .id(resultSet.getInt("student_id"))
        .groupId(resultSet.getInt("group_id"))
        .firstName(resultSet.getString("first_name"))
        .lastName(resultSet.getString("last_name"))
        .build();
  }

}
