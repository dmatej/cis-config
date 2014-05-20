package cz.i.cis.config.helpers;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
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
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;

@Stateless
@Local
public class ConfigurationItemKeyTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemKeyTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;

  private Set<ConfigurationItemKey> keys = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper categorHelper;

  public ConfigurationItemKeyTestHelper() {
  }


  public ConfigurationItemKey createConfigurationKey() {
    ConfigurationItemCategory configuration_category =  categorHelper.createConfigurationCategory();
    ConfigurationItemKey key = new ConfigurationItemKey();
    key.setKey((RandomStringUtils.random(3, true, true)));
    key.setDescription("my key");
    key.setType(ConfigurationItemKeyType.URL);
    key.setCategory(configuration_category);
    em.persist(key);
    em.flush();
    keys.add(key);
    return key;
  }

  public void addToDelete(ConfigurationItemKey configItemKey) {
   keys.add(configItemKey);
  }


  public void cleanup() {
//    if (keys.isEmpty()) {
//      return;
//    }

    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<ConfigurationItemKey> delete = criteriaBuilder
        .createCriteriaDelete(ConfigurationItemKey.class);
//    Root<ConfigurationItemKey> root = delete.from(ConfigurationItemKey.class);
//    delete.where(root.in(keys));
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted configuration item keys: {}", cnt);
  }
}

