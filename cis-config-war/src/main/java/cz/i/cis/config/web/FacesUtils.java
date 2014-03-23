package cz.i.cis.config.web;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


public class FacesUtils {

  public static void addMessage(Severity severity, String message){
    FacesMessage msg = new FacesMessage(severity, message, "");
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public static void redirect(String where) throws IOException{
    FacesContext.getCurrentInstance().getExternalContext().redirect(where);
  }

  public static String getRootMessage(Throwable t){
    while(t.getCause() != null){
      t = t.getCause();
    }
    return t.getMessage();
  }
}
