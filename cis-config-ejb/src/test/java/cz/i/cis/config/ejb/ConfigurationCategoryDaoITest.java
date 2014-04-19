package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationCategoryDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationCategoryDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao;


  @After
  public void removeEnvirenments() {
    List<ConfigurationItemCategory> categorys = dao.listCategories();
    dao.removeCategory(categorys.get(0));
    List<ConfigurationItemCategory> list_categorys = dao.listCategories();
    assertTrue("list_category.isEmpty", list_categorys.isEmpty());
    LOG.debug("list_category: {}", list_categorys);
  }


  @Test
  public void createConfigurationCategory() {
    LOG.debug("DEBUG ConfigurationCategoryDao");
    ConfigurationItemCategory configuration_category = new ConfigurationItemCategory();
    configuration_category.setName("Connection settings for basic registers");
    dao.addCategory(configuration_category);
    LOG.debug("configuration_category: {}", configuration_category);
    configuration_category.setName("Disonnection settings for basic registers");
    dao.updateCategory(configuration_category);
    LOG.debug("update configuration_category: {}", configuration_category);
    assertNotNull("configuration_category.id", configuration_category.getId());

    final List<ConfigurationItemCategory> categorys = dao.listCategories();
    LOG.debug("categorys: {}", categorys);
    assertNotNull("categorys", categorys);
    assertEquals("categorys.size", 1, categorys.size());
    ConfigurationItemCategory category = categorys.get(0);
    assertNotNull("categorys[0]", category);
    assertEquals("user.hashCode", category.hashCode(), configuration_category.hashCode());
    assertEquals("user is not same os original", category, configuration_category);
    assertTrue("category.equals", category.equals(categorys.get(0)));
  }
}
