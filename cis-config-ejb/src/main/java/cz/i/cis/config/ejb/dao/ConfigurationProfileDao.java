package cz.i.cis.config.ejb.dao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.ConfigurationProfile;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationProfileDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile getProfile(Integer id) {
    return em.find(ConfigurationProfile.class, id);
  }


  public List<ConfigurationProfile> listProfiles() {
    final TypedQuery<ConfigurationProfile> query = em.createQuery("SELECT profile FROM ConfigurationProfile profile",
        ConfigurationProfile.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addProfile(ConfigurationProfile profile) {
    em.persist(profile);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile updateProfile(ConfigurationProfile profile) {
    return em.merge(profile);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeProfile(Integer id) {
    ConfigurationProfile profile = getProfile(id);
    final Query profileItemsDeleteQuery = em.createQuery("DELETE FROM ConfigurationProfileItem profileItem WHERE profileItem.profile = :profile");
    profileItemsDeleteQuery.setParameter("profile", profile);
    profileItemsDeleteQuery.executeUpdate();

    em.remove(profile);
  }
}
