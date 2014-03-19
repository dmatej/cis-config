/**
 *
 */
package cz.i.cis.config.ejb;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.test.ArquillianITest;


/**
 * @author David Matějček
 *
 */
public class UserDaoITest extends ArquillianITest {
  private static final Logger LOG = LoggerFactory.getLogger(UserDaoITest.class);


  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserDao")
  private UserDao dao;


  @Test
  public void createUser() {
    CisUser user0 = new CisUser();
    user0.setLastName("Matějka");
    user0.setLogin("xt001");
    user0.setFirstName("Afanasi");
    user0.setBirthDate(new Date());
    dao.addUser(user0);
    LOG.debug("user0: {}", user0);
    assertNotNull("user0.id", user0.getId());

    final List<CisUser> users = dao.listUsers();
    LOG.debug("users: {}", users);
    assertNotNull("users", users);
    assertEquals("users.size", 1, users.size());
    CisUser user = users.get(0);
    assertNotNull("users[0]", user);
    assertEquals("user.hashCode", user0.hashCode(), user.hashCode());
    assertEquals("user is not same os original", user0, user);
    // DELETEME: deep equals will not work because of the birtDate - db returns zeroes as time, new Date not!
    assertFalse("reflection equals should not return true", EqualsBuilder.reflectionEquals(user0, user, true));
  }
}
