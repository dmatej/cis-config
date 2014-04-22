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
import cz.i.cis.config.jpa.ConfigurationProfile;

@Stateless
@Local
public class ConfigurationProfileTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationProfile> configuration_profiles = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper user_helper;


  public ConfigurationProfileTestHelper() {
  }


  public ConfigurationProfile createConfigurationProfile() {
    final CisUser user = user_helper.createUser();

    final ConfigurationProfile configuration_profile = new ConfigurationProfile();
    configuration_profile.setDescription("description");
    configuration_profile.setName((RandomStringUtils.random(10, true, true)));
    configuration_profile.setUpdate(new Date());
    configuration_profile.setUser(user);
    em.persist(configuration_profile);
    em.flush();
    configuration_profiles.add(configuration_profile);
    return configuration_profile;
  }


  public void addToDelete(ConfigurationProfile configuration_profile) {
    configuration_profiles.add(configuration_profile);
  }


  public void cleanup() {
    if (configuration_profiles.isEmpty()) {
      return;
    }
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaDelete<ConfigurationProfile> delete = criteriaBuilder.createCriteriaDelete(ConfigurationProfile.class);
      Root<ConfigurationProfile> root = delete.from(ConfigurationProfile.class);
      delete.where(root.in(configuration_profiles));
      Query query = em.createQuery(delete);
      int cnt = query.executeUpdate();
      LOG.debug("Deleted configuration profiles: {}", cnt);
    } catch (Exception e) {
      LOG.error("Cleanup failed", e);
    }
  }
}
