package ua.foxminded.schoolconsoleapp.service.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.foxminded.schoolconsoleapp.entitу.Group;
import ua.foxminded.schoolconsoleapp.repository.GroupRepository;

@SpringBootTest
class GroupServiceTest {

  @MockBean
  private GroupRepository groupRepository;

  @Autowired
  private GroupService groupService;

  @Test
  void findGroupsWithLessOrEqualStudentShouldReturnCorrectData() {
    int maxStudents = 10;
    List<Group> mockGroups = Arrays.asList(
        Group.builder().withId(1).withGroupName("Group A").build(),
        Group.builder().withId(2).withGroupName("Group B").build()
    );

    when(groupRepository.findGroupsWithLessOrEqualStudent(maxStudents)).thenReturn(mockGroups);

    List<Group> groups = groupService.findGroupsWithLessOrEqualStudent(maxStudents);

    assertThat(groups).hasSize(2)
        .isEqualTo(mockGroups);
  }

  @Test
  void addGroupShouldWorkCorrectlyIfGroupEntityCorrect() {
    Group group = Group.builder().withId(1).withGroupName("Group A").build();

    groupService.addGroup(group);

    verify(groupRepository).save(group);
  }

  @Test
  void getGroupByIdShouldReturnGroupIfDataCorrect() {
    Integer groupId = 1;
    Group mockGroup = Group.builder().withId(groupId).withGroupName("Group A").build();

    when(groupRepository.findById(groupId)).thenReturn(Optional.of(mockGroup));

    Optional<Group> group = groupService.getGroupById(groupId);

    assertThat(group).isPresent()
        .contains(mockGroup);
  }

  @Test
  void getAllGroupsShouldReturnAllGroupsIfGroupsExist() {
    List<Group> mockGroups = Arrays.asList(
        Group.builder().withId(1).withGroupName("Group A").build(),
        Group.builder().withId(2).withGroupName("Group B").build()
    );

    when(groupRepository.findAll()).thenReturn(mockGroups);

    List<Group> groups = groupService.getAllGroups();

    assertThat(groups).hasSize(2).
        isEqualTo(mockGroups);
  }

  @Test
  void getAllGroupsWithPaginationShouldReturnCorrectData() {
    Group groupA = Group.builder()
        .withId(1)
        .withGroupName("Group A")
        .build();

    List<Group> mockGroups = Collections.singletonList(groupA);
    Page<Group> groupPage = new PageImpl<>(mockGroups);

    when(groupRepository.findAll(any(Pageable.class))).thenReturn(groupPage);

    List<Group> groups = groupService.getAllGroups(1, 1);

    assertThat(groups).hasSize(1).containsExactly(groupA);

    verify(groupRepository).findAll(PageRequest.of(0, 1));
  }

  @Test
  void updateGroupShouldCallDaoUpdateMethod() {
    Group group = Group.builder().withId(1).withGroupName("Group A").build();

    groupService.updateGroup(group);

    verify(groupRepository).save(group);
  }

  @Test
  void deleteGroupShouldReturnTrueWhenGroupExists() {
    Integer groupId = 1;
    when(groupRepository.existsById(groupId)).thenReturn(true);

    boolean result = groupService.deleteGroup(groupId);
    assertThat(result).isTrue();

    verify(groupRepository).deleteById(groupId);
  }

  @Test
  void deleteGroupShouldReturnFalseWhenGroupDoesNotExist() {
    Integer groupId = 1;
    when(groupRepository.existsById(groupId)).thenReturn(false);

    boolean result = groupService.deleteGroup(groupId);
    assertThat(result).isFalse();

    verify(groupRepository, never()).deleteById(groupId);
  }

}
