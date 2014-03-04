/**
 *
 */
package cz.i.cis.config.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cz.i.cis.config.jpa.usr.en.CisUser;


/**
 * @author David Matějček
 */
@Local
@Stateless
public class UserDao {

  @PersistenceContext(name="cis-jta")
  private EntityManager em;

  public List<CisUser> listUsers() {
    final TypedQuery<CisUser> query = em.createQuery("select user from CisUser user", CisUser.class);
    return query.getResultList();
  }
}
