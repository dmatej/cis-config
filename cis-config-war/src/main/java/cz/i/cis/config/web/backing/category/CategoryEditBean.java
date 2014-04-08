package cz.i.cis.config.web.backing.category;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "categoryEdit")
@ViewScoped
public class CategoryEditBean {

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Integer id;

  private ConfigurationItemCategory category;

  private String name;


  public void init(){
    category = categoryDao.getCategory(id);

    if(category != null){
      name = category.getName();
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Zvolená kategorie nebyla nalezena v databázi - ID = " + id);
    }
  }

  public String actionUpdateCategory(){
    if(category != null){
      try{
        category.setName(name);

        category = categoryDao.updateCategory(category);
//        FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Změny byly uloženy.");
        FacesUtils.redirect("list.xhtml#category-" + category.getId());
      }
      catch(Exception e){
        FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se uložit změny: " + FacesUtils.getRootMessage(e));
      }
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Musíte editovat existující kategorii, abyste mohli uložit její změny.");
    }
    return null;  //stay on the same page to display the messages
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
