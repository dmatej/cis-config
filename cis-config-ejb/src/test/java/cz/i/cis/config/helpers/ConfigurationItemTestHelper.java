package cz.i.cis.config.helpers;

import java.util.Date;
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

import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemKey;

@Stateless
@Local
public class ConfigurationItemTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationItem> configurationItems = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelpe")
  private UserTestHelper userHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private   ConfigurationItemKeyTestHelper keyHelper;

  public ConfigurationItemTestHelper() {
  }


  public ConfigurationItem createConfigurationItem() {
    final CisUser user = userHelper.createUser();
    final ConfigurationItemKey key = keyHelper.createConfigurationKey();
    final ConfigurationItem configurationItem = new ConfigurationItem();

    configurationItem.setUser(user);
    configurationItem.setKey(key);
    configurationItem.setValue(RandomStringUtils.random(6, true, true));
    configurationItem.setUpdate(new Date());
    em.persist(configurationItem);
    em.flush();
    configurationItems.add(configurationItem);
    return configurationItem;
  }


  public void addToDelete(ConfigurationItem configuration_item) {
    configurationItems.add(configuration_item);
  }


  public void cleanup() {
//    if (configuration_items.isEmpty()) {
//      return;
//    }
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<ConfigurationItem> delete = criteriaBuilder
        .createCriteriaDelete(ConfigurationItem.class);
//    Root<ConfigurationItem> root = delete.from(ConfigurationItem.class);
//    delete.where(root.in(configuration_items));
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted configuration items: {}", cnt);
  }
}
