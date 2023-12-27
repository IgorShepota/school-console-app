package ua.foxminded.schoolconsoleapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.schoolconsoleapp.consolemanager.ConsoleManager;
import ua.foxminded.schoolconsoleapp.consolemenu.ConsoleMenu;

@ExtendWith(MockitoExtension.class)
class SchoolFrontControllerTest {

  @Mock
  private ConsoleMenu consoleMenu;

  @Mock
  private SchoolOperations operations;

  @Mock
  ConsoleManager consoleManager;

  @InjectMocks
  private SchoolFrontController controller;

  @Test
  void testRun() {
    when(consoleManager.readLine()).thenReturn("1", "2", "3", "4", "5", "6", "invalid", "exit");

    controller.run();
    verify(operations).findGroupsWithLessOrEqualStudent();
    verify(operations).findStudentsByGroupName();
    verify(operations).addNewStudent();
    verify(operations).deleteStudent();
    verify(operations).enrollStudentToCourse();
    verify(operations).removeStudentFromCourse();
    verify(consoleManager).print("Invalid choice. Please select a valid option.");
    verify(consoleManager).print("Exiting the School Application.");
    verify(consoleMenu, times(8)).drawMenu();
  }

}
