package cz.i.cis.config.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


public final class FacesMessagesUtils {


  public static void addFatalErrorMessage(String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, null, sumary, detail);
  }

  public static void addFatalErrorMessage(String clientId, String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, clientId, sumary, detail);
  }

  public static void addErrorMessage(String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, null, sumary, detail);
  }

  public static void addErrorMessage(String clientId, String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, clientId, sumary, detail);
  }

  public static void addWarningMessage(String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, null, sumary, detail);
  }

  public static void addWarningMessage(String clientId, String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, clientId, sumary, detail);
  }

  public static void addInfoMessage(String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, null, sumary, detail);
  }

  public static void addInfoMessage(String clientId, String sumary, String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, clientId, sumary, detail);
  }

  public static void addMessage(Severity severity, String sumary, String detail) {
    addMessage(severity, null, sumary, detail);
  }

  public static void addMessage(Severity severity, String clientId, String sumary, String detail) {
    String msgDetail = (detail == null) ? "" : detail;
    FacesMessage msg = new FacesMessage(severity, sumary, msgDetail);
    FacesContext.getCurrentInstance().addMessage(clientId, msg);
  }

  public static List<FacesMessage> getMessages() {
    List<FacesMessage> messages = new ArrayList<FacesMessage>();
    Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
    while (iter.hasNext()) {
      messages.add(iter.next());
    }

    return messages;
  }

  public static void failedRedirectMessage(String link, IOException exc) {
    String sumary = "Nepodařilo se provést přesměrování na adresu: " + link;
    addMessage(FacesMessage.SEVERITY_FATAL, sumary, FacesUtils.getRootMessage(exc));
  }
}
