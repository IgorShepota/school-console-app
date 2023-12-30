package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.dao.TestBase;
import ua.foxminded.schoolconsoleapp.entitу.Group;

@ExtendWith(MockitoExtension.class)
class GroupDaoImplTest extends TestBase {

  @Test
  @Tag("database")
  void findGroupsWithLessOrEqualStudentShouldWorkCorrectlyIfGroupsExist() {
    int maxStudents = 1;

    List<Group> groups = groupDao.findGroupsWithLessOrEqualStudent(maxStudents);

    assertThat(groups).isNotEmpty()
        .hasSize(2)
        .allSatisfy(group -> assertThat(group.getId()).isIn(2, 3));
  }

  @Test
  @Tag("database")
  void saveShouldWorkCorrectlyWhileGroupEntityIsCorrect() {
    Group group = Group.builder()
        .withId(4)
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
  @Tag("database")
  void findByIdShouldWorkCorrectlyIfGroupExists() {
    int groupIdToFind = 1;

    assertThat(groupDao.findById(groupIdToFind)).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(1);
          assertThat(retrieved.getGroupName()).isEqualTo("XV-46");
        });
  }

  @Test
  @Tag("database")
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
  @Tag("database")
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
  @Tag("database")
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
  @Tag("database")
  void deleteShouldWorkCorrectlyIfGroupExists() {
    int groupId = 3;
    groupDao.deleteById(groupId);

    assertThat(groupDao.findById(groupId)).isNotPresent();
  }

  @Test
  @Tag("database")
  void deleteShouldWorkCorrectlyIfGroupNotExists() {
    int groupId = 50;

    assertThat(groupDao.deleteById(groupId)).isFalse();
  }

}
