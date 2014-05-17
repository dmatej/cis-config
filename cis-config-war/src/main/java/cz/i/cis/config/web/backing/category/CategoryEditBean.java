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

@Named(value = "categoryEdit")
@ViewScoped
public class CategoryEditBean {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryEditBean.class);

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Integer id;

  private ConfigurationItemCategory category;

  private String name;


  public void init() {
    LOG.debug("init()");
    try {
      category = categoryDao.getCategory(id);
    } catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("Category id " + id + " is not valid", "");
    }

    if (category != null) {
      name = category.getName();
    } else {
      FacesMessagesUtils.addErrorMessage("Zvolená kategorie nebyla nalezena v databázi - ID = " + id, "");
    }
  }


  public String actionUpdateCategory() {
    LOG.debug("actionUpdateCategory()");
    if (category != null) {
      String link = "";
      try {
        category.setName(name);
        category = categoryDao.updateCategory(category);

        link = "list.xhtml#category-" + category.getId();
        FacesUtils.redirectToURL(link);
      } catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      } catch (Exception exc) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesMessagesUtils.getRootMessage(exc));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující kategorii, abyste mohli uložit její změny.", "");
    }
    return null; // stay on the same page to display the messages
  }


  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
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
