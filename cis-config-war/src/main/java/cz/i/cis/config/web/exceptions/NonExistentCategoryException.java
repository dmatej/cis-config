package cz.i.cis.config.web.exceptions;

/**
 * This exception serves for baking beans for nonexistent category.
 *
 * @author Mr.FrAnTA (Michal Dékány)
 */
public class NonExistentCategoryException extends Exception {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -1960527705644759335L;


  /**
   * {@inheritDoc}
   */
  public NonExistentCategoryException() {
    super("Vybraná kategorie není správná (nejspíš neexistuje)");
  }


  /**
   * {@inheritDoc}
   */
  public NonExistentCategoryException(String message) {
    super(message);
  }


  /**
   * {@inheritDoc}
   */
  public NonExistentCategoryException(Throwable cause) {
    super(cause);
  }


  /**
   * {@inheritDoc}
   */
  public NonExistentCategoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
