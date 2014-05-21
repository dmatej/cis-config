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
import cz.i.cis.config.jpa.ConfigurationProfile;

@Stateless
@Local
public class ConfigurationProfileTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<ConfigurationProfile> configurationProfiles = new HashSet<>();

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper userHelper;


  public ConfigurationProfileTestHelper() {
  }


  public ConfigurationProfile createConfigurationProfile() {
    final CisUser user = userHelper.createUser();

    final ConfigurationProfile configurationProfile = new ConfigurationProfile();
    configurationProfile.setDescription("description");
    configurationProfile.setName((RandomStringUtils.random(10, true, true)));
    configurationProfile.setUpdate(new Date());
    configurationProfile.setUser(user);
    em.persist(configurationProfile);
    em.flush();
    configurationProfiles.add(configurationProfile);
    return configurationProfile;
  }


  public void addToDelete(ConfigurationProfile configurationProfile) {
    configurationProfiles.add(configurationProfile);
  }


  public void cleanup() {
//    if (configuration_profiles.isEmpty()) {
//      return;
//    }
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<ConfigurationProfile> delete = criteriaBuilder.createCriteriaDelete(ConfigurationProfile.class);
//    Root<ConfigurationProfile> root = delete.from(ConfigurationProfile.class);
//    delete.where(root.in(configuration_profiles));
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted configuration profiles: {}", cnt);
  }
}
