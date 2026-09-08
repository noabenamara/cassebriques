//package cassebriques;

import java.awt.Color;

class BriqueRetrecitBarre extends Brique {
  private final int RETRECIT=3;

  public BriqueRetrecitBarre() {
    super();
    couleur=Color.orange;
  }

  public int choc() {
    super.choc();
    return RETRECIT;
  }

}
