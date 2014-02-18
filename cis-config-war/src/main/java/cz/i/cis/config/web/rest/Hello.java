/**
 *
 */
package cz.i.cis.config.web.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * @author David Matějček
 */
@Stateless
@Path("hello")
public class Hello {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("greet")
  public String greet() {
    return "Hello";
  }
}
