package dataAccess;

/**
 * Indicates there was a conflict with existing data in the database
 */
public class DataConflictException extends DataAccessException {
  public DataConflictException(String message) { super(message); }
  public DataConflictException(String message, Throwable ex) {
    super(message, ex);
  }
}
