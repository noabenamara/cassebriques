import java.awt.Color;
import java.awt.Graphics2D;

public class Laser{
    private int largeur, hauteur, vitesse, x, y;
    Color couleur;

    public Laser(int x, int y) {
        largeur = 2;
        hauteur = 20;
        vitesse = 5;
        couleur = Color.red;
        this.x = x;
        this.y = y;

    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int height) {
        this.largeur = height;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void deplace() {
        y -= vitesse;
    }

    public void dessine(Graphics2D motif) {
        motif.setColor(couleur);
        motif.fillRect(x, y, largeur, hauteur);
    }
}
