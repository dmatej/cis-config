package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.test.ArquillianITest;

import java.util.Date;
import java.util.List;

public class ConfigurationProfileDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileDaoITest.class);
  private CisUser user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao dao;
  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao daouser;


  @Before
  public void createEnvirenments() throws UserAlreadyExistsException {
    LOG.debug("DEBUG ConfigurationProfileDao");
    user = new CisUser();
    user.setFirstName("andrej");
    user.setLastName("minin");
    user.setLogin("pirat");
    Date birthday = new Date();
    user.setBirthDate(birthday);
    daouser.addUser(user);
    LOG.debug("created user: {}", user);

  }


  @After
  public void removeEnvirenments() {
    List<ConfigurationProfile> profiles = dao.listItems();
    dao.removeItem(profiles.get(0));
    List<ConfigurationProfile> list_profiles = dao.listItems();
    assertTrue("profiles.isEmpty", list_profiles.isEmpty());
    LOG.debug("list_profiles: {}", list_profiles);
  }


  @Test
  public void createConfigurationProfile() {
    ConfigurationProfile configuration_profile = new ConfigurationProfile();
    configuration_profile.setName("base configurat");
    configuration_profile.setDescription("domain configuration");
    Date current_date = new Date();
    configuration_profile.setUpdate(current_date);
    configuration_profile.setUser(user);
    dao.addItem(configuration_profile);
    LOG.debug("configuration_profile: {}", configuration_profile);
    configuration_profile.setDescription("windows web hosting platform");
    dao.updateItem(configuration_profile);
    LOG.debug("update configuration_profile: {}", configuration_profile);
    assertNotNull("configuration_profile.id", configuration_profile.getId());

    List<ConfigurationProfile> profiles = dao.listItems();
    LOG.debug("profiles: {}", profiles);
    assertNotNull("profiles", profiles);
    assertEquals("profiles.size", 1, profiles.size());
    ConfigurationProfile profile = profiles.get(0);
    assertNotNull("profile", profile);
    assertEquals("profiles.hashCode", profile.hashCode(), configuration_profile.hashCode());
    assertEquals("profile is not same os original", profile, configuration_profile);
    assertTrue("profile.equals", profile.equals(profiles.get(0)));

  }
}
