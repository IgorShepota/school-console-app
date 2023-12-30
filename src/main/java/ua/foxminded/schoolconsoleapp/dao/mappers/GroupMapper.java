package ua.foxminded.schoolconsoleapp.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.dao.mappers.annotation.RowMapperComponent;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@RowMapperComponent
public class GroupMapper implements RowMapper<Group> {

  @Override
  public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Group.builder()
        .withId(resultSet.getInt("group_id"))
        .withGroupName(resultSet.getString("group_name"))
        .build();
  }

}
