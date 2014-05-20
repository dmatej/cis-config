package cz.i.cis.config.ejb.dao.exceptions;

public class CisUserDaoException extends Exception {

  private static final long serialVersionUID = 8637276376992976635L;


  public CisUserDaoException() {
    super();
  }


  public CisUserDaoException(String message, Throwable cause) {
    super(message, cause);

  }


  public CisUserDaoException(String message) {
    super(message);
  }


  public CisUserDaoException(Throwable cause) {
    super(cause);
  }

}
