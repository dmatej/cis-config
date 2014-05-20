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

/**
 * Data access object for work with {@code ConfigurationItemCategory} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationItemCategoryDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Finds configuration item category entity with entered id.
   *
   * @param id identifier of configuration item category entity.
   * @return Found configuration item category entity or {@code null} if the entity does not exist.
   */
  public ConfigurationItemCategory getCategory(Integer id) {
    return em.find(ConfigurationItemCategory.class, id);
  }

  /**
   * Finds configuration item category entity with entered name.
   *
   * @param name name of configuration item category entity.
   * @return Found configuration item category entity or {@code null} if the entity does not exist.
   */
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


  /**
   * Returns list of all configuration item categories.
   *
   * @return List of all configuration item categories.
   */
  public List<ConfigurationItemCategory> listCategories() {
    final TypedQuery<ConfigurationItemCategory> query = em.createQuery(
        "SELECT category FROM ConfigurationItemCategory category", ConfigurationItemCategory.class);

    return query.getResultList();
  }


  /**
   * Returns map of all configuration item categories.
   *
   * @return Map of all configuration item categories.
   */
  public Map<String, ConfigurationItemCategory> getCategoryMap() {
    Map<String, ConfigurationItemCategory> categoryMap = new HashMap<>();
    for (ConfigurationItemCategory category : listCategories()) {
      categoryMap.put(category.getId().toString(), category);
    }

    return categoryMap;
  }


  /**
   * Inserts configuration item category entity into database.
   *
   * @param category configuration item category entity which will be inserted into database.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addCategory(ConfigurationItemCategory category) {
    em.persist(category);
  }


  /**
   * Updates entered configuration item category entity.
   *
   * @param category configuration item category entity which will be updated.
   * @return Updated instance of configuration item category entity.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemCategory updateCategory(ConfigurationItemCategory category) {
    return em.merge(category);
  }


  /**
   * Deletes configuration item category entity by entered id.
   *
   * @param id identifier of configuration item category entity which will be deleted.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(Integer id) {
    em.remove(em.getReference(ConfigurationItemCategory.class, id));
  }
}
