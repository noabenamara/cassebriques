import java.awt.Color;

/**
 * BriqueVie
 */
class BriqueVie extends Brique {

  public BriqueVie() {
    super();
    couleur = Color.BLUE;
    contiensObjet = new bonusVie();
  }
}