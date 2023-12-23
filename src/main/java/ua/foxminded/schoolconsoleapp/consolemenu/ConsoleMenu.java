package ua.foxminded.schoolconsoleapp.consolemenu;

import org.springframework.stereotype.Component;

@Component
public class ConsoleMenu {

  public void drawMenu() {
    String menu =
        "\r\n**************************************************************************\r\n"
            + "*************             SCHOOL CONSOLE APP                 *************\r\n"
            + "**************************************************************************\r\n"
            + "\r\n"
            + "* type '1' to find all groups with less or equal students’ number.\r\n"
            + "* type '2' to find all students related to the course with the given name.\r\n"
            + "* type '3' to add a new student\r\n"
            + "* type '4' to delete a student by the student id\r\n"
            + "* type '5' to add a student to the course (from a list)\r\n"
            + "* type '6' to remove the student from one of their courses.\r\n"
            + "* type 'exit' to quit\r\n".replaceAll("\r\n", System.lineSeparator());

    System.out.println(menu);
  }
}
