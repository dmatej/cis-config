package cz.i.cis.config.ejb.dao.exceptions;

public class UniqueKeyException extends Exception {

  private static final long serialVersionUID = -1711244814725299444L;

  public UniqueKeyException() {
    super();
  }


  public UniqueKeyException(String message, Throwable cause) {
    super(message, cause);
  }


  public UniqueKeyException(String message) {
    super(message);
  }


  public UniqueKeyException(Throwable cause) {
    super(cause);
  }

}
