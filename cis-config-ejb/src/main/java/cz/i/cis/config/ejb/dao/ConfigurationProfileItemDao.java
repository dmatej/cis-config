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

import cz.i.cis.config.ejb.dao.exceptions.UniqueProfileKeyException;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationProfileItemDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<ConfigurationProfileItem> listItems() {
    final TypedQuery<ConfigurationProfileItem> query = this.em.createQuery(
        "select item from ConfigurationProfileItem item", ConfigurationProfileItem.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationProfileItem item) throws UniqueProfileKeyException {
    try {
      this.em.persist(item);
      this.em.flush();
    } catch (PersistenceException exc) {
      throw new UniqueProfileKeyException("Key" + item.getKey().getKey() + " and profile "
          + item.getProfile().getName() + " already exists!", exc);
    }
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationProfileItem item) {
    this.em.remove(this.em.getReference(ConfigurationProfileItem.class, item.getId()));
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfileItem updateItem(ConfigurationProfileItem item) {
    return this.em.merge(item);
  }


  public static Map<String, ConfigurationProfileItem> getItemMap(List<ConfigurationProfileItem> items) {
    Map<String, ConfigurationProfileItem> itemMap = new HashMap<>();
    for (ConfigurationProfileItem item : items) {
      itemMap.put(item.getId().toString(), item);
    }

    return itemMap;
  }


  public List<ConfigurationProfileItem> listItems(Integer profileID) {
    final TypedQuery<ConfigurationProfileItem> query = this.em.createQuery(
        "SELECT item FROM ConfigurationProfileItem item WHERE item.profile.id = :profileID",
        ConfigurationProfileItem.class);

    query.setParameter("profileID", profileID);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<ConfigurationProfileItem> saveChanges(Map<String, ConfigurationProfileItem> profileItems)
    throws UniqueProfileKeyException {
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
}
