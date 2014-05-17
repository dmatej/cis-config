package cz.i.cis.config.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FacesMessagesUtils {
  private static final Logger LOG = LoggerFactory.getLogger(FacesMessagesUtils.class);


  public static void addFatalErrorMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, null, sumary, detail);
  }

  public static void addFatalErrorMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, clientId, sumary, detail);
  }

  public static void addErrorMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, null, sumary, detail);
  }

  public static void addErrorMessage(final String sumary, final Throwable cause) {
    addMessage(FacesMessage.SEVERITY_ERROR, null, sumary, getRootMessage(cause));
  }

  public static void addErrorMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, clientId, sumary, detail);
  }

  public static void addWarningMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, null, sumary, detail);
  }

  public static void addWarningMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, clientId, sumary, detail);
  }

  public static void addInfoMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, null, sumary, detail);
  }

  public static void addInfoMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, clientId, sumary, detail);
  }

  public static void addMessage(final Severity severity, final String sumary, final String detail) {
    addMessage(severity, null, sumary, detail);
  }

  public static void addMessage(final Severity severity, final String clientId, final String sumary, final String detail) {
    LOG.debug("addMessage(severity={}, clientId={}, sumary={}, detail={})", severity, clientId, sumary, detail);
    final String msgDetail = StringUtils.trimToEmpty(detail);
    final FacesMessage msg = new FacesMessage(severity, sumary, msgDetail);
    FacesContext.getCurrentInstance().addMessage(clientId, msg);
  }

  public static List<FacesMessage> getMessages() {
    LOG.trace("getMessages()");
    final List<FacesMessage> messages = new ArrayList<FacesMessage>();
    final Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
    while (iter.hasNext()) {
      messages.add(iter.next());
    }

    return messages;
  }

  public static void failedRedirectMessage(final String link, final IOException e) {
    LOG.debug("failedRedirectMessage(link={}, e={})", link, e);
    final String sumary = "Nepodařilo se provést přesměrování na adresu: " + link;
    addMessage(FacesMessage.SEVERITY_FATAL, sumary, getRootMessage(e));
  }

  public static String getRootMessage(Throwable t) {
    LOG.debug("getRootMessage(t={})", t);
    while (t.getCause() != null) {
      t = t.getCause();
    }

    return t.getMessage();
  }
}
