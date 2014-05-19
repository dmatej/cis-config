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
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cz.i.cis.config.ejb.dao.exceptions.ActiveItemKeyException;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemKeyDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public ConfigurationItemKey getItemKey(Integer id) {
    return em.find(ConfigurationItemKey.class, id);
  }

  public ConfigurationItemKey getItemKey(String key) {
    final TypedQuery<ConfigurationItemKey> query = em.createQuery(
        "SELECT itemKey FROM ConfigurationItemKey itemKey WHERE itemKey.key = :key",
        ConfigurationItemKey.class);

    query.setParameter("key", key);

    try {
      return query.getSingleResult();
    } catch (NoResultException exc) {
      return null;
    }
  }

  public List<ConfigurationItemKey> listItemKeys() {
    final TypedQuery<ConfigurationItemKey> query = em.createQuery("SELECT itemKey FROM ConfigurationItemKey itemKey",
        ConfigurationItemKey.class);

    return query.getResultList();
  }


  public List<ConfigurationItemKey> filterItemKeys(ConfigurationItemCategory category) {
    category = em.find(ConfigurationItemCategory.class, category.getId());

    final TypedQuery<ConfigurationItemKey> query = em.createQuery(
        "SELECT itemKey FROM ConfigurationItemKey itemKey WHERE itemKey.category = :category",
        ConfigurationItemKey.class);
    query.setParameter("category", category);

    return query.getResultList();
  }


  public static Map<String, ConfigurationItemKey> getItemKeyMap(List<ConfigurationItemKey> itemKeys) {
    Map<String, ConfigurationItemKey> itemKeyMap = new HashMap<>();
    for (ConfigurationItemKey itemKey : itemKeys) {
      itemKeyMap.put(itemKey.getId() + "", itemKey);
    }

    return itemKeyMap;
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItemKey(ConfigurationItemKey key) {
    em.persist(key);
  }


  public ConfigurationItemKey updateItemKey(ConfigurationItemKey key) {
    return em.merge(key);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItemKey(Integer id) throws ActiveItemKeyException {
    ConfigurationItemKey itemKey = getItemKey(id);

    final TypedQuery<ConfigurationItem> activeQuery = em.createQuery(
        "SELECT item FROM ConfigurationItem item WHERE item.key = :itemKey", ConfigurationItem.class);
    activeQuery.setParameter("itemKey", itemKey);

    if (!activeQuery.getResultList().isEmpty()) {
      throw new ActiveItemKeyException("Key " + itemKey.getKey() + " is used in an active configuration!");
    }

    final Query profileDeleteQuery = em.createQuery("DELETE FROM ConfigurationProfileItem item WHERE item.key = :itemKey");
    profileDeleteQuery.setParameter("itemKey", itemKey);
    profileDeleteQuery.executeUpdate();

    em.remove(itemKey);
  }
}
