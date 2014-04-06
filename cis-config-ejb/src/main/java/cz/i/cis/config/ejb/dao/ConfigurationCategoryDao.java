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

import cz.i.cis.config.jpa.ConfigurationItemCategory;


@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigurationCategoryDao {
	@PersistenceContext(name = "cis-jta")
	private EntityManager em;

	
	public ConfigurationCategoryDao() {
	}

	
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
		this.em.remove(category);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ConfigurationItemCategory updateCategory(ConfigurationItemCategory category) {
		return this.em.merge(category);
	}
}
