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

/**
 * Convenience class for work with messages.
 */
public final class FacesMessagesUtils {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(FacesMessagesUtils.class);

  /**
   * Private to enforce static access.
   */
  private FacesMessagesUtils() {
  }

  /**
   * Adds faces message with fatal severity.
   *
   * @param sumary Short info.
   * @param detail Long description.
   */
  public static void addFatalErrorMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, null, sumary, detail);
  }


  /**
   * Adds faces message with fatal severity.
   *
   * @param clientId Related view component id.
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addFatalErrorMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_FATAL, clientId, sumary, detail);
  }


  /**
   * Adds faces message with error severity.
   *
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addErrorMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, null, sumary, detail);
  }


  /**
   * Adds faces message with error severity.
   *
   * @param sumary Short message.
   * @param cause Error cause. Root message is used for description.
   */
  public static void addErrorMessage(final String sumary, final Throwable cause) {
    addMessage(FacesMessage.SEVERITY_ERROR, null, sumary, getRootMessage(cause));
  }


  /**
   * Adds faces message with error severity.
   *
   * @param clientId Related view component id.
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addErrorMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_ERROR, clientId, sumary, detail);
  }


  /**
   * Adds faces message with error severity.
   *
   * @param clientId Related view component id.
   * @param sumary Short message.
   * @param cause Error cause. Root message is used for description.
   */
  public static void addErrorMessage(final String clientId, final String sumary, final Throwable cause) {
    addMessage(FacesMessage.SEVERITY_ERROR, clientId, sumary, getRootMessage(cause));
  }


  /**
   * Adds faces message with warning severity.
   *
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addWarningMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, null, sumary, detail);
  }


  /**
   * Adds faces message with warning severity.
   *
   * @param clientId
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addWarningMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_WARN, clientId, sumary, detail);
  }


  /**
   * Adds faces message with info severity.
   *
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addInfoMessage(final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, null, sumary, detail);
  }


  /**
   * Adds faces message with info severity.
   *
   * @param clientId Related view component id.
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addInfoMessage(final String clientId, final String sumary, final String detail) {
    addMessage(FacesMessage.SEVERITY_INFO, clientId, sumary, detail);
  }


  /**
   * Adds faces message with given severity.
   *
   * @param severity Message severity.
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addMessage(final Severity severity, final String sumary, final String detail) {
    addMessage(severity, null, sumary, detail);
  }


  /**
   * Adds faces message with given severity.
   *
   * @param severity Message severity.
   * @param clientId Related view component id.
   * @param sumary Short message.
   * @param detail Long description.
   */
  public static void addMessage(final Severity severity, final String clientId, final String sumary, final String detail) {
    LOG.debug("addMessage(severity={}, clientId={}, sumary={}, detail={})", severity, clientId, sumary, detail);
    final String msgDetail = StringUtils.trimToEmpty(detail);
    final FacesMessage msg = new FacesMessage(severity, sumary, msgDetail);
    FacesContext.getCurrentInstance().addMessage(clientId, msg);
  }


  /**
   * Returns current messages.
   *
   * @return Current messages.
   */
  public static List<FacesMessage> getMessages() {
    LOG.trace("getMessages()");
    final List<FacesMessage> messages = new ArrayList<FacesMessage>();
    final Iterator<FacesMessage> iterator = FacesContext.getCurrentInstance().getMessages();
    while (iterator.hasNext()) {
      messages.add(iterator.next());
    }

    return messages;
  }


  /**
   * Adds faces message with fatal severity for redirection fail.
   *
   * @param link Failed redirection URL.
   * @param e Error description.
   */
  public static void failedRedirectMessage(final String link, final IOException e) {
    LOG.debug("failedRedirectMessage(link={}, e={})", link, e);
    final String sumary = "Nepodařilo se provést přesměrování na adresu: " + link;
    addMessage(FacesMessage.SEVERITY_FATAL, sumary, getRootMessage(e));
  }


  /**
   * Returns inner most throwable message.
   *
   * @param t Throwable chain.
   * @return Inner most throwable message.
   */
  public static String getRootMessage(Throwable t) {
    LOG.debug("getRootMessage(t={})", t);
    while (t.getCause() != null) {
      t = t.getCause();
    }

    return t.getMessage();
  }
}
