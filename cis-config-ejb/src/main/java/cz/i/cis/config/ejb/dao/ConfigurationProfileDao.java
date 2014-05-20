package cz.i.cis.config.ejb.dao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.i.cis.config.ejb.dao.exceptions.CisUserDaoException;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationProfileDaoException;
import cz.i.cis.config.jpa.ConfigurationProfile;

/**
 * Data access object for work with {@code ConfigurationProfile} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationProfileDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Finds configuration profile entity with entered id.
   *
   * @param id identifier of configuration profile entity.
   * @return Found configuration profile entity or {@code null} if the entity does not exist.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile getProfile(Integer id) {
    return em.find(ConfigurationProfile.class, id);
  }


  /**
   * Finds configuration profile entity with entered name.
   *
   * @param name name of configuration profile entity.
   * @return Found configuration profile entity or {@code null} if the entity does not exist.
   */
  public ConfigurationProfile getProfile(String name) {
    final TypedQuery<ConfigurationProfile> query = em.createQuery(
        "SELECT profile FROM ConfigurationProfile profile WHERE profile.name = :name", ConfigurationProfile.class);

    query.setParameter("name", name);

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }


  /**
   * Returns list of all configuration profiles.
   *
   * @return List of all configuration profiles.
   */
  public List<ConfigurationProfile> listProfiles() {
    final TypedQuery<ConfigurationProfile> query = em.createQuery("SELECT profile FROM ConfigurationProfile profile",
        ConfigurationProfile.class);

    return query.getResultList();
  }


  /**
   * Inserts configuration profile entity into database.
   *
   * @param profile configuration profile entity which will be inserted into database.
   * @throws ConfigurationProfileDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addProfile(ConfigurationProfile profile) throws ConfigurationProfileDaoException {
    try {
      em.persist(profile);
      em.flush(); // to get persistence exception
    } catch (PersistenceException e) {
      throw new ConfigurationProfileDaoException("Cannot insert profile " + profile, e);
    }
  }


  /**
   * Updates entered configuration profile entity.
   *
   * @param profile configuration profile entity which will be updated.
   * @return Updated instance of configuration profile entity.
   * @throws CisUserDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfile updateProfile(ConfigurationProfile profile) throws CisUserDaoException {
    try {
      ConfigurationProfile merged = em.merge(profile);
      em.flush();

      return merged;
    } catch (PersistenceException e) {
      throw new CisUserDaoException("Cannot update profile " + profile, e);
    }
  }


  /**
   * Deletes configuration profile entity by entered id.
   *
   * @param id identifier of configuration profile entity which will be deleted.
   * @throws CisUserDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeProfile(Integer id) throws CisUserDaoException {
    ConfigurationProfile profile = getProfile(id);
    final Query profileItemsDeleteQuery = em
        .createQuery("DELETE FROM ConfigurationProfileItem profileItem WHERE profileItem.profile = :profile");
    profileItemsDeleteQuery.setParameter("profile", profile);
    profileItemsDeleteQuery.executeUpdate();

    try {
      em.remove(profile);
      em.flush();
    } catch (PersistenceException e) {
      throw new CisUserDaoException("Cannot remove profile " + profile, e);
    }
  }
}
