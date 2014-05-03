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
//     this.em.remove(item);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationProfileItem updateItem(ConfigurationProfileItem item) {
    return this.em.merge(item);
  }
}
