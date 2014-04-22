package cz.i.cis.config.ejb.dao.exceptions;

public class UniqueProfileKeyException extends Exception {

  private static final long serialVersionUID = -1744617586209589680L;

  public UniqueProfileKeyException(String message, Exception cause) {
    super(message, cause);
  }

}
