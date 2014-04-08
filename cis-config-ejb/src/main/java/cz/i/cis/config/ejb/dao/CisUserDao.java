/**
 *
 */
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

import cz.i.cis.config.jpa.CisUser;

/**
 * @author David Matějček
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CisUserDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public CisUserDao() {
  }


  public List<CisUser> listUsers() {
    final TypedQuery<CisUser> query = this.em.createQuery("select user from CisUser user", CisUser.class);

    return query.getResultList();
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addUser(CisUser user) throws UserAlreadyExistsException {
    try {
      this.em.persist(user);
      em.flush();
    } catch (PersistenceException e) {
      throw new UserAlreadyExistsException("User " + user.getLogin() + " already exists!", e);
    }
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeUser(CisUser user) {
    CisUser u = this.em.merge(user);
    this.em.remove(u);
    // this.em.remove(user);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeUser(Integer id) {
    CisUser user = getUser(id);
    removeUser(user);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public CisUser updateUser(CisUser user) {
    return this.em.merge(user);
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public CisUser getUser(Integer id) {
    return em.find(CisUser.class, id);
  }
}
