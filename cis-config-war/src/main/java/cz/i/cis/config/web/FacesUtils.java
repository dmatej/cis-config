package cz.i.cis.config.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience class helper methods.
 */
public final class FacesUtils {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(FacesUtils.class);


  /**
   * Private to enforce static access.
   */
  private FacesUtils() {
    // hidden
  }


  /**
   * Redirects to concrete URL.
   *
   * @param url Redirection target.
   * @throws IOException If redirection fails.
   */
  public static void redirectToURL(String url) throws IOException {
    LOG.debug("redirectToURL(url={})", url);
    FacesContext.getCurrentInstance().getExternalContext().redirect(url);
  }


  /**
   * Redirects to given outcome.
   *
   * @param outcome Navigation outcome to redirect to.
   */
  public static void redirectToOutcome(String outcome) {
    LOG.debug("redirectToOutcome(outcome={})", outcome);
    FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
  }


  /**
   * Returns session map.
   *
   * @return Session map.
   */
  public static Map<String, Object> getSessions() {
    LOG.trace("getSessions()");
    return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
  }


  /**
   * Returns session item with specified key.
   *
   * @param sessionName Key of requested session item.
   * @return Session item with specified key.
   */
  public static Object getSession(String sessionName) {
    LOG.debug("getSession(sessionName={})", sessionName);
    Map<String, Object> sessions = getSessions();
    if (sessions == null) {
      return null;
    }
    return sessions.get(sessionName);
  }


  /**
   * Sets session item with specified key.
   *
   * @param sessionName Session item key.
   * @param value Session item value.
   */
  public static void setSession(String sessionName, Object value) {
    LOG.debug("setSession(sessionName={}, value={})", sessionName, value);
    Map<String, Object> sessions = getSessions();
    if (sessions == null) {
      return;
    }
    sessions.put(sessionName, value);
  }


  /**
   * Returns request parameter with specified name.
   *
   * @param key Name of desired request parameter.
   * @return Request parameter with specified name.
   */
  public static String getRequestParameter(String key) {
    LOG.debug("getRequestParameter(key={})", key);
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
  }


  /**
   * Returns login of logged in user.
   *
   * @return Login of logged in user or null.
   */
  public static String getRemoteUser() {
    LOG.trace("getRemoteUser()");
    return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
  }
}
