/**
 *
 */
package cz.i.cis.config.web.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;


/**
 * @author David Matějček
 */
@Stateless
@Path("hello")
public class Hello {
  private static final Logger LOG = LoggerFactory.getLogger(Hello.class);


  @EJB
  private CisUserDao userDao;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("greet")
  public String greet() {
    LOG.debug("greet()");
    final List<CisUser> cisUsers = userDao.listUsers();
    return cisUsers.toString();
  }
}
