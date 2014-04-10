package cz.i.cis.config.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: ConfigurationItemKey
 */
@Entity
@Table(name = "configuration_item_key")
public class ConfigurationItemKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

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


  public ConfigurationItemKey() {
    super();
  }


  public Integer getId() {
    return this.id;
  }


  public void setId(Integer id) {
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


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());

    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof ConfigurationItemKey)) {
      return false;
    }

    ConfigurationItemKey other = (ConfigurationItemKey) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }

    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }

    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }

    if (type != other.type) {
      return false;
    }

    return true;
  }


  @Override
  public String toString() {
    return this.getClass().getCanonicalName() + " id=" + this.getId() + " category_id=" + this.getCategory().getId()
        + ",type=" + this.getType() + ",key=" + this.getKey() + ",description=" + this.getDescription();
  }
}
