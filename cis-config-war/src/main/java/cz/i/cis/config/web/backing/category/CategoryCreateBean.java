package cz.i.cis.config.web.backing.category;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "categoryCreate")
@ViewScoped
public class CategoryCreateBean {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryCreateBean.class);

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private String name;


  public String actionAddCategory() throws IOException {
    LOG.debug("actionAddCategory()");
    String link = "";
    try {
      ConfigurationItemCategory category = new ConfigurationItemCategory();
      category.setName(name);
      categoryDao.addCategory(category);

      // return "edit?faces-redirect=true&includeViewParams=true&id=" + category.getId();
      link = "list.xhtml#category-" + category.getId();
      FacesUtils.redirectToURL(link);
    }
    catch (IOException exc) {
      FacesMessagesUtils.failedRedirectMessage(link, exc);
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se přidat novou kategorii", FacesMessagesUtils.getRootMessage(exc));
    }

    return null;
  }


  public String getName() {
    LOG.trace("getName()");
    return name;
  }

  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }
}
