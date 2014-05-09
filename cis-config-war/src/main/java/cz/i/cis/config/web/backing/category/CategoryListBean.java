package cz.i.cis.config.web.backing.category;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "categoryList")
@ViewScoped
public class CategoryListBean {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryListBean.class);

  @EJB
  private ConfigurationCategoryDao categoryDao;


  public List<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return categoryDao.listCategories();
  }


  public void actionDeleteCategory(ConfigurationItemCategory toDelete) {
    LOG.debug("actionDeleteCategory(toDelete={})", toDelete);
    try {
      categoryDao.removeCategory(toDelete);
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat kategorii", FacesUtils.getRootMessage(exc));
    }
  }
}
