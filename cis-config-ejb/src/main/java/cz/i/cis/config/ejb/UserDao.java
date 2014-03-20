/**
 *
 */
package cz.i.cis.config.ejb;

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

import cz.i.cis.config.jpa.CisUser;


/**
 * @author David Matějček
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserDao {

  @PersistenceContext(name="cis-jta")
  private EntityManager em;

  public UserDao() {
  }

  public List<CisUser> listUsers() {
		final TypedQuery<CisUser> query = this.em.createQuery(
				"select user from CisUser user", CisUser.class);

    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addUser(CisUser user) {
    this.em.persist(user);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeUser(CisUser user) {
    this.em.remove(user);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public CisUser updateUser(CisUser user) {
    return this.em.merge(user);
  }
}
