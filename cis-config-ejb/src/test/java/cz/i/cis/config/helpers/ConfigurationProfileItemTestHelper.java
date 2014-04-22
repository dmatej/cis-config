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
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

@Stateless
@Local
public class ConfigurationProfileItemTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileItemTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationProfileItem> profile_items = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper key_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper profile_helper;


  public ConfigurationProfileItemTestHelper() {
  }


  public ConfigurationProfileItem createConfigurationProfileItem() {
    final ConfigurationItemKey key = key_helper.createConfigurationKey();
    final ConfigurationProfile profile = profile_helper.createConfigurationProfile();

    final ConfigurationProfileItem profile_item = new ConfigurationProfileItem();
    profile_item.setKey(key);
    profile_item.setProfile(profile);
    profile_item.setValue((RandomStringUtils.random(5, true, true)));

    em.persist(profile_item);
    em.flush();
    profile_items.add(profile_item);
    return profile_item;
  }


  public void addToDelete(ConfigurationProfileItem profile_item) {
    profile_items.add(profile_item);
  }


  public void cleanup() {
    if (profile_items.isEmpty()) {
      return;
    }
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaDelete<ConfigurationProfileItem> delete = criteriaBuilder
          .createCriteriaDelete(ConfigurationProfileItem.class);
      Root<ConfigurationProfileItem> root = delete.from(ConfigurationProfileItem.class);
      delete.where(root.in(profile_items));
      Query query = em.createQuery(delete);
      int cnt = query.executeUpdate();
      LOG.debug("Deleted configuration profile items: {}", cnt);
    } catch (Exception e) {
      LOG.error("Cleanup failed", e);
    }
  }
}
