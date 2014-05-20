package cz.i.cis.config.ejb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cz.i.cis.config.ejb.dao.exceptions.ActiveItemKeyException;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemKeyDaoException;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;

/**
 * Data access object for work with {@code ConfigurationItemKey} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemKeyDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Finds configuration item key entity with entered id.
   *
   * @param id identifier of configuration item key entity.
   * @return Found configuration item key entity or {@code null} if the entity does not exist.
   */
  public ConfigurationItemKey getItemKey(Integer id) {
    return em.find(ConfigurationItemKey.class, id);
  }


  /**
   * Finds configuration item key entity with entered key name.
   *
   * @param key name of configuration item key entity.
   * @return Found configuration item key entity or {@code null} if the entity does not exist.
   */
  public ConfigurationItemKey getItemKey(String key) {
    final TypedQuery<ConfigurationItemKey> query = em.createQuery(
        "SELECT itemKey FROM ConfigurationItemKey itemKey WHERE itemKey.key = :key", ConfigurationItemKey.class);

    query.setParameter("key", key);

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }


  /**
   * Returns list of all configuration item keys.
   *
   * @return List of all configuration item keys.
   */
  public List<ConfigurationItemKey> listItemKeys() {
    final TypedQuery<ConfigurationItemKey> query = em.createQuery("SELECT itemKey FROM ConfigurationItemKey itemKey",
        ConfigurationItemKey.class);

    return query.getResultList();
  }


  /**
   * Returns list of configuration item keys by category.
   *
   * @param category category for filter list
   * @return List of configuration item keys by category.
   */
  public List<ConfigurationItemKey> filterItemKeys(ConfigurationItemCategory category) {
    category = em.find(ConfigurationItemCategory.class, category.getId());

    final TypedQuery<ConfigurationItemKey> query = em.createQuery(
        "SELECT itemKey FROM ConfigurationItemKey itemKey WHERE itemKey.category = :category",
        ConfigurationItemKey.class);
    query.setParameter("category", category);

    return query.getResultList();
  }


  /**
   * Inserts configuration item key entity into database.
   *
   * @param key configuration item key entity which will be inserted into database.
   * @throws ConfigurationItemKeyDaoException If configuration item key cannot be added.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItemKey(ConfigurationItemKey key) throws ConfigurationItemKeyDaoException {
    try {
      em.persist(key);
      em.flush(); // to get persistence exception
    } catch (PersistenceException e) {
      throw new ConfigurationItemKeyDaoException("Cannot insert key: " + key, e);
    }
  }


  /**
   * Updates entered configuration item key entity.
   *
   * @param key configuration item key entity which will be updated.
   * @return Updated instance of configuration item key entity.
   * @throws ConfigurationItemKeyDaoException If configuration item key cannot be updated.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemKey updateItemKey(ConfigurationItemKey key) throws ConfigurationItemKeyDaoException {
    try {
      ConfigurationItemKey merged = em.merge(key);
      em.flush();

      return merged;
    } catch (PersistenceException e) {
      throw new ConfigurationItemKeyDaoException("Cannot update key: " + key, e);
    }
  }


  /**
   * Deletes configuration item key entity by entered id.
   *
   * @param id identifier of configuration item entity which will be deleted.
   * @throws ActiveItemKeyException If an item key is used at active configuration.
   * @throws ConfigurationItemKeyDaoException If cannot remove item key with entered id.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItemKey(Integer id) throws ActiveItemKeyException, ConfigurationItemKeyDaoException {
    ConfigurationItemKey itemKey = getItemKey(id);

    final TypedQuery<ConfigurationItem> activeQuery = em.createQuery(
        "SELECT item FROM ConfigurationItem item WHERE item.key = :itemKey", ConfigurationItem.class);
    activeQuery.setParameter("itemKey", itemKey);

    if (!activeQuery.getResultList().isEmpty()) {
      throw new ActiveItemKeyException("Key " + itemKey.getKey() + " is used in an active configuration!");
    }

    try {
      final Query profileDeleteQuery = em.createQuery(
          "DELETE FROM ConfigurationProfileItem item WHERE item.key = :itemKey");
      profileDeleteQuery.setParameter("itemKey", itemKey);
      profileDeleteQuery.executeUpdate();
      em.flush();

      em.remove(itemKey);
      em.flush();
    } catch (PersistenceException e) {
      throw new ConfigurationItemKeyDaoException("Cannot remove key with id: " + id, e);
    }
  }


  /**
   * Returns map of entered list of configuration item keys.
   *
   * @param itemKeys list of configuration item keys for put to map.
   * @return Map of entered list of configuration item keys.
   */
  public static Map<String, ConfigurationItemKey> getItemKeyMap(List<ConfigurationItemKey> itemKeys) {
    Map<String, ConfigurationItemKey> itemKeyMap = new HashMap<>();
    for (ConfigurationItemKey itemKey : itemKeys) {
      itemKeyMap.put(itemKey.getId() + "", itemKey);
    }

    return itemKeyMap;
  }
}
