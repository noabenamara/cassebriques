import java.awt.Color;

public class BriqueBouleBonus extends Brique {
  private Boule contiensBoule;

  public BriqueBouleBonus() {
    super();
    couleur = Color.ORANGE;

    contiensBoule = new Boule();
    contiensBoule.setX(getX() / 2);
    contiensBoule.setY(getY() / 2);
    contiensBoule.angleDep(90);

    setObjet(contiensBoule);
  }
}
