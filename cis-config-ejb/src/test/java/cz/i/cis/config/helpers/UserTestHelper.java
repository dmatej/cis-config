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
import javax.persistence.criteria.Root;

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
    final CisUser user0 = new CisUser();
    user0.setLastName("XTMatějka");
    user0.setLogin(RandomStringUtils.random(6, true, true));
    user0.setFirstName("XTAfanasi");
    user0.setBirthDate(new Date());
    em.persist(user0);
    em.flush();
    users.add(user0);
    return user0;
  }


  public void addToDelete(CisUser user) {
    users.add(user);
  }


  public void cleanup() {
    if (users.isEmpty()) {
      return;
    }
    try {
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
      CriteriaDelete<CisUser> delete = criteriaBuilder.createCriteriaDelete(CisUser.class);
      Root<CisUser> root = delete.from(CisUser.class);
      delete.where(root.in(users));
      Query query = em.createQuery(delete);
      int cnt = query.executeUpdate();
      LOG.debug("Deleted users: {}", cnt);
    } catch (Exception e) {
      LOG.error("Cleanup failed", e);
    }
  }
}
