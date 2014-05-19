/**
 *
 */
package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.test.ArquillianITest;

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


  @Test(expected = PersistenceException.class)
  public void createAlreadyExistingUser() throws PersistenceException {
    final CisUser userFirst = helper.createUser();
    final CisUser userSecond = new CisUser();
    userSecond.setLastName("XTMatějka");
    userSecond.setLogin(userFirst.getLogin());
    userSecond.setFirstName("XTAfanasi");
    userSecond.setBirthDate(new Date());
    dao.addUser(userSecond);
  }


  @Test
  public void createNewUser() {
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
    LOG.debug("list users: {}", users);
    assertNotNull("users", users);
    assertTrue("users.empty", users.isEmpty());
  }


  @Test
  public void testMethodsUserDao() {
    final CisUser user = new CisUser();
    user.setLastName("Jezik");
    user.setLogin("dan");
    user.setFirstName("Daniel");
    user.setBirthDate(new Date());

    helper.addToDelete(user);
    dao.addUser(user);
    LOG.debug("created user: {}", user);
    assertTrue("user.iValid", user.isValid());
    assertFalse("user.isDeleted", user.isDeleted());
    user.setFirstName("Petr");
    dao.updateUser(user);
    LOG.debug("update user name: {}", user);
    assertSame(user.getStatus(), CisUser.STATUS_VALID);
    dao.updateUserStatus(user, CisUser.STATUS_DELETED);
    assertNotSame(user.getStatus(), CisUser.STATUS_VALID);
    final CisUser cu = dao.getUser("dan");
    assertEquals(user.hashCode(), cu.hashCode());
  }


  @Test
  public void testComparation() {
    final CisUser userFirst = new CisUser();
    userFirst.setId(1);
    userFirst.setLastName("ZXTMatějka");
    userFirst.setLogin("xyz");
    userFirst.setFirstName("ZTAfanasi");
    userFirst.setBirthDate(new Date());

    final CisUser userSecond = new CisUser();
    userSecond.setId(1);
    userSecond.setLastName("ZXTMatějka");
    userSecond.setLogin("xyz");
    userSecond.setFirstName("ZTAfanasi");
    userSecond.setBirthDate(DateUtils.addMinutes(userFirst.getBirthDate(), 360));

    assertEquals(userFirst.hashCode(), userSecond.hashCode());
    // assertEquals(user0, user1);
    assertFalse(EqualsBuilder.reflectionEquals(userFirst, userSecond, false));
  }
}
