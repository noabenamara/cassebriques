import java.awt.Color;

public class BriqueBouleBonus extends Brique {
  private Boule contiensBoule;

  public BriqueBouleBonus() {
    super();
    couleur = Color.ORANGE;
    
    contiensBoule = new Boule();
    
    contiensBoule.setX(x/2);
    contiensBoule.setY(y/2);
    contiensBoule.angleDep(90);

    contiensObjet = contiensBoule;
  }
}
