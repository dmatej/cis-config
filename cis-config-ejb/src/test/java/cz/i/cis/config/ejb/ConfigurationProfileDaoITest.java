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
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.helpers.ConfigurationProfileTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationProfileDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao_user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper user_helper;


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
    user_helper.cleanup();

  }


  @Test
  public void creatNewConfigurationProfile() throws UserAlreadyExistsException {

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    dao_user.addUser(user);
    user_helper.addToDelete(user);

    final ConfigurationProfile profile = new ConfigurationProfile();
    profile.setUser(user);
    profile.setDescription("description");
    profile.setName("profile name");
    profile.setUpdate(new Date());
    dao.addProfile(profile);
    helper.addToDelete(profile);

    LOG.debug("profile: {}", profile);
    assertNotNull("profile.id", profile.getId());

    profile.setName("new profile name");
    dao.updateProfile(profile);
    assertTrue(dao.listProfiles().get(0).getName().equals("new profile name"));
  }

/*
  @Test
  public void creatAlreadyExistingConfigurationProfile() {
    final ConfigurationProfile profile = helper.createConfigurationProfile();
    final ConfigurationProfile copy_profile = new ConfigurationProfile();
    copy_profile.setDescription(profile.getDescription());
    copy_profile.setUpdate(profile.getUpdate());
    copy_profile.setUser(profile.getUser());
    copy_profile.setName(profile.getName());
    dao.addProfile(copy_profile);
  }
*/

  @Test
  public void listConfigurationProfiles() {
    final List<ConfigurationProfile> profiles = dao.listProfiles();
    LOG.debug("list configuration profiles: {}", profiles);
    assertNotNull("configuration profiles", profiles);
    assertTrue("profiles.empty", profiles.isEmpty());
  }


  @Test
  public void testComparationConfigurationProfiles() {
    final CisUser user = new CisUser();

    final ConfigurationProfile profile0 = new ConfigurationProfile();
    profile0.setId(1);
    profile0.setUser(user);
    profile0.setDescription("description");
    profile0.setName("profile name");
    profile0.setUpdate(new Date());

    final ConfigurationProfile profile1 = new ConfigurationProfile();
    profile1.setId(1);
    profile1.setUser(user);
    profile1.setDescription("description");
    profile1.setName("profile name");
    profile1.setUpdate(new Date());

    assertEquals(profile0.hashCode(), profile1.hashCode());
    assertEquals(profile0, profile1);
    assertTrue(profile0.equals(profile1));
  }

}
