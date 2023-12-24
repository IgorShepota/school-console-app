//package ua.foxminded.schoolconsoleapp;
//
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ua.foxminded.schoolconsoleapp.consolemanager.ConsoleManager;
//import ua.foxminded.schoolconsoleapp.consolemenu.ConsoleMenu;
//import ua.foxminded.schoolconsoleapp.datainserter.DataInserter;
//
//@ExtendWith(MockitoExtension.class)
//class SchoolFrontControllerTest {
//
//  @Mock
//  private DataInserter dataInserter;
//
//  @Mock
//  private ConsoleMenu consoleMenu;
//
//  @Mock
//  private SchoolOperations operations;
//
//  @Mock
//  ConsoleManager consoleManager;
//
//  @InjectMocks
//  private SchoolFrontController controller;
//
//  @Test
//  void initializeShouldInsertDataWhenNotInitialized() {
//    when(dataInserter.isDataInitialized()).thenReturn(false);
//
//    controller.initialize();
//
//    verify(dataInserter, times(1)).insertGroups();
//    verify(dataInserter, times(1)).insertCourses();
//    verify(dataInserter, times(1)).insertStudents();
//    verify(dataInserter, times(1)).assignStudentsToGroups();
//    verify(dataInserter, times(1)).assignCoursesToStudents();
//  }
//
//  @Test
//  void initializeShouldNotInsertDataWhenAlreadyInitialized() {
//    when(dataInserter.isDataInitialized()).thenReturn(true);
//
//    controller.initialize();
//
//    verify(dataInserter, never()).insertGroups();
//    verify(dataInserter, never()).insertCourses();
//  }
//
//  @Test
//  void testRun() {
//    when(consoleManager.readLine()).thenReturn("1", "2", "3", "4", "5", "6", "invalid", "exit");
//
//    controller.run();
//    verify(operations).findGroupsWithLessOrEqualStudent();
//    verify(operations).findStudentsByGroupName();
//    verify(operations).addNewStudent();
//    verify(operations).deleteStudent();
//    verify(operations).enrollStudentToCourse();
//    verify(operations).removeStudentFromCourse();
//    verify(consoleManager).print("Invalid choice. Please select a valid option.");
//    verify(consoleManager).print("Exiting the School Application.");
//    verify(consoleMenu, times(8)).drawMenu();
//  }
//
//}
