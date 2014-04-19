/**
 *
 */
package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.test.ArquillianITest;
import cz.i.cis.config.test.UserTestHelper;

/**
 * @author David Matějček
 */
@Stateless
public class UserDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(UserDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao;
  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper helper;

  @Before
  public void init() {
  }

  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
  }

  @Test(expected=UserAlreadyExistsException.class)
  public void createAlreadyExistingUser() throws UserAlreadyExistsException {
    final CisUser user0 = helper.createUser();
    final CisUser user = new CisUser();
    user.setLastName("XTMatějka");
    user.setLogin(user0.getLogin());
    user.setFirstName("XTAfanasi");
    user.setBirthDate(new Date());
    dao.addUser(user);
  }


  @Test
  public void createNewUser() throws Exception {
    final CisUser user = new CisUser();
    user.setLastName("ZXTMatějka");
    user.setLogin(RandomStringUtils.random(6, true, true));
    user.setFirstName("ZTAfanasi");
    user.setBirthDate(new Date());
    dao.addUser(user);
    helper.addToDelete(user);
    LOG.debug("user: {}", user);
    assertNotNull("user.id", user.getId());
  }


  @Test
  public void listUsers() {
    final List<CisUser> users = dao.listUsers();
    LOG.debug("users: {}", users);
    assertNotNull("users", users);
    assertFalse("users.empty", users.isEmpty());
  }


  @Test
  public void testComparation() {
    final CisUser user0 = new CisUser();
    user0.setLastName("ZXTMatějka");
    user0.setLogin("xyz");
    user0.setFirstName("ZTAfanasi");
    user0.setBirthDate(new Date());

    final CisUser user1 = new CisUser();
    user1.setLastName("ZXTMatějka");
    user1.setLogin("xyz");
    user1.setFirstName("ZTAfanasi");
    user1.setBirthDate(DateUtils.addMinutes(user0.getBirthDate(), 360));

    assertEquals(user0.hashCode(), user1.hashCode());
    assertEquals(user0, user1);
    assertFalse(EqualsBuilder.reflectionEquals(user0, user1, false));
  }
}
