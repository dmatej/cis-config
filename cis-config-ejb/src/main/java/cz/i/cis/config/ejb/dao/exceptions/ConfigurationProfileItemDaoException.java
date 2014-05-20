package cz.i.cis.config.ejb.dao.exceptions;

/**
 * Exception which serves for data access object for configuration profile items.
 */
public class ConfigurationProfileItemDaoException extends Exception {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = 5487215247674928594L;


  /**
   * {@inheritDoc}
   */
  public ConfigurationProfileItemDaoException() {
    super();
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationProfileItemDaoException(String message) {
    super(message);
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationProfileItemDaoException(Throwable cause) {
    super(cause);
  }


  /**
   * {@inheritDoc}
   */
  public ConfigurationProfileItemDaoException(String message, Throwable cause) {
    super(message, cause);
  }
}
