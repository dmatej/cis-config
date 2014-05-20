package cz.i.cis.config.ejb.dao.exceptions;

/**
 * Exception for configuration item key which is active.
 */
public class ActiveItemKeyException extends Exception {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = 4607760167423853691L;

  /**
   * {@inheritDoc}
   */
  public ActiveItemKeyException() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public ActiveItemKeyException(String message) {
    super(message);
  }

  /**
   * {@inheritDoc}
   */
  public ActiveItemKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * {@inheritDoc}
   */
  public ActiveItemKeyException(Throwable cause) {
    super(cause);
  }

}
