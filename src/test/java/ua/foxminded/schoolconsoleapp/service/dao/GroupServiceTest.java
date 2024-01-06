package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.schoolconsoleapp.dao.GroupDao;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@SpringBootTest
class GroupServiceTest {

  @MockBean
  private GroupDao groupDao;

  @Autowired
  private GroupService groupService;

  @Test
  void findGroupsWithLessOrEqualStudentShouldReturnCorrectData() {
    int maxStudents = 10;
    List<Group> mockGroups = Arrays.asList(
        Group.builder().id(1).groupName("Group A").build(),
        Group.builder().id(2).groupName("Group B").build()
    );

    when(groupDao.findGroupsWithLessOrEqualStudent(maxStudents)).thenReturn(mockGroups);

    List<Group> groups = groupService.findGroupsWithLessOrEqualStudent(maxStudents);

    assertThat(groups).hasSize(2)
        .isEqualTo(mockGroups);
  }

  @Test
  void addGroupShouldWorkCorrectlyIfGroupEntityCorrect() {
    Group group = Group.builder().id(1).groupName("Group A").build();

    groupService.addGroup(group);

    verify(groupDao).save(group);
  }

  @Test
  void getGroupByIdShouldReturnGroupIfDataCorrect() {
    Integer groupId = 1;
    Group mockGroup = Group.builder().id(groupId).groupName("Group A").build();

    when(groupDao.findById(groupId)).thenReturn(Optional.of(mockGroup));

    Optional<Group> group = groupService.getGroupById(groupId);

    assertThat(group).isPresent()
        .contains(mockGroup);
  }

  @Test
  void getAllGroupsShouldReturnAllGroupsIfGroupsExist() {
    List<Group> mockGroups = Arrays.asList(
        Group.builder().id(1).groupName("Group A").build(),
        Group.builder().id(2).groupName("Group B").build()
    );

    when(groupDao.findAll()).thenReturn(mockGroups);

    List<Group> groups = groupService.getAllGroups();

    assertThat(groups).hasSize(2).
        isEqualTo(mockGroups);
  }

  @Test
  void getAllGroupsWithPaginationShouldReturnCorrectData() {
    List<Group> mockGroups = Collections.singletonList(
        Group.builder().id(1).groupName("Group A").build()
    );

    when(groupDao.findAll(1, 1)).thenReturn(mockGroups);

    List<Group> groups = groupService.getAllGroups(1, 1);

    assertThat(groups).hasSize(1)
        .isEqualTo(mockGroups);
  }

  @Test
  void updateGroupShouldCallDaoUpdateMethod() {
    Group group = Group.builder().id(1).groupName("Group A").build();

    groupService.updateGroup(group);

    verify(groupDao).update(group);
  }

  @Test
  void deleteGroupShouldReturnTrueIfGroupDeleted() {
    Integer groupId = 1;
    when(groupDao.deleteById(groupId)).thenReturn(true);

    boolean result = groupService.deleteGroup(groupId);

    assertThat(result).isTrue();
  }

  @Test
  void deleteGroupShouldReturnFalseIfGroupNotDeleted() {
    Integer groupId = 999;
    when(groupDao.deleteById(groupId)).thenReturn(false);

    boolean result = groupService.deleteGroup(groupId);

    assertThat(result).isFalse();
  }

}
