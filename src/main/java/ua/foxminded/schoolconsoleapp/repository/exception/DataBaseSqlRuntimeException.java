package ua.foxminded.schoolconsoleapp.repository.exception;

public class DataBaseSqlRuntimeException extends RuntimeException {

  public DataBaseSqlRuntimeException() {
  }

  public DataBaseSqlRuntimeException(String message) {
    super(message);
  }

  public DataBaseSqlRuntimeException(String message, Exception cause) {
    super(message, cause);
  }
}
