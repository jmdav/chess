package client.errors;

/**
 * Indicates there was an error in the command
 */
public class ResponseException extends Exception {
  private final String message;

  public ResponseException(String message) { this.message = message; }

  public String getErrorMessage() { return message; }
}