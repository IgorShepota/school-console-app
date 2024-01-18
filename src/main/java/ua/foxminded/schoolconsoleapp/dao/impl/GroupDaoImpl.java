package ua.foxminded.schoolconsoleapp.dao.impl;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@Repository
@RequiredArgsConstructor
public class GroupDaoImpl implements GroupDao {

  private static final String FIND_WITH_LESS_OR_EQUAL_AMOUNT_QUERY = "SELECT g FROM Group g JOIN g.students s GROUP BY g HAVING COUNT(s) <= :maxStudents";
  private static final String FIND_ALL_GROUPS_QUERY = "SELECT g FROM Group g";
  private static final String DELETE_GROUP_QUERY = "DELETE FROM Group WHERE id = :id";

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Group> findGroupsWithLessOrEqualStudent(int maxStudents) {
    TypedQuery<Group> query = entityManager.createQuery(FIND_WITH_LESS_OR_EQUAL_AMOUNT_QUERY,
        Group.class);
    query.setParameter("maxStudents", (long) maxStudents);
    return query.getResultList();
  }

  @Override
  public void save(Group group) {
    entityManager.persist(group);
  }

  @Override
  public Optional<Group> findById(Integer id) {
    Group group = entityManager.find(Group.class, id);
    return Optional.ofNullable(group);
  }

  @Override
  public List<Group> findAll() {
    TypedQuery<Group> query = entityManager.createQuery(FIND_ALL_GROUPS_QUERY, Group.class);
    return query.getResultList();
  }

  @Override
  public List<Group> findAll(Integer page, Integer itemsPerPage) {
    TypedQuery<Group> query = entityManager.createQuery(FIND_ALL_GROUPS_QUERY, Group.class);
    query.setFirstResult((page - 1) * itemsPerPage);
    query.setMaxResults(itemsPerPage);
    return query.getResultList();
  }

  @Override
  public void update(Group group) {
    entityManager.merge(group);
  }

  @Override
  public boolean deleteById(Integer id) {
    Query query = entityManager.createQuery(DELETE_GROUP_QUERY);
    query.setParameter("id", id);
    int result = query.executeUpdate();

    return result > 0;
  }

}
