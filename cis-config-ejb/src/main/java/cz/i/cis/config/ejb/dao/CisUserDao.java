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
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.CisUser;

/**
 * Data access object for work with {@code CisUser} entities.
 */
@Local
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CisUserDao {

  /** JPA entity manager for work with entities. */
  @PersistenceContext(name = "cis-jta")
  private EntityManager em;


  /**
   * Finds user entity with entered id.
   *
   * @param id identifier of user entity.
   * @return Found user entity or {@code null} if the entity does not exist.
   */
  public CisUser getUser(Integer id) {
    return em.find(CisUser.class, id);
  }


  /**
   * Finds user entity with entered login.
   *
   * @param login login of user entity.
   * @return Found user entity or {@code null} if the entity does not exist.
   */
  public CisUser getUser(String login) {
    final TypedQuery<CisUser> query = em.createQuery("SELECT user FROM CisUser user WHERE user.login = :login",
        CisUser.class);
    query.setParameter("login", login);
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }


  /**
   * Returns list of all users.
   *
   * @return List of all users.
   */
  public List<CisUser> listUsers() {
    final TypedQuery<CisUser> query = em.createQuery("SELECT user FROM CisUser user", CisUser.class);
    return query.getResultList();
  }


  /**
   * Inserts user entity into database.
   *
   * @param user user entity which will be inserted into database.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void addUser(CisUser user) {
    em.persist(user);
  }


  /**
   * Updates entered user entity.
   *
   * @param user user entity which will be updated.
   * @return Updated instance of user entity.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public CisUser updateUser(CisUser user) {
    return em.merge(user);
  }


  /**
   * Updates user status for entered user with entered status.
   *
   * @param user user entity which will have updated status.
   * @param newStatus new status for entered user.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateUserStatus(CisUser user, Integer newStatus) {
    user.setStatus(newStatus);
    updateUser(user);
  }


  /**
   * Deletes user entity by entered id (sets status to {@link CisUser#STATUS_DELETED}).
   *
   * @param id identifier of user entity which will be deleted.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void removeUser(Integer id) {
    CisUser user = getUser(id);
    updateUserStatus(user, CisUser.STATUS_DELETED);
  }


  /**
   * Restores user entity by entered id (sets status to {@link CisUser#STATUS_VALID}).
   *
   * @param id identifier of user entity which will be deleted.
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void restoreUser(Integer id) {
    CisUser user = getUser(id);
    if (user.getStatus() == CisUser.STATUS_DELETED) {
      updateUserStatus(user, CisUser.STATUS_VALID);
      return;
    }
    throw new IllegalStateException("User is not deleted: " + user);
  }
}
