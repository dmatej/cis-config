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

import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

@Stateless
@Local
public class ConfigurationProfileItemTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileItemTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationProfileItem> profileItems = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper keyHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper profileHelper;


  public ConfigurationProfileItemTestHelper() {
  }


  public ConfigurationProfileItem createConfigurationProfileItem() {
    final ConfigurationItemKey key = keyHelper.createConfigurationKey();
    final ConfigurationProfile profile = profileHelper.createConfigurationProfile();

    final ConfigurationProfileItem profileItem = new ConfigurationProfileItem();
    profileItem.setKey(key);
    profileItem.setProfile(profile);
    profileItem.setValue((RandomStringUtils.random(5, true, true)));

    em.persist(profileItem);
    em.flush();
    profileItems.add(profileItem);
    return profileItem;
  }


  public void addToDelete(ConfigurationProfileItem profileItem) {
    profileItems.add(profileItem);
  }


  public void cleanup() {
    LOG.debug("cleanup()");
//    if (profile_items.isEmpty()) {
//      return;
//    }
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<ConfigurationProfileItem> delete = criteriaBuilder
        .createCriteriaDelete(ConfigurationProfileItem.class);
//    Root<ConfigurationProfileItem> root = delete.from(ConfigurationProfileItem.class);
//    delete.where(root.in(profile_items));
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted configuration profile items: {}", cnt);
    int profileCnt = em.createQuery("delete from ConfigurationProfile p").executeUpdate();
    LOG.debug("Deleted configuration profiles: {}", profileCnt);
  }
}
