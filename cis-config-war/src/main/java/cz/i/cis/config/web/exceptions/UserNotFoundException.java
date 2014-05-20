package cz.i.cis.config.web.exceptions;

/**
 * This exception serves for baking beans for user which was not found in database.
 */
public class UserNotFoundException extends Exception {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -8048823619633617283L;


  /**
   * Constructs a new exception with default detail message.
   */
  public UserNotFoundException() {
    super("Přihlášený uživatel nebyl nalezen v databázi");
  }


  /**
   * {@inheritDoc}
   */
  public UserNotFoundException(String message) {
    super(message);
  }


  /**
   * {@inheritDoc}
   */
  public UserNotFoundException(Throwable cause) {
    super(cause);
  }


  /**
   * {@inheritDoc}
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
