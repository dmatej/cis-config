/**
 *
 */
package cz.i.cis.config.ejb;

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
public class Deleteme {

  public String greet() {
    return "Hello";
  }
}
