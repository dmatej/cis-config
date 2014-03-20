package cz.i.cis.config.jpa;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationItem
 */
@Entity
@Table(name = "configuration_item")
public class ConfigurationItem implements Serializable {

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "key_id", nullable = false)
	private ConfigurationItemKey key;

	@Column(name = "item_value", nullable = false)
	private String value;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date update;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private CisUser user;

	private static final long serialVersionUID = 1L;

	public ConfigurationItem() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConfigurationItemKey getKey() {
		return key;
	}

	public void setKey(ConfigurationItemKey key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getUpdate() {
		return update;
	}

	public void setUpdate(Date update) {
		this.update = update;
	}

	public CisUser getUser() {
		return user;
	}

	public void setUser(CisUser user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((update == null) ? 0 : update.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());

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
		if (!(obj instanceof ConfigurationItem)) {
			return false;
		}

		ConfigurationItem other = (ConfigurationItem) obj;
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

		if (update == null) {
			if (other.update != null) {
				return false;
			}
		} else if (!update.equals(other.update)) {
			return false;
		}

		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}

		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}

		return true;
	}
}
