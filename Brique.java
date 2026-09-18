//package cassebriques;

import java.awt.Color;
import java.awt.Graphics2D;

class Brique {
  private final int SIMPLE = 0;
  protected int largeur, hauteur, x, y;
  protected Color couleur;
  protected boolean isdetruite;
  protected dropObject contiensObjet;

  public Brique() {
    largeur = 17;
    hauteur = 14;
    couleur = Color.GREEN;
    isdetruite = false;
    contiensObjet = null;
  }

  public int choc() {
    // Si la brique n'est pas déjà détruite
    if (!isdetruite) {
      isdetruite = true;
    }
    return SIMPLE;
  }

  public void dessine(Graphics2D motif) {
    // Dessin de la brique
    motif.setColor(couleur);
    motif.fillRect(x, y, largeur, hauteur);
  }

  public int getLargeur() {
    return largeur;
  }

  public int getHauteur() {
    return hauteur;
  }

  public void setX(int x) {
    this.x = x;
    contiensObjet.setX(x);
  }

  public void setY(int y) {
    this.y = y;
    contiensObjet.setY(y);
  }

  public boolean getIsDetruite() {
    return isdetruite;
  }

  public Object getObjet() {
    return contiensObjet;
  }
}
