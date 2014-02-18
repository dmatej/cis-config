/**
 *
 */
package cz.i.cis.config.web.experiment;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * @author David Matějček
 *
 */
@Stateless
@Path("/hell")
public class Hello {

  @GET
  @Produces("text/plain")
  public String greet() {
    return "Hello";
  }
}
