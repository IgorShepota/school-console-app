package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@Service
public class GroupService {

  private final GroupDao groupDao;

  @Autowired
  public GroupService(GroupDao groupDao) {
    this.groupDao = groupDao;
  }

  public List<Group> findGroupsWithLessOrEqualStudent(int maxStudents) {
    return groupDao.findGroupsWithLessOrEqualStudent(maxStudents);
  }

  public void addGroup(Group group) {
    groupDao.save(group);
  }

  public Optional<Group> getGroupById(Integer id) {
    return groupDao.findById(id);
  }

  public List<Group> getAllGroups() {
    return groupDao.findAll();
  }

  public List<Group> getAllGroups(Integer page, Integer itemsPerPage) {
    return groupDao.findAll(page, itemsPerPage);
  }

  public void updateGroup(Group group) {
    groupDao.update(group);
  }

  public boolean deleteGroup(Integer id) {
    return groupDao.deleteById(id);
  }

}
