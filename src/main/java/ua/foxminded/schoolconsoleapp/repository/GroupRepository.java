package ua.foxminded.schoolconsoleapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.foxminded.schoolconsoleapp.entitу.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

  @Query("SELECT g FROM Group g JOIN g.students s GROUP BY g HAVING COUNT(s) <= :maxStudents")
  List<Group> findGroupsWithLessOrEqualStudent(long maxStudents);

}
