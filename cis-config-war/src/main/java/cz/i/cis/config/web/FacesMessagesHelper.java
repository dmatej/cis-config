package cz.i.cis.config.web;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helping bean for faces messages.
 */
@Named(value = "facesMessagesHelper")
@ViewScoped
public class FacesMessagesHelper {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(FacesMessagesHelper.class);

  /**
   * Returns information whether faces context contains some global message.
   *
   * @return Information whether faces context contains some global message.
   */
  public boolean globalMessagesExists() {
    LOG.debug("globalMessagesExists()");
    return FacesContext.getCurrentInstance().getMessages(null).hasNext();
  }
}
