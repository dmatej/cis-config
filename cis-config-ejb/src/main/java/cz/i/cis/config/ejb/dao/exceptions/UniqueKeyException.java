package cz.i.cis.config.ejb.dao.exceptions;

public class UniqueKeyException extends Exception {

  private static final long serialVersionUID = -1711244814725299444L;


  public UniqueKeyException(String message, Exception cause) {
    super(message, cause);

  }

}
