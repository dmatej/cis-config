package cz.i.cis.config.ejb.dao;

import java.util.Date;
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

import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemDaoException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

/**
 * Data access object for work with {@code ConfigurationItem} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Finds configuration item entity with entered id.
   *
   * @param id identifier of configuration item entity.
   * @return Found configuration item entity or {@code null} if the entity does not exist.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItem getItem(Integer id) {
    return em.find(ConfigurationItem.class, id);
  }


  /**
   * Returns list of all configuration items.
   *
   * @return List of all configuration items.
   */
  public List<ConfigurationItem> listItems() {
    final TypedQuery<ConfigurationItem> query = em.createQuery("SELECT item FROM ConfigurationItem item",
        ConfigurationItem.class);

    return query.getResultList();
  }


  /**
   * Returns list of configuration items by category.
   *
   * @param category category for filter list
   * @return List of configuration items by category.
   */
  public List<ConfigurationItem> listItems(ConfigurationItemCategory category) {
    final TypedQuery<ConfigurationItem> query = em.createQuery(
        "SELECT item FROM ConfigurationItem item WHERE item.key.category = :category", ConfigurationItem.class);
    query.setParameter("category", category);
    return query.getResultList();
  }


  /**
   * Inserts configuration item entity into database.
   *
   * @param item configuration item entity which will be inserted into database.
   * @throws ConfigurationItemDaoException
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationItem item) throws ConfigurationItemDaoException {
    try {
      em.persist(item);
      em.flush();
    } catch (PersistenceException e) {
      throw new ConfigurationItemDaoException("Cannot insert configuration item : " + item, e);
    }
  }


  /**
   * Updates entered configuration item category entity.
   *
   * @param item configuration item entity which will be updated.
   * @return Updated instance of configuration item entity.
   * @throws ConfigurationItemDaoException If entered configuration item cannot be updated.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItem updateItem(ConfigurationItem item) throws ConfigurationItemDaoException {
    try {
      ConfigurationItem merged = em.merge(item);
      em.flush();

      return merged;
    } catch (PersistenceException e) {
      throw new ConfigurationItemDaoException("Cannot update configuration item: " + item, e);
    }
  }


  /**
   * Deletes configuration item entity by entered id.
   *
   * @param id identifier of configuration item entity which will be deleted.
   * @throws ConfigurationItemDaoException If configuration item with entered id cannot be removed.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(Integer id) throws ConfigurationItemDaoException {
    try {
      em.remove(em.getReference(ConfigurationItem.class, id));
      em.flush();
    } catch (PersistenceException e) {
      throw new ConfigurationItemDaoException("Cannot remove configuration item with id: " + id, e);
    }
  }


  /**
   * Activates profile that means it inserts entered list of profile items into table for
   * configuration items.
   *
   * @param profileItems list of profile items to insert.
   * @param user user who activated profile.
   * @throws ConfigurationItemDaoException If entered profile items cannot be changed to
   *           configuration items (activation of profile items).
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void activateProfile(List<ConfigurationProfileItem> profileItems, CisUser user) throws ConfigurationItemDaoException {
    List<ConfigurationItem> configurationItems = listItems();
    Map<ConfigurationItemKey, ConfigurationItem> items = new HashMap<>();

    for (ConfigurationItem configurationItem : configurationItems) {
      items.put(configurationItem.getKey(), configurationItem);
    }

    for (ConfigurationProfileItem profileItem : profileItems) {
      ConfigurationItem item = items.get(profileItem.getKey());

      if (item == null) {
        item = new ConfigurationItem();
      }

      item.setKey(profileItem.getKey());
      item.setValue(profileItem.getValue());
      item.setUpdate(new Date());
      item.setUser(user);

      item = this.updateItem(item);
      items.put(item.getKey(), item);
    }
  }
}
