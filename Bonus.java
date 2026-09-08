import java.awt.*;

public class Bonus {
    private int x, y, vitesse, largeur, hauteur;
    protected Color couleur;
    protected boolean isdetruite;
    protected int effect;

    public Bonus(){
    largeur=17;
    hauteur=14;
    vitesse=1;
    couleur = Color.BLACK;
    effect=0;
    }

    public void deplace() {
        y+=vitesse;
    }

    public boolean isDetruite() {
        return isdetruite;
    }

    public void dessine(Graphics2D motif){
    motif.setColor(couleur);
    motif.fillRect(x,y,largeur,hauteur);
    }



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
