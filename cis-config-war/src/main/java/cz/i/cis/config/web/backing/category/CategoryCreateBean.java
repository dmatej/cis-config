package cz.i.cis.config.web.backing.category;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "categoryCreate")
@ViewScoped
public class CategoryCreateBean {

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private String name;


  public String actionAddCategory() throws IOException{
    try{
      ConfigurationItemCategory category = new ConfigurationItemCategory();
        category.setName(name);

      categoryDao.addCategory(category);
//      return "edit?faces-redirect=true&includeViewParams=true&id=" + category.getId();
      FacesUtils.redirect("list.xhtml#category-" + category.getId());
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se přidat novou kategorii: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
