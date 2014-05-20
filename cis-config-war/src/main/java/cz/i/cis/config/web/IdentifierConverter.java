package cz.i.cis.config.web;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converter for view parameter {@code id}.
 */
@FacesConverter(value = "IdentifierConverter")
public class IdentifierConverter implements Converter {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(IdentifierConverter.class);


  /**
   * Returns integer value of entered string. If entered string isn't number, it returns zero which
   * is equal to nonexistent identifier.
   *
   * @param context {@link FacesContext} for the request being processed
   * @param component {@link UIComponent} with which this model object
   *          value is associated
   * @param value String value to be converted (may be <code>null</code>)
   * @return Integer value of entered string. If entered string isn't number, it returns zero which
   *         is equal to nonexistent identifier.
   */
  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    LOG.trace("getAsObject(context={}, component={}, value={}", context, component, value);
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException exc) {
      return 0;
    }
  }


  /**
   * Returns string value of view parameter value.
   *
   * @param context {@link FacesContext} for the request being processed
   * @param component {@link UIComponent} with which this model object
   *          value is associated
   * @param value Model object value to be converted
   *          (may be <code>null</code>)
   * @return String value of view parameter value.
   */
  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    LOG.trace("getAsString(context={}, component={}, value={}", context, component, value);
    return String.valueOf(value);
  }
}
