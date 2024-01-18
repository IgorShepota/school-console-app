package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.DaoTestBase;
import ua.foxminded.schoolconsoleapp.entitу.Group;

class GroupDaoImplTest extends DaoTestBase {

  @Test
  void findGroupsWithLessOrEqualStudentShouldWorkCorrectlyIfGroupsExist() {
    int maxStudents = 1;

    List<Group> groups = groupDao.findGroupsWithLessOrEqualStudent(maxStudents);

    assertThat(groups).isNotEmpty()
        .hasSize(1)
        .allSatisfy(group -> assertThat(group.getId()).isIn(2));
  }

  @Test
  void saveShouldWorkCorrectlyWhileGroupEntityIsCorrect() {
    Group group = Group.builder()
        .withGroupName("AA-01")
        .build();

    groupDao.save(group);

    assertThat(groupDao.findById(group.getId())).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(group.getId());
          assertThat(retrieved.getGroupName()).isEqualTo(group.getGroupName());
        });
  }

  @Test
  void findByIdShouldWorkCorrectlyIfGroupExists() {
    int groupIdToFind = 1;

    assertThat(groupDao.findById(groupIdToFind)).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(1);
          assertThat(retrieved.getGroupName()).isEqualTo("XV-46");
        });
  }

  @Test
  void findAllShouldWorkCorrectlyIfGroupsExist() {
    List<Group> groups = groupDao.findAll();
    assertThat(groups).isNotEmpty()
        .hasSize(3)
        .allSatisfy(group -> {
          assertThat(group.getId()).isNotNull();
          assertThat(group.getGroupName()).isNotNull();
        });
  }

  @Test
  void findAllWithPaginationShouldWorkCorrectlyIfGroupsExist() {
    List<Group> groups = groupDao.findAll(1, 1);

    assertThat(groups).isNotEmpty()
        .hasSize(1)
        .allSatisfy(group -> {
          assertThat(group.getId()).isNotNull();
          assertThat(group.getGroupName()).isNotNull();
        });
  }

  @Test
  void updateShouldWorkCorrectlyIfGroupExists() {
    Group group = Group.builder()
        .withId(1)
        .withGroupName("AA-01")
        .build();

    groupDao.update(group);
    assertThat(groupDao.findById(group.getId())).isPresent()
        .hasValueSatisfying(updatedGroup -> {
          assertThat(updatedGroup.getId()).isEqualTo(group.getId());
          assertThat(updatedGroup.getGroupName()).isEqualTo(group.getGroupName());
        });
  }

  @Test
  void deleteShouldWorkCorrectlyIfGroupExists() {
    int groupId = 3;
    groupDao.deleteById(groupId);

    assertThat(groupDao.findById(groupId)).isNotPresent();
  }

  @Test
  void deleteShouldWorkCorrectlyIfGroupNotExists() {
    int groupId = 50;

    assertThat(groupDao.deleteById(groupId)).isFalse();
  }

}
