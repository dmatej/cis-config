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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;


@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CisUserDao {

  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  public List<CisUser> listUsers() {
    final TypedQuery<CisUser> query = this.em.createQuery("select user from CisUser user", CisUser.class);
    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addUser(CisUser user) throws UserAlreadyExistsException {
    try {
      this.em.persist(user);
      em.flush();
    } catch (PersistenceException exc) {
      throw new UserAlreadyExistsException("User " + user.getLogin() + " already exists!", exc);
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeUser(CisUser user) {
    updateUserStatus(user, CisUser.STATUS_DELETED);
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

  public CisUser getUser(Integer id) {
    return em.find(CisUser.class, id);
  }

  public CisUser getUser(String login) {
    final TypedQuery<CisUser> query = this.em.createQuery("select user from CisUser user where user.login = :login", CisUser.class);
    query.setParameter("login", login);
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void restoreUser(CisUser user) {
    if(user.getStatus() == CisUser.STATUS_DELETED){
      updateUserStatus(user, CisUser.STATUS_VALID);
      return;
    }
    throw new IllegalStateException("User is not deleted: " + user);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void restoreUser(Integer id) {
    CisUser user = getUser(id);
    restoreUser(user);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateUserStatus(CisUser user, Integer newStatus) {
    user.setStatus(newStatus);
    updateUser(user);
  }
}
