package client;

import model.ErrorMessage;

/**
 * Indicates there was an error in the command
 */
public class ResponseException extends Exception {
  private final int statusCode;
  private final ErrorMessage message;

  public ResponseException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = new ErrorMessage(message);
  }

  public int getStatusCode() { return statusCode; }
  public ErrorMessage getErrorMessage() { return message; }
}
