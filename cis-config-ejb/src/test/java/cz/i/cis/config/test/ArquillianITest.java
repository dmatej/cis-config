/**
 *
 */
package cz.i.cis.config.test;

import org.apache.log4j.PropertyConfigurator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Matějček
 */
@RunWith(Arquillian.class)
public abstract class ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ArquillianITest.class);

  private static EnterpriseArchive ear;
  private static boolean alreadyInitialized;


  /**
   * Initialize test class.
   *
   * @return {@link JavaArchive} to deploy to the container.
   * @throws Exception exception
   */
  @Deployment
  public static EnterpriseArchive getEar() throws Exception {
    if (alreadyInitialized) {
      LOG.trace("Already initialized, returning.");
      return ear;
    }

    // do not try it again - if it fails, it should fail again.
    alreadyInitialized = true;

    JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "cis-config-test-ejb.jar").addPackages(true, "cz.i.cis.config")
        .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    LOG.info("{}", jar.toString(true));

    ear = ShrinkWrap.create(EnterpriseArchive.class, "cis-config-test.ear").addAsModule(jar);
    return ear;
  }
}
