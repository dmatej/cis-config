package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.UniqueKeyException;
import cz.i.cis.config.ejb.dao.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemDao.class);
  private CisUser user;
  private ConfigurationItemKey key;
  private ConfigurationItemCategory itCategory;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemDao")
  private ConfigurationItemDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao_user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_itCategory;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao dao_key;


  @Before
  public void createEnvirenments() throws UserAlreadyExistsException {
    LOG.debug("DEBUG ConfigurationItemDao");
    user = new CisUser();
    user.setFirstName("andrej");
    user.setLastName("minin");
    user.setLogin("pirat");
    Date birthday = new Date();
    user.setBirthDate(birthday);
    dao_user.addUser(user);
    LOG.debug("created user: {}", user);

    itCategory = new ConfigurationItemCategory();
    itCategory.setName("new id category");
    dao_itCategory.addCategory(itCategory);
    LOG.debug("created item category: {}", itCategory);

    key = new ConfigurationItemKey();
    key.setCategory(itCategory);
    key.setKey("key");
    key.setDescription("my key");
    key.setType(Type.Text);
    dao_key.addCategory(key);
    LOG.debug("created item key: {}", key);
    // delete
    /*
     * key2 = new ConfigurationItemKey();
     * key2.setCategory(itCategory);
     * key2.setKey("key");
     * key2.setDescription("my key");
     * key2.setType(Type.Text);
     * dao_key.addCategory(key2);
     */
  }


  @After
  public void removeEnvirenments() {
    List<ConfigurationItem> configurations = dao.listItems();
    dao.removeItem(configurations.get(0));
    List<ConfigurationItem> list_configurations = dao.listItems();
    assertTrue("list_configurations.isEmpty", list_configurations.isEmpty());
    LOG.debug("list_configurations: {}", list_configurations);
  }


  @Test
  // (expected=UniqueKeyException.class)
  public void createConfigurationItem() // throws UniqueKeyException
  {
    ConfigurationItem configuration_item = new ConfigurationItem();
    configuration_item.setUser(user);
    configuration_item.setKey(key);
    configuration_item.setValue("key-user value");
    Date current_date = new Date();
    configuration_item.setUpdate(current_date);
    dao.addItem(configuration_item);
    /*
     * ConfigurationItem configuration_item2 = new ConfigurationItem();
     * configuration_item2.setUser(user);
     * configuration_item2.setKey(key2);
     * configuration_item2.setValue("key-user value");
     * Date current_date2 = new Date();
     * configuration_item2.setUpdate(current_date2);
     * dao.addItem(configuration_item2);
     */
    LOG.debug("configuration_item: {}", configuration_item);
    configuration_item.setValue("new key-user value");
    dao.updateItem(configuration_item);
    LOG.debug("update configuration_item: {}", configuration_item);
    assertNotNull("configuration_item.id", configuration_item.getId());

    final List<ConfigurationItem> configurations = dao.listItems();
    LOG.debug("configurations: {}", configurations);
    assertNotNull("configurations", configurations);
    assertEquals("configurations.size", 1, configurations.size());

    ConfigurationItem configuration = configurations.get(0);
    assertNotNull("configuration[0]", configuration);
    assertEquals("configuration.hashCode", configuration.hashCode(), configuration_item.hashCode());
    assertEquals("configuration is not same os original", configuration, configuration_item);
    assertTrue("configuration.equals", configuration.equals(configurations.get(0)));
  }
}
