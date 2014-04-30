package cz.i.cis.config.web.backing.category;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "categoryEdit")
@ViewScoped
public class CategoryEditBean {

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Integer id;

  private ConfigurationItemCategory category;

  private String name;


  public void init() {
    category = categoryDao.getCategory(id);

    if (category != null) {
      name = category.getName();
    } else {
      FacesMessagesUtils.addErrorMessage("Zvolená kategorie nebyla nalezena v databázi - ID = " + id, null);
    }
  }


  public String actionUpdateCategory() {
    if (category != null) {
      String link = "";
      try {
        category.setName(name);
        category = categoryDao.updateCategory(category);

        link = "list.xhtml#category-" + category.getId();
        FacesUtils.redirect(link);
      } catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      } catch (Exception exc) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(exc));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující kategorii, abyste mohli uložit její změny.", null);
    }
    return null; // stay on the same page to display the messages
  }


  public Integer getId() {
    return id;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }
}
