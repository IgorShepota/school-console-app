package ua.foxminded.schoolconsoleapp.dao;


import java.util.List;
import ua.foxminded.schoolconsoleapp.entitу.Group;


public interface GroupDao extends CrudDao<Group> {

  List<Group> findGroupsWithLessOrEqualStudent(int maxStudents);

}
