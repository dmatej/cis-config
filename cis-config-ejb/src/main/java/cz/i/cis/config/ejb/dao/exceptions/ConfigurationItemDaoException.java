package cz.i.cis.config.ejb.dao.exceptions;

/**
 * Exception which serves for data access object for configuration items.
 */
public class ConfigurationItemDaoException extends Exception {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -8024321964230971158L;


  /**
   * {@inheritDoc}
   */
  public ConfigurationItemDaoException() {
    super();
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationItemDaoException(String message) {
    super(message);
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationItemDaoException(Throwable cause) {
    super(cause);
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationItemDaoException(String message, Throwable cause) {
    super(message, cause);
  }
}
