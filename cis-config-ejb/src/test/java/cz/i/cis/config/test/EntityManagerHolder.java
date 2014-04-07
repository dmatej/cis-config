/**
 *
 */
package cz.i.cis.config.test;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author David Matějček
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local
public class EntityManagerHolder {

  private static final Logger LOG = LoggerFactory.getLogger(EntityManagerHolder.class);

  private EntityManager em;


  @PersistenceContext(unitName = "cis-jta", type = PersistenceContextType.EXTENDED)
  private void setEntityManager(EntityManager em) {
    LOG.info("setEntityManager(em={})", em);
    this.em = em;
  }


  /**
   * @return an injected entity manager
   */
  public EntityManager getEntityManager() {
    Assert.assertNotNull("Entity manager is null.", em);
    return em;
  }
}
