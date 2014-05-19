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
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.ConfigurationItemCategory;

@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationCategoryDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<ConfigurationItemCategory> listCategories() {
    final TypedQuery<ConfigurationItemCategory> query = this.em.createQuery(
        "select category from ConfigurationItemCategory category",
        ConfigurationItemCategory.class);

    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addCategory(ConfigurationItemCategory category) {
    this.em.persist(category);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(ConfigurationItemCategory category) {
    ConfigurationItemCategory managed = em.merge(category);
    this.em.remove(managed);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeCategory(Integer id) {
    ConfigurationItemCategory category = getCategory(id);
    removeCategory(category);
  }

  public ConfigurationItemCategory getCategory(Integer id) {
    return em.find(ConfigurationItemCategory.class, id);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ConfigurationItemCategory updateCategory(ConfigurationItemCategory category) {
    return this.em.merge(category);
  }

  public Map<String, ConfigurationItemCategory> getCategoryMap() {
    Map<String, ConfigurationItemCategory> categoryMap = new HashMap<>();
    for (ConfigurationItemCategory category : listCategories()) {
      categoryMap.put(category.getId().toString(), category);
    }

    return categoryMap;
  }


  public static ConfigurationItemCategory getCategory(List<ConfigurationItemCategory> categories, Integer id) {
    for (ConfigurationItemCategory category : categories) {
      if(category.getId().intValue() == id.intValue()) {
        return category;
      }
    }

    return null;
  }
}
