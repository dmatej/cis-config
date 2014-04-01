package cz.i.cis.config.ejb.dao;



public class UserAlreadyExistsException extends Exception {

  public UserAlreadyExistsException(String message, Exception cause) {
    super(message, cause);
  }

}
