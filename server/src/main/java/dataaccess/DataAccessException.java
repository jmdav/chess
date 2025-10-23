package dataaccess;

import model.ErrorMessage;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
  private final int statusCode;
  private final ErrorMessage message;

  public DataAccessException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = new ErrorMessage(message);
  }

  public int getStatusCode() { return statusCode; }
  public ErrorMessage getErrorMessage() { return message; }
}
