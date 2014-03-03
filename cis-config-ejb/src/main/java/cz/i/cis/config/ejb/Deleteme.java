/**
 *
 */
package cz.i.cis.config.ejb;

import javax.ejb.Stateless;
import javax.ws.rs.Path;


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
