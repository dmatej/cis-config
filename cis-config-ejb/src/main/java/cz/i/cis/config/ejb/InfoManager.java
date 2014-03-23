package cz.i.cis.config.ejb;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;


@Named(value = "infoManager")
@ApplicationScoped
public class InfoManager {

  public String getJsfImplementation() {
    return FacesContext.class.getPackage().getImplementationTitle();
  }

  public String getJsfVersion() {
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
