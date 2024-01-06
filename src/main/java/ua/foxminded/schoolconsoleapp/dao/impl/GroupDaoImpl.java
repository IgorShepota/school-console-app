package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.dao.mappers.GroupMapper;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@Repository
@RequiredArgsConstructor
public class GroupDaoImpl implements GroupDao {

  private static final String ADD_GROUP_QUERY = "INSERT INTO groups(group_name) VALUES (?);";
  private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?;";
  private static final String FIND_ALL_QUERY = "SELECT * FROM groups;";
  private static final String FIND_ALL_WITH_PAGINATION_QUERY = "SELECT * FROM groups ORDER BY group_id LIMIT ? OFFSET ?;";
  private static final String FIND_WITH_LESS_OR_EQUAL_AMOUNT =
      "SELECT groups.group_id, groups.group_name FROM groups " +
          "LEFT JOIN students ON groups.group_id = students.group_id " +
          "GROUP BY groups.group_id, groups.group_name " +
          "HAVING COUNT(students.student_id) <= ?;";
  private static final String UPDATE_GROUP_QUERY = "UPDATE groups "
      + "SET group_name = ? "
      + "WHERE group_id = ?;";
  private static final String DELETE_GROUP_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?;";

  private final JdbcTemplate jdbcTemplate;
  private final GroupMapper groupMapper;

  @Override
  public List<Group> findGroupsWithLessOrEqualStudent(int maxStudents) {
    return jdbcTemplate.query(FIND_WITH_LESS_OR_EQUAL_AMOUNT, groupMapper, maxStudents);
  }

  @Override
  public void save(Group group) {
    jdbcTemplate.update(ADD_GROUP_QUERY, group.getGroupName());
  }

  @Override
  public Optional<Group> findById(Integer id) {
    try {
      Group group = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, groupMapper, id);

      return Optional.ofNullable(group);

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Group> findAll() {
    return jdbcTemplate.query(FIND_ALL_QUERY, new GroupMapper());
  }

  @Override
  public List<Group> findAll(Integer page, Integer itemsPerPage) {
    int offset = (page - 1) * itemsPerPage;
    return jdbcTemplate.query(FIND_ALL_WITH_PAGINATION_QUERY, groupMapper, itemsPerPage,
        offset);
  }

  @Override
  public void update(Group group) {
    jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getGroupName(), group.getId());
  }

  @Override
  public boolean deleteById(Integer id) {
    return jdbcTemplate.update(DELETE_GROUP_BY_ID_QUERY, id) > 0;
  }

}
