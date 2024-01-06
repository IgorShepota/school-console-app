package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

  private final GroupDao groupDao;

  public List<Group> findGroupsWithLessOrEqualStudent(int maxStudents) {
    log.info("Finding groups with less or equal to {} students", maxStudents);
    return groupDao.findGroupsWithLessOrEqualStudent(maxStudents);
  }

  public void addGroup(Group group) {
    log.info("Adding new group: {}", group);
    groupDao.save(group);
  }

  public Optional<Group> getGroupById(Integer id) {
    log.info("Retrieving group by ID: {}", id);
    return groupDao.findById(id);
  }

  public List<Group> getAllGroups() {
    log.info("Retrieving all groups");
    return groupDao.findAll();
  }

  public List<Group> getAllGroups(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all groups with pagination: page {}, itemsPerPage {}", page, itemsPerPage);
    return groupDao.findAll(page, itemsPerPage);
  }

  public void updateGroup(Group group) {
    log.info("Updating group: {}", group);
    groupDao.update(group);
  }

  public boolean deleteGroup(Integer id) {
    boolean isDeleted = groupDao.deleteById(id);
    log.info("Deleting group with id {}: {}", id, isDeleted ? "success" : "failure");
    return isDeleted;
  }

}
