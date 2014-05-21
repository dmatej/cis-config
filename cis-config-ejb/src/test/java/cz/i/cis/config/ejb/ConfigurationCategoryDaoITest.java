package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemCategoryDaoException;
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


  @Test(expected = ConfigurationItemCategoryDaoException.class)
  public void creatAlreadyExistingConfigurationCategory() throws Exception {
    final ConfigurationItemCategory category = helper.createConfigurationCategory();
    final ConfigurationItemCategory copyCategory = new ConfigurationItemCategory();
    copyCategory.setName(category.getName());
    dao.addCategory(copyCategory);
    helper.addToDelete(copyCategory);
  }


  @Test
  public void testGetCategory() {
    final ConfigurationItemCategory category0 = helper.createConfigurationCategory();
    String nameCategory = category0.getName();
    final ConfigurationItemCategory category1 = dao.getCategory(nameCategory);
    assertEquals(category0.hashCode(), category1.hashCode());
    int idCategory = category0.getId();
    final ConfigurationItemCategory category2 = dao.getCategory(idCategory);
    assertEquals(category0.hashCode(), category2.hashCode());
    final ConfigurationItemCategory categoryNull = dao.getCategory(nameCategory + "a");
    assertNull(categoryNull);
  }


  @Test(expected = ConfigurationItemCategoryDaoException.class)
  public void wrongUpdateCategory() throws Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setId(1);
    category.setName("id1");
    dao.addCategory(category);
    helper.addToDelete(category);
    category.setId(2);
    dao.updateCategory(category);
  }


  @Test(expected = ConfigurationItemCategoryDaoException.class)
  public void testRemoveCategory() throws Exception {
    final ConfigurationItemCategory category = helper.createConfigurationCategory();
    dao.removeCategory(category.getId());
    dao.removeCategory(category.getId());
  }


  @Test
  public void testGetCategoryMap() {
    final Map<String, ConfigurationItemCategory> map0 = dao.getCategoryMap();
    assertTrue("map0.isEmpty", map0.isEmpty());
    helper.createConfigurationCategory();
    final Map<String, ConfigurationItemCategory> map1 = dao.getCategoryMap();
    assertFalse("map1.isEmpty", map1.isEmpty());
    assertTrue("map1.isEmpty", map1.size() == 1);
  }


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
