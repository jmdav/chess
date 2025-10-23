package dataAccess;

/**
 * Indicates data could not be found
 */
public class NoDataException extends DataAccessException {
  public NoDataException(String message) { super(message); }
  public NoDataException(String message, Throwable ex) { super(message, ex); }
}
