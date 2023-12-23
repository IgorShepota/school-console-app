package ua.foxminded.schoolconsoleapp.consolemanager;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleManager {

  private static final String ONLY_POSITIVE_DIGITS = "\\d+";

  private final Scanner scanner;

  @Autowired
  public ConsoleManager(Scanner scanner) {
    this.scanner = scanner;
  }

  public String readLine() {
    return scanner.nextLine();
  }

  public void print(String input) {
    System.out.println(input);
  }

  public int parseInput(String input) {
    input = verifyInput(input);

    return Integer.parseInt(input);
  }

  private String verifyInput(String input) {
    if (input == null || input.isEmpty()) {
      input = "-1";
    } else if (!input.matches(ONLY_POSITIVE_DIGITS)) {
      input = "-2";
    }
    return input;
  }

}
