package cz.i.cis.config.web;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Backing bean used to retrieve environment information.
 */
@Named(value = "infoManager")
@ApplicationScoped
public class InfoManager {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(InfoManager.class);

  /**
   * Returns JSF implementation name.
   * @return JSF implementation name.
   */
  public String getJsfImplementation() {
    LOG.trace("getJsfImplementation()");
    return FacesContext.class.getPackage().getImplementationTitle();
  }

  /**
   * Returns JSF implementation version.
   * @return JSF implementation name.
   */
  public String getJsfVersion() {
    LOG.trace("getJsfVersion()");
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
