package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.test.ArquillianITest;

@Stateless
public class ConfigurationCategoryDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationCategoryDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao;
  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper helper;

  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
  }

  @Test
  public void creatNewteConfigurationCategory() {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("Connection settings for registers");
    dao.addCategory(category);
    helper.addToDelete(category);
    LOG.debug("category: {}", category);
    assertNotNull("category.id", category.getId());
    }
/*
  @Test
  public void creatAlreadyExistingteConfigurationCategory() {
    LOG.debug("DEBUG ConfigurationCategoryDao");
    final ConfigurationItemCategory configuration_category = helper.createConfigurationCategory();
    final ConfigurationItemCategory copy_configuration_category = new ConfigurationItemCategory();
    copy_configuration_category.setName(configuration_category.getName());
    dao.addCategory(copy_configuration_category);
   // helper.addToDelete(copy_configuration_category);
    }
*/
  @Test
  public void listConfigurationCategories() {
    final List<ConfigurationItemCategory> configuration_categories = dao.listCategories();
    LOG.debug("list configuration categories: {}", configuration_categories);
    assertNotNull("configuration categories", configuration_categories);
    assertTrue("configuration_categories.empty",configuration_categories.isEmpty());
  }

  @Test
  public void testMethodsConfigurationCategoriesDao() throws Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category");
    helper.addToDelete(category);
    dao.addCategory(category);
    LOG.debug("created category: {}",category);
    category.setName("some new category");
    dao.updateCategory(category);
    LOG.debug("updated category: {}",category);
    final ConfigurationItemCategory c = dao.getCategory(category.getId());
    assertNotNull("c.id", c.getId());
  }

  @Test
  public void testComparationCategories() {
    final ConfigurationItemCategory category0 = new ConfigurationItemCategory();
    category0.setId(1);
    category0.setName("category x");

    final ConfigurationItemCategory category1 = new ConfigurationItemCategory();
    category1.setId(1);
    category1.setName("category x");

   assertEquals(category0.hashCode(), category1.hashCode());
   assertEquals(category0, category1);
   assertTrue(category0.equals(category1));
  }

}
