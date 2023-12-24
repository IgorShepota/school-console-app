package ua.foxminded.schoolconsoleapp.service.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.datainserter.DataInserter;

@Component
public class DataInserterRunner implements ApplicationRunner {

  private final DataInserter dataInserter;

  @Autowired
  public DataInserterRunner(DataInserter dataInserter) {
    this.dataInserter = dataInserter;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (!dataInserter.isDataInitialized()) {
      dataInserter.insertGroups();
      dataInserter.insertCourses();
      dataInserter.insertStudents();
      dataInserter.assignStudentsToGroups();
      dataInserter.assignCoursesToStudents();
    }
  }

}
