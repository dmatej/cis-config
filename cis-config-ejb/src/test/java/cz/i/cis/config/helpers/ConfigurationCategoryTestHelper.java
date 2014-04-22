package cz.i.cis.config.helpers;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.jpa.ConfigurationItemCategory;

@Stateless
@Local
public class ConfigurationCategoryTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationCategoryTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationItemCategory> configuration_categories = new HashSet<>();


  public ConfigurationCategoryTestHelper() {
  }


  public ConfigurationItemCategory createConfigurationCategory() {
    final ConfigurationItemCategory configuration_category = new ConfigurationItemCategory();
    configuration_category.setName((RandomStringUtils.random(10, true, true)));
    em.persist(configuration_category);
    em.flush();
    configuration_categories.add(configuration_category);
    return configuration_category;
  }


  public void addToDelete(ConfigurationItemCategory configuration_category) {
    configuration_categories.add(configuration_category);
  }


  public void cleanup() {
    if (configuration_categories.isEmpty()) {
      return;
    }
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaDelete<ConfigurationItemCategory> delete = criteriaBuilder
          .createCriteriaDelete(ConfigurationItemCategory.class);
      Root<ConfigurationItemCategory> root = delete.from(ConfigurationItemCategory.class);
      delete.where(root.in(configuration_categories));
      Query query = em.createQuery(delete);
      int cnt = query.executeUpdate();
      LOG.debug("Deleted configuration categorys: {}", cnt);
    } catch (Exception e) {
      LOG.error("Cleanup failed", e);
    }
  }
}
