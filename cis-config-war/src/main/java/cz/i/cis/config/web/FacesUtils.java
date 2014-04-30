package cz.i.cis.config.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

public final class FacesUtils {

  private FacesUtils() {
  }


  public static void redirect(String where) throws IOException {
    FacesContext.getCurrentInstance().getExternalContext().redirect(where);
  }

  public static Map<String, Object> getSessions() {
    FacesContext context = FacesContext.getCurrentInstance();

    return context.getExternalContext().getSessionMap();
  }

  public static Object getSession(String sessionName) {
    Map<String, Object> sessions = getSessions();
    if(sessions == null) {
      return null;
    }

    return sessions.get(sessionName);
  }

  public static void setSession(String sessionName, Object value) {
    Map<String, Object> sessions = getSessions();
    if(sessions == null) {
      return;
    }

    sessions.put(sessionName, value);
  }


  public static String getRootMessage(Throwable t) {
    while (t.getCause() != null) {
      t = t.getCause();
    }

    return t.getMessage();
  }
}
