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

import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.test.ArquillianITest;

@Stateless
public class ConfigurationCategoryDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationCategoryDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemCategoryDao")
  private ConfigurationItemCategoryDao dao;
  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper helper;


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
  }


  @Test
  public void creatNewConfigurationCategory() throws Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("Connection settings for registers");
    dao.addCategory(category);
    helper.addToDelete(category);
    LOG.debug("category: {}", category);
    assertNotNull("category.id", category.getId());
  }


  /*
   * @Test
   * public void creatAlreadyExistingConfigurationCategory() {
   * final ConfigurationItemCategory configuration_category = helper.createConfigurationCategory();
   * final ConfigurationItemCategory copy_configuration_category = new ConfigurationItemCategory();
   * copy_configuration_category.setName(configuration_category.getName());
   * dao.addCategory(copy_configuration_category);
   * // helper.addToDelete(copy_configuration_category);
   * }
   */
  @Test
  public void listConfigurationCategories() {
    final List<ConfigurationItemCategory> configurationCategories = dao.listCategories();
    LOG.debug("list configuration categories: {}", configurationCategories);
    assertNotNull("configuration categories", configurationCategories);
    assertTrue("configurationCategories.empty", configurationCategories.isEmpty());
  }


  @Test
  public void testMethodsConfigurationCategoriesDao() throws Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category");
    helper.addToDelete(category);
    dao.addCategory(category);
    LOG.debug("created category: {}", category);
    category.setName("some new category");
    dao.updateCategory(category);
    LOG.debug("updated category: {}", category);
    final ConfigurationItemCategory configItemCategory = dao.getCategory(category.getId());
    assertNotNull("configItemCategory.id", configItemCategory.getId());
  }


  @Test
  public void testComparationCategories() {
    final ConfigurationItemCategory categoryFirst = new ConfigurationItemCategory();
    categoryFirst.setId(1);
    categoryFirst.setName("category x");

    final ConfigurationItemCategory categorySecond = new ConfigurationItemCategory();
    categorySecond.setId(1);
    categorySecond.setName("category x");

    assertEquals(categoryFirst.hashCode(), categorySecond.hashCode());
    assertEquals(categoryFirst, categorySecond);
    assertTrue(categoryFirst.equals(categorySecond));
  }
}
