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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.exceptions.UniqueKeyException;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;


@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemKeyDao {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemKeyDao.class);

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<ConfigurationItemKey> listItemKeys() {
    LOG.debug("listItemKeys()");
    final TypedQuery<ConfigurationItemKey> query = this.em.createQuery(
        "select itemKey from ConfigurationItemKey itemKey", ConfigurationItemKey.class);

    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItemKey(ConfigurationItemKey key) throws UniqueKeyException {
    LOG.debug("addItemKey(key={})", key);
   // this.em.persist(key);
    try {
      this.em.persist(key);
      em.flush();
    }
    catch (PersistenceException exc) {
      throw new UniqueKeyException("Key" + key.getKey() + " already exists!", exc);
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItemKey(ConfigurationItemKey key) {
    LOG.debug("removeItemKey(key={})", key);
    this.em.remove(key);
  }

  public ConfigurationItemKey updateItemKey(ConfigurationItemKey key) {
    LOG.debug("updateItemKey(key={})", key);
    return this.em.merge(key);
  }

  public List<ConfigurationItemKey> filterItemKeys(ConfigurationItemCategory category){
    LOG.debug("filterItemKeys(category={})", category);
    // FIXME: coze? hrozi nechteny update! ...
    category = em.merge(category);

    final TypedQuery<ConfigurationItemKey> query = this.em.createQuery(
        "SELECT itemKey FROM ConfigurationItemKey itemKey WHERE itemKey.category = :category", ConfigurationItemKey.class);
    query.setParameter("category", category);

    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItemKey(Integer id) {
    LOG.debug("removeItemKey(id={})", id);
    ConfigurationItemKey itemKey = getItemKey(id);
    em.remove(itemKey);
  }

  public ConfigurationItemKey getItemKey(Integer id){
    LOG.debug("getItemKey(id={})", id);
    return em.find(ConfigurationItemKey.class, id);
  }

  public static Map<String, ConfigurationItemKey> getItemKeyMap(List<ConfigurationItemKey> itemKeys) {
    LOG.debug("getItemKeyMap(itemKeys={})", itemKeys);
    Map<String, ConfigurationItemKey> itemKeyMap = new HashMap<>();
    for (ConfigurationItemKey itemKey : itemKeys) {
      itemKeyMap.put(itemKey.getId().toString(), itemKey);
    }

    return itemKeyMap;
  }
}
