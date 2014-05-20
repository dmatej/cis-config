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
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemCategoryDaoException;
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
   * @throws ConfigurationItemCategoryDaoException If entered category cannot be added.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addCategory(ConfigurationItemCategory category) throws ConfigurationItemCategoryDaoException {
    try {
      em.persist(category);
      em.flush();
    } catch (PersistenceException exc) {
      throw new ConfigurationItemCategoryDaoException("Cannot insert category " + category);
    }
  }


  /**
   * Updates entered configuration item category entity.
   *
   * @param category configuration item category entity which will be updated.
   * @return Updated instance of configuration item category entity.
   * @throws ConfigurationItemCategoryDaoException If entered category cannot be updated.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemCategory updateCategory(ConfigurationItemCategory category)
    throws ConfigurationItemCategoryDaoException {
    try {
      ConfigurationItemCategory merged = em.merge(category);
      em.flush();

      return merged;
    } catch (PersistenceException e) {
      throw new ConfigurationItemCategoryDaoException("Cannot update category: " + category, e);
    }
  }


  /**
   * Deletes configuration item category entity by entered id.
   *
   * @param id identifier of configuration item category entity which will be deleted.
   * @throws ConfigurationItemCategoryDaoException If cannot remove category with entered id.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(Integer id) throws ConfigurationItemCategoryDaoException {
    try {
      em.remove(em.getReference(ConfigurationItemCategory.class, id));
      em.flush();
    } catch (PersistenceException e) {
      throw new ConfigurationItemCategoryDaoException("Cannot remove category with id: " + id, e);
    }
  }
}
