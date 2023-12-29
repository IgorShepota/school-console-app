package ua.foxminded.schoolconsoleapp.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.dao.mappers.annotation.RowMapperComponent;
import ua.foxminded.schoolconsoleapp.entitу.Course;

@RowMapperComponent
public class CourseMapper implements RowMapper<Course> {

  @Override
  public Course mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Course.builder()
        .withId(resultSet.getInt("course_id"))
        .withCourseName(resultSet.getString("course_name"))
        .build();
  }

}
