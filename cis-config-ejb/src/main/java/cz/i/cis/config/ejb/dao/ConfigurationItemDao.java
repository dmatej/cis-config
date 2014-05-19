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

import cz.i.cis.config.ejb.dao.exceptions.UniqueKeyException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfileItem;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<ConfigurationItem> listItems() {
    final TypedQuery<ConfigurationItem> query = this.em.createQuery("select item from ConfigurationItem item",
        ConfigurationItem.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationItem item) throws UniqueKeyException {
    try {
      this.em.persist(item);
      this.em.flush();
    } catch (PersistenceException e) {
      throw new UniqueKeyException("ConfigurationItem with unique foreign key " + item.getKey().getKey()
          + " already exists!", e);
    }
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItem getItem(Integer id) {
    return em.find(ConfigurationItem.class, id);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationItem item) {
    this.em.remove(this.em.getReference(ConfigurationItem.class, item.getId()));
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(Integer id) {
    ConfigurationItem item = getItem(id);
    this.em.remove(item);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItem updateItem(ConfigurationItem item) {
    return this.em.merge(item);
  }


  public List<ConfigurationItem> listConfigurationItems(ConfigurationItemCategory category) {
    final TypedQuery<ConfigurationItem> query = this.em.createQuery("select item FROM ConfigurationItem item "
        + "WHERE item.key.category = :category", ConfigurationItem.class);
    query.setParameter("category", category);
    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void activateProfile(List<ConfigurationProfileItem> profileItems, CisUser user) {
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
