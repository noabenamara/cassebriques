import java.awt.Color;

class BriqueVie extends Brique {
  public BriqueVie() {
    super();
    couleur = Color.BLUE;
    setObjet(new bonusVie());
  }
}
