package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationItemKeyDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemKeyDao.class);
  private ConfigurationItemCategory category;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_category;


  @Before
  public void createEnvirenments() {
    LOG.debug("DEBUG ConfigurationItemKeyDao");
    category = new ConfigurationItemCategory();
    category.setName("base");
    dao_category.addCategory(category);
  }


  @After
  public void removeEnvirenments() {
    List<ConfigurationItemKey> keys = dao.listCategorys();
    dao.removeCategory(keys.get(0));
    List<ConfigurationItemKey> list_keys = dao.listCategorys();
    assertTrue("list_keys.isEmpty", list_keys.isEmpty());
    LOG.debug("list_keys: {}", list_keys);
  }


  @Test
  public void createConfigurationKey() {
    ConfigurationItemKey configuration_key = new ConfigurationItemKey();
    configuration_key.setCategory(category);
    configuration_key.setDescription("base confiuration key");
    configuration_key.setKey("base");
    configuration_key.setType(Type.Text);
    dao.addCategory(configuration_key);
    LOG.debug("configuration_key: {}", configuration_key);
    configuration_key.setDescription("base confiuration key 2.0");
    dao.updateCategory(configuration_key);
    assertNotNull("configuration_key.id", configuration_key.getId());

    final List<ConfigurationItemKey> keys = dao.listCategorys();
    LOG.debug("keys: {}", keys);
    assertNotNull("keys", keys);
    assertEquals("keys.size", 1, keys.size());
    ConfigurationItemKey key = keys.get(0);
    assertNotNull("key[0]", key);
    assertEquals("key.hashCode", key.hashCode(), configuration_key.hashCode());
    assertEquals("key is not same os original", key, configuration_key);
    assertTrue("key.equals", key.equals(keys.get(0)));

  }
}
