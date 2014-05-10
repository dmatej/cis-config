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


  public ConfigurationProfileDao() {
  }


  public List<ConfigurationProfile> listProfiles() {
    final TypedQuery<ConfigurationProfile> query = this.em.createQuery(
        "select profile from ConfigurationProfile profile", ConfigurationProfile.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addProfile(ConfigurationProfile profile) {
    this.em.persist(profile);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile getProfile(Integer id) {
    return em.find(ConfigurationProfile.class, id);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeProfile(ConfigurationProfile profile) {
    this.em.remove(profile);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeProfile(Integer id) {
    ConfigurationProfile profile = getProfile(id);
    final Query profileItemsDeleteQuery = this.em.createQuery("DELETE FROM ConfigurationProfileItem profileItem WHERE profileItem.profile = :profile");
    profileItemsDeleteQuery.setParameter("profile", profile);
    profileItemsDeleteQuery.executeUpdate();

    this.em.remove(profile);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile updateProfile(ConfigurationProfile profile) {
    return this.em.merge(profile);
  }
}
