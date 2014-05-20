package cz.i.cis.config.ejb.dao;

import java.util.ArrayList;
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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.i.cis.config.ejb.dao.exceptions.ConfigurationProfileItemDaoException;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

/**
 * Data access object for work with {@code ConfigurationItemCategory} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationProfileItemDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Returns list of all configuration profile items.
   *
   * @return List of all configuration profile items.
   */
  public List<ConfigurationProfileItem> listItems() {
    final TypedQuery<ConfigurationProfileItem> query = em.createQuery("SELECT item FROM ConfigurationProfileItem item",
        ConfigurationProfileItem.class);

    return query.getResultList();
  }


  /**
   * Returns list of configuration profile items by entered profile id.
   *
   * @param profileID profile identifier for filter list
   * @return List of configuration profile items by entered profile id.
   */
  public List<ConfigurationProfileItem> listItems(Integer profileID) {
    final TypedQuery<ConfigurationProfileItem> query = em.createQuery(
        "SELECT item FROM ConfigurationProfileItem item WHERE item.profile.id = :profileID",
        ConfigurationProfileItem.class);

    query.setParameter("profileID", profileID);

    return query.getResultList();
  }


  /**
   * Inserts configuration profile item entity into database.
   *
   * @param item configuration profile item entity which will be inserted into database.
   * @throws ConfigurationProfileItemDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationProfileItem item) throws ConfigurationProfileItemDaoException {
    try {
      em.persist(item);
      em.flush(); // to get persistence exception
    } catch (PersistenceException e) {
      throw new ConfigurationProfileItemDaoException("Cannot insert profile item " + item, e);
    }
  }


  /**
   * Updates entered configuration profile item entity.
   *
   * @param item configuration profile item entity which will be updated.
   * @return Updated instance of configuration profile item entity.
   * @throws ConfigurationProfileItemDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfileItem updateItem(ConfigurationProfileItem item) throws ConfigurationProfileItemDaoException {
    try {
      ConfigurationProfileItem merged = em.merge(item);
      em.flush();

      return merged;
    } catch (PersistenceException e) {
      throw new ConfigurationProfileItemDaoException("Cannot update profile item " + item, e);
    }
  }


  /**
   * Deletes entered configuration profile item entity.
   *
   * @param item configuration profile item entity which will be deleted.
   * @throws ConfigurationProfileItemDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationProfileItem item) throws ConfigurationProfileItemDaoException {
    try {
      em.remove(em.getReference(ConfigurationProfileItem.class, item.getId()));
      em.flush(); // to get persistence exception
    } catch (PersistenceException e) {
      throw new ConfigurationProfileItemDaoException("Cannot remove profile item " + item, e);
    }
  }


  /**
   * Save changes in configuration profile.
   *
   * @param profileItems configuration profile items entity that will be saved.
   * @return List of configuration profile items entity.
   * @throws ConfigurationProfileItemDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<ConfigurationProfileItem> saveChanges(Map<String, ConfigurationProfileItem> profileItems)
    throws ConfigurationProfileItemDaoException {
    List<ConfigurationProfileItem> updatedItems = new ArrayList<>(profileItems.size());

    for (ConfigurationProfileItem item : profileItems.values()) {
      if (item.isDeleted()) {
        removeItem(item);
        continue;
      }

      ConfigurationProfileItem updatedItem = updateItem(item);
      updatedItems.add(updatedItem);
    }
    return updatedItems;
  }


  /**
   * Returns map of entered list of configuration profile items.
   *
   * @param items list of configuration profile items for put to map.
   * @return Map of entered list of configuration profile items.
   */
  public static Map<String, ConfigurationProfileItem> getItemMap(List<ConfigurationProfileItem> items) {
    Map<String, ConfigurationProfileItem> itemMap = new HashMap<>();
    for (ConfigurationProfileItem item : items) {
      itemMap.put(item.getId().toString(), item);
    }

    return itemMap;
  }
}
