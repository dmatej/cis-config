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
import javax.persistence.criteria.Root;

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
  private Set<ConfigurationItem> configuration_items = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelpe")
  private UserTestHelper user_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private   ConfigurationItemKeyTestHelper key_helper;

  public ConfigurationItemTestHelper() {
  }


  public ConfigurationItem createConfigurationItem() {
    final CisUser user = user_helper.createUser();
    final ConfigurationItemKey key = key_helper.createConfigurationKey();
    final ConfigurationItem configuration_item = new ConfigurationItem();

    configuration_item.setUser(user);
    configuration_item.setKey(key);
    configuration_item.setValue(RandomStringUtils.random(6, true, true));
    configuration_item.setUpdate(new Date());
    em.persist(configuration_item);
    em.flush();
    configuration_items.add(configuration_item);
    return configuration_item;
  }


  public void addToDelete(ConfigurationItem configuration_item) {
    configuration_items.add(configuration_item);
  }


  public void cleanup() {
    if (configuration_items.isEmpty()) {
      return;
    }
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaDelete<ConfigurationItem> delete = criteriaBuilder
          .createCriteriaDelete(ConfigurationItem.class);
      Root<ConfigurationItem> root = delete.from(ConfigurationItem.class);
      delete.where(root.in(configuration_items));
      Query query = em.createQuery(delete);
      int cnt = query.executeUpdate();
      LOG.debug("Deleted configuration items: {}", cnt);
    } catch (Exception e) {
      LOG.error("Cleanup failed", e);
    }
  }
}
