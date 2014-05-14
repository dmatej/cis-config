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
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.ejb.dao.exceptions.UniqueKeyException;
import cz.i.cis.config.ejb.dao.exceptions.UniqueProfileKeyException;
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemKeyTestHelper;
import cz.i.cis.config.helpers.ConfigurationProfileItemTestHelper;
import cz.i.cis.config.helpers.ConfigurationProfileTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationProfileItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileItemDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemDao")
  private ConfigurationProfileItemDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao dao_key;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_category;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao dao_profile;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao_user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemTestHelper")
  private ConfigurationProfileItemTestHelper helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper key_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper category_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper profile_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper user_helper;


  @Before
  public void init() {
  }


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
    user_helper.cleanup();
    profile_helper.cleanup();
    category_helper.cleanup();
    key_helper.cleanup();
  }


  @Test
  public void creatNewConfigurationProfileItem() throws UniqueKeyException, UserAlreadyExistsException, UniqueProfileKeyException {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    dao_category.addCategory(category);
    category_helper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    dao_key.addItemKey(key);
    key_helper.addToDelete(key);

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    dao_user.addUser(user);
    user_helper.addToDelete(user);

    final ConfigurationProfile profile = new ConfigurationProfile();
    profile.setName("base configuration");
    profile.setDescription("domain configuration");
    profile.setUpdate(new Date());
    profile.setUser(user);
    dao_profile.addProfile(profile);
    profile_helper.addToDelete(profile);

    final ConfigurationProfileItem profile_item = new ConfigurationProfileItem();
    profile_item.setKey(key);
    profile_item.setProfile(profile);
    profile_item.setValue("key-profile");
    dao.addItem(profile_item);
    helper.addToDelete(profile_item);
    LOG.debug("profile_item: {}", profile_item);
    assertNotNull("profile_item.id", profile_item.getId());

    profile_item.setValue("profile-key");
    dao.updateItem(profile_item);
    assertTrue(dao.listItems().get(0).getValue().equals("profile-key"));
  }


  @Test(expected = UniqueProfileKeyException.class)
  public void creatAlreadyExistingConfigurationProfileItem() throws UniqueProfileKeyException {
    final ConfigurationProfileItem profile_item = helper.createConfigurationProfileItem();
    final ConfigurationProfileItem copy_profile_item = new ConfigurationProfileItem();
    copy_profile_item.setProfile(profile_item.getProfile());
    copy_profile_item.setKey(profile_item.getKey());
    copy_profile_item.setValue(profile_item.getValue());
    dao.addItem(copy_profile_item);
  }


  @Test
  public void listConfigurationProfileItems() {
    final List<ConfigurationProfileItem> items = dao.listItems();
    LOG.debug("list configuration profile items: {}", items);
    assertNotNull("configuration profile items", items);
    assertTrue("items.empty", items.isEmpty());
  }


  @Test
  public void testComparationConfigurationProfileItems() {
    final ConfigurationItemKey key = new ConfigurationItemKey();
    final ConfigurationProfile profile = new ConfigurationProfile();

    final ConfigurationProfileItem item0 = new ConfigurationProfileItem();
    item0.setId(1);
    item0.setKey(key);
    item0.setProfile(profile);
    item0.setValue("key-profile");

    final ConfigurationProfileItem item1 = new ConfigurationProfileItem();
    item1.setId(1);
    item1.setKey(key);
    item1.setProfile(profile);
    item1.setValue("key-profile");

    assertEquals(item0.hashCode(), item1.hashCode());
    assertEquals(item0, item1);
    assertTrue(item0.equals(item1));
  }

}
