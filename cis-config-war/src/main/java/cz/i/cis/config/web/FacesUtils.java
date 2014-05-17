package cz.i.cis.config.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FacesUtils {
  private static final Logger LOG = LoggerFactory.getLogger(FacesUtils.class);


  private FacesUtils() {
    // hidden
  }


  public static void redirectToURL(String url) throws IOException {
    LOG.debug("redirectToURL(url={})", url);
    FacesContext.getCurrentInstance().getExternalContext().redirect(url);
  }

  public static void redirectToOutcome(String outcome){
    LOG.debug("redirectToOutcome(outcome={})", outcome);
    FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
  }

  public static Map<String, Object> getSessions() {
    LOG.trace("getSessions()");
    FacesContext context = FacesContext.getCurrentInstance();
    return context.getExternalContext().getSessionMap();
  }

  public static Object getSession(String sessionName) {
    LOG.debug("getSession(sessionName={})", sessionName);
    Map<String, Object> sessions = getSessions();
    if(sessions == null) {
      return null;
    }

    return sessions.get(sessionName);
  }

  public static void setSession(String sessionName, Object value) {
    LOG.debug("setSession(sessionName={}, value={})", sessionName, value);
    Map<String, Object> sessions = getSessions();
    if(sessions == null) {
      return;
    }

    sessions.put(sessionName, value);
  }

  public static String getRequestParameter(String key) {
    LOG.debug("getRequestParameter(key={})", key);
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
  }

  public static String getRemoteUser(){
    LOG.trace("getRemoteUser()");
    return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
  }
}
