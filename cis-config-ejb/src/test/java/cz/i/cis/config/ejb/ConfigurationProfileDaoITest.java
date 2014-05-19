package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.helpers.ConfigurationProfileTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationProfileDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao profileDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao userDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper profileHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper userHelper;


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    profileHelper.cleanup();
    userHelper.cleanup();

  }


  @Test
  public void creatNewConfigurationProfile() {

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    userDao.addUser(user);
    userHelper.addToDelete(user);

    final ConfigurationProfile profile = new ConfigurationProfile();
    profile.setUser(user);
    profile.setDescription("description");
    profile.setName("profile name");
    profile.setUpdate(new Date());
    profileDao.addProfile(profile);
    profileHelper.addToDelete(profile);

    LOG.debug("profile: {}", profile);
    assertNotNull("profile.id", profile.getId());

    profile.setName("new profile name");
    profileDao.updateProfile(profile);
    assertTrue(profileDao.listProfiles().get(0).getName().equals("new profile name"));
  }


//  @Test
//  public void creatAlreadyExistingConfigurationProfile() {
//    final ConfigurationProfile profile = helper.createConfigurationProfile();
//    final ConfigurationProfile profileCopy = new ConfigurationProfile();
//    profileCopy.setDescription(profile.getDescription());
//    profileCopy.setUpdate(profile.getUpdate());
//    profileCopy.setUser(profile.getUser());
//    profileCopy.setName(profile.getName());
//    dao.addProfile(profileCopy);
//  }


  @Test
  public void listConfigurationProfiles() {
    final List<ConfigurationProfile> profiles = profileDao.listProfiles();
    LOG.debug("list configuration profiles: {}", profiles);
    assertNotNull("configuration profiles", profiles);
    assertTrue("profiles.empty", profiles.isEmpty());
  }


  @Test
  public void testComparationConfigurationProfiles() {
    final CisUser user = new CisUser();

    final ConfigurationProfile profileFirst = new ConfigurationProfile();
    profileFirst.setId(1);
    profileFirst.setUser(user);
    profileFirst.setDescription("description");
    profileFirst.setName("profile name");
    profileFirst.setUpdate(new Date());

    final ConfigurationProfile profileSecond = new ConfigurationProfile();
    profileSecond.setId(1);
    profileSecond.setUser(user);
    profileSecond.setDescription("description");
    profileSecond.setName("profile name");
    profileSecond.setUpdate(new Date());

    assertEquals(profileFirst.hashCode(), profileSecond.hashCode());
    assertEquals(profileFirst, profileSecond);
    assertTrue(profileFirst.equals(profileSecond));
  }

}
