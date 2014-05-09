package cz.i.cis.config.web;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Named(value = "infoManager")
@ApplicationScoped
public class InfoManager {
  private static final Logger LOG = LoggerFactory.getLogger(InfoManager.class);

  public String getJsfImplementation() {
    LOG.trace("getJsfImplementation()");
    return FacesContext.class.getPackage().getImplementationTitle();
  }

  public String getJsfVersion() {
    LOG.trace("getJsfVersion()");
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
