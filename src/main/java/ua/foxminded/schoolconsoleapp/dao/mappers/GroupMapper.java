package ua.foxminded.schoolconsoleapp.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.schoolconsoleapp.dao.mappers.annotation.RowMapperComponent;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@RowMapperComponent
public class GroupMapper implements RowMapper<Group> {

  @Override
  public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Group.builder()
        .id(resultSet.getInt("group_id"))
        .groupName(resultSet.getString("group_name"))
        .build();
  }

}
