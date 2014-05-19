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
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.ConfigurationProfileItem;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationProfileItemDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<ConfigurationProfileItem> listItems() {
    final TypedQuery<ConfigurationProfileItem> query = em.createQuery("SELECT item FROM ConfigurationProfileItem item",
        ConfigurationProfileItem.class);

    return query.getResultList();
  }


  public List<ConfigurationProfileItem> listItems(Integer profileID) {
    final TypedQuery<ConfigurationProfileItem> query = em.createQuery(
        "SELECT item FROM ConfigurationProfileItem item WHERE item.profile.id = :profileID",
        ConfigurationProfileItem.class);

    query.setParameter("profileID", profileID);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationProfileItem item) {
    em.persist(item);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfileItem updateItem(ConfigurationProfileItem item) {
    return em.merge(item);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationProfileItem item) {
    em.remove(em.getReference(ConfigurationProfileItem.class, item.getId()));
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<ConfigurationProfileItem> saveChanges(Map<String, ConfigurationProfileItem> profileItems) {
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


  public static Map<String, ConfigurationProfileItem> getItemMap(List<ConfigurationProfileItem> items) {
    Map<String, ConfigurationProfileItem> itemMap = new HashMap<>();
    for (ConfigurationProfileItem item : items) {
      itemMap.put(item.getId().toString(), item);
    }

    return itemMap;
  }
}
