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
  private Set<ConfigurationItemCategory> configurationCategories = new HashSet<>();


  public ConfigurationCategoryTestHelper() {
  }


  public ConfigurationItemCategory createConfigurationCategory() {
    final ConfigurationItemCategory configurationCategory = new ConfigurationItemCategory();
    configurationCategory.setName((RandomStringUtils.random(10, true, true)));
    em.persist(configurationCategory);
    em.flush();
    configurationCategories.add(configurationCategory);
    return configurationCategory;
  }


  public void addToDelete(ConfigurationItemCategory configurationCategory) {
    configurationCategories.add(configurationCategory);
  }


  public void cleanup() {
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<ConfigurationItemCategory> delete = criteriaBuilder
        .createCriteriaDelete(ConfigurationItemCategory.class);
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted configuration categorys: {}", cnt);
    em.flush();
  }
}
