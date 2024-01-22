package ua.foxminded.schoolconsoleapp.service.dao;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.schoolconsoleapp.entitу.Group;
import ua.foxminded.schoolconsoleapp.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

  private final GroupRepository groupRepository;

  public List<Group> findGroupsWithLessOrEqualStudent(int maxStudents) {
    log.info("Finding groups with less or equal to {} students", maxStudents);
    return groupRepository.findGroupsWithLessOrEqualStudent(maxStudents);
  }

  @Transactional
  public void addGroup(Group group) {
    log.info("Adding new group: {}", group);
    groupRepository.save(group);
  }

  public Optional<Group> getGroupById(Integer id) {
    log.info("Retrieving group by ID: {}", id);
    return groupRepository.findById(id);
  }

  public List<Group> getAllGroups() {
    log.info("Retrieving all groups");
    return groupRepository.findAll();
  }

  public List<Group> getAllGroups(Integer page, Integer itemsPerPage) {
    log.info("Retrieving all groups with pagination: page {}, itemsPerPage {}", page, itemsPerPage);
    Pageable pageable = Pageable.ofSize(itemsPerPage).withPage(page - 1);

    return groupRepository.findAll(pageable).getContent();
  }

  @Transactional
  public void updateGroup(Group group) {
    log.info("Updating group: {}", group);
    groupRepository.save(group);
  }

  @Transactional
  public boolean deleteGroup(Integer id) {
    if (groupRepository.existsById(id)) {
      groupRepository.deleteById(id);
      log.info("Group with id {} was successfully deleted.", id);
      return true;
    } else {
      log.info("Group with id {} not found.", id);
      return false;
    }
  }

}
