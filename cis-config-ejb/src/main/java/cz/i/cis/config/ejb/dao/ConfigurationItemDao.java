package cz.i.cis.config.ejb.dao;

import java.util.List;

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

import cz.i.cis.config.jpa.ConfigurationItem;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public ConfigurationItemDao() {
  }


  public List<ConfigurationItem> listItems() {
    final TypedQuery<ConfigurationItem> query = this.em.createQuery("select item from ConfigurationItem item",
        ConfigurationItem.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addItem(ConfigurationItem item) // throws UniqueKeyException
  {
    // try{
    this.em.persist(item);
    // }catch (PersistenceException e) {
    // throw new UniqueKeyException("ConfigurationItem with unique foreign key " +
    // item.getKey().getKey() + " already exists!", e);
    // }

  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeItem(ConfigurationItem item) {
    ConfigurationItem i = this.em.merge(item);
    this.em.remove(i);
    // this.em.remove(item);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItem updateItem(ConfigurationItem item) {
    return this.em.merge(item);
  }
}
