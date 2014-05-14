package cz.i.cis.config.jpa;

/**
 * Enumeration which determines type of configuration item key.
 *
 * @author David Matějček
 * @author Mr.FrAnTA (Michal Dékány)
 */
public enum ConfigurationItemKeyType {
  /** Enumeration constant for text type of configuration item key. */
  Text("Text"),
  /** Enumeration constant for URL type of configuration item key. */
  URL("URL"),
  /** Enumeration constant for path type of configuration item key. */
  Path("Cesta"),
  /** Enumeration constant for date type of configuration item key. */
  Date("Datum"),
  /** Enumeration constant for integer type of configuration item key. */
  Integer("Číslo");

  /** Description for configuration item key type. */
  private String description;


  /**
   * Private constructor for this enumerate fields.
   *
   * @param description
   */
  private ConfigurationItemKeyType(String description) {
    this.description = description;
  }


  /**
   * Returns description of this configuration item key type. This method returns
   * same result as {@link #toString()}.
   *
   * @return Description of this configuration item key type.
   */
  public String getDescription() {
    return description;
  }


  /**
   * Returns description of this configuration item key type as string value of
   * this enumerate.
   *
   * @return Description of onfiguration item key type.
   */
  @Override
  public String toString() {
    return description;
  }


  /**
   * Searches configuration item key type with entered value.
   *
   * @param value value which will be searched.
   * @return Found configuration item key type or {@code null}.
   */
  public static ConfigurationItemKeyType getTypeByValue(String value) {
    for (ConfigurationItemKeyType type : values()) {
      if (type.getDescription().equals(value)) {
        return type;
      }
    }

    return null;
  }
}
