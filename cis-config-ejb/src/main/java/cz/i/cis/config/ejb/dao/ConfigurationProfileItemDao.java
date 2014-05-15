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
//    this.em.persist(item);
    try {
      this.em.persist(item);
      em.flush();
    }
    catch (PersistenceException exc) {
      throw new UniqueProfileKeyException("Key" + item.getKey().getKey() +" and profile " + item.getProfile().getName() + " already exists!", exc);
    }
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationProfileItem item) {
    ConfigurationProfileItem i = this.em.merge(item);
    this.em.remove(i);
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
        "SELECT item FROM ConfigurationProfileItem item WHERE item.profile.id = :profileID", ConfigurationProfileItem.class);

    query.setParameter("profileID", profileID);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void saveChanges(Map<String, ConfigurationProfileItem> profileItems, ItemClassifier classifier) throws UniqueProfileKeyException {
    for (ConfigurationProfileItem item : new ArrayList<>(profileItems.values())) {
      Integer id = item.getId();

      // delete
      if (classifier.isDeletedItem(id)) {
        // only existing items are in deleted list
        removeItem(item);
        profileItems.remove(id);
      }
      // insert
      else if (classifier.isNewItem(id)) {
        addItem(item);
        // assure to have item under new ID in map
        profileItems.remove(id);
        profileItems.put(item.getId().toString(), item);
      }
      // update
      else {
        updateItem(item);
      }
    }
  }




  public interface ItemClassifier{
    public boolean isDeletedItem(Integer id);
    public boolean isNewItem(Integer id);
  }
}
