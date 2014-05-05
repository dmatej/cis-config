package cz.i.cis.config.jpa;


public enum Type {
  Text("Text"), URL("URL"), Path("Cesta"), Date("Datum"), Integer("Číslo");

  private String description;


  private Type(String description){
    this.description = description;
  }


  @Override
  public String toString() {
    return description;
  }
}
