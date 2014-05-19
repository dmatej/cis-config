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
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.ConfigurationItemCategory;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationCategoryDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public ConfigurationItemCategory getCategory(Integer id) {
    return em.find(ConfigurationItemCategory.class, id);
  }

  public ConfigurationItemCategory getCategory(String name) {
    final TypedQuery<ConfigurationItemCategory> query = em.createQuery(
        "SELECT category FROM ConfigurationItemCategory category WHERE category.name = :name",
        ConfigurationItemCategory.class);

    query.setParameter("name", name);

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<ConfigurationItemCategory> listCategories() {
    final TypedQuery<ConfigurationItemCategory> query = em.createQuery(
        "SELECT category FROM ConfigurationItemCategory category", ConfigurationItemCategory.class);

    return query.getResultList();
  }


  public Map<String, ConfigurationItemCategory> getCategoryMap() {
    Map<String, ConfigurationItemCategory> categoryMap = new HashMap<>();
    for (ConfigurationItemCategory category : listCategories()) {
      categoryMap.put(category.getId().toString(), category);
    }

    return categoryMap;
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addCategory(ConfigurationItemCategory category) {
    em.persist(category);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemCategory updateCategory(ConfigurationItemCategory category) {
    return em.merge(category);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(Integer id) {
    em.remove(em.getReference(ConfigurationItemCategory.class, id));
  }
}
