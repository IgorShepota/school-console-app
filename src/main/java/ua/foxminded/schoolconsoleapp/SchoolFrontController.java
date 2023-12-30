package ua.foxminded.schoolconsoleapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.schoolconsoleapp.consolemanager.ConsoleManager;
import ua.foxminded.schoolconsoleapp.consolemenu.ConsoleMenu;

@Component
public class SchoolFrontController {

  private final ConsoleMenu consoleMenu;
  private final ConsoleManager consoleManager;
  private final SchoolOperations operations;

  @Autowired
  public SchoolFrontController(ConsoleMenu consoleMenu,
      ConsoleManager consoleManager, SchoolOperations operations) {
    this.consoleMenu = consoleMenu;
    this.consoleManager = consoleManager;
    this.operations = operations;
  }

  public void run() {
    boolean exit = false;
    while (!exit) {
      consoleMenu.drawMenu();
      String choice = consoleManager.readLine().trim();

      switch (choice) {
        case "1":
          operations.findGroupsWithLessOrEqualStudent();
          break;
        case "2":
          operations.findStudentsByGroupName();
          break;
        case "3":
          operations.addNewStudent();
          break;
        case "4":
          operations.deleteStudent();
          break;
        case "5":
          operations.enrollStudentToCourse();
          break;
        case "6":
          operations.removeStudentFromCourse();
          break;
        case "exit":
          exit = true;
          break;
        default:
          consoleManager.print("Invalid choice. Please select a valid option.");
          break;
      }
    }
    consoleManager.print("Exiting the School Application.");
  }

}
