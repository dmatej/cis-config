package cz.i.cis.config.helpers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.jpa.CisUser;

@Stateless
@Local
public class UserTestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(UserTestHelper.class);
  @PersistenceContext(unitName = "cis-jta")
  private EntityManager em;
  private Set<CisUser> users = new HashSet<>();


  public UserTestHelper() {
  }


  public CisUser createUser() {
    final CisUser user = new CisUser();
    user.setLastName("XTMatějka");
    user.setLogin(RandomStringUtils.random(6, true, true));
    user.setFirstName("XTAfanasi");
    user.setBirthDate(new Date());
    em.persist(user);
    em.flush();
    users.add(user);
    return user;
  }


  public void addToDelete(CisUser user) {
    users.add(user);
  }


  public void cleanup() {
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaDelete<CisUser> delete = criteriaBuilder.createCriteriaDelete(CisUser.class);
    Query query = em.createQuery(delete);
    int cnt = query.executeUpdate();
    LOG.debug("Deleted users: {}", cnt);
  }
}
