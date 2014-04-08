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
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.ConfigurationItemKey;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemKeyDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public ConfigurationItemKeyDao() {
  }


  public List<ConfigurationItemKey> listCategorys() {
    final TypedQuery<ConfigurationItemKey> query = this.em.createQuery("select k from ConfigurationItemKey k  ",
        ConfigurationItemKey.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addCategory(ConfigurationItemKey key) {
    this.em.persist(key);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(ConfigurationItemKey key) {
    ConfigurationItemKey k = this.em.merge(key);
    this.em.remove(k);
    // this.em.remove(key);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemKey updateCategory(ConfigurationItemKey key) {
    return this.em.merge(key);
  }
}
