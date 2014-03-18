package cz.i.cis.config.jpa;

import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.Type;

import java.io.Serializable;
import java.lang.String;
import java.util.Collection;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationItemKey
 */
@Entity
@Table(name = "configuration_item_key")
public class ConfigurationItemKey implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private ConfigurationItemCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Type type;

  @Column(nullable = false, unique = true)
  private String key;

  @Column(nullable = false)
  private String description = "";

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "key")
  private Collection<ConfigurationItem> configurationItems;

  private static final long serialVersionUID = 1L;


  public ConfigurationItemKey() {
    super();
  }


  public Long getId() {
    return this.id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public ConfigurationItemCategory getCategory() {
    return this.category;
  }


  public void setCategory(ConfigurationItemCategory idCategory) {
    this.category = idCategory;
  }


  public Type getType() {
    return this.type;
  }


  public void setType(Type type) {
    this.type = type;
  }


  public String getKey() {
    return this.key;
  }


  public void setKey(String key) {
    this.key = key;
  }


  public String getDescription() {
    return this.description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public Collection<ConfigurationItem> getConfigurationItems() {
    return configurationItems;
  }


  public void setConfigurationItems(Collection<ConfigurationItem> configurationItems) {
    this.configurationItems = configurationItems;
  }

}
