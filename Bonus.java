import java.awt.Color;
import java.awt.Graphics2D;

public class Bonus extends dropObject {
    private int vitesse, largeur, hauteur;
    protected Color couleur;
    protected boolean isdetruite;
    protected int effect;

    public Bonus() {
        largeur = 17;
        hauteur = 14;
        vitesse = 1;
        couleur = Color.BLACK;
        effect = 0;
    }

    public void deplace() {
        y += vitesse;
    }

    public boolean getIsDetruite() {
        return isdetruite;
    }

    public void dessine(Graphics2D motif) {
        motif.setColor(couleur);
        motif.fillRect(x, y, largeur, hauteur);
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getEffect() {
        return effect;
    }
}
