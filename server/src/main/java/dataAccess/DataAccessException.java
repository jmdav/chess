package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
  private final int statusCode;
  private final String message;

  public DataAccessException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() { return statusCode; }
  public String getMessage() { return message; }
}
