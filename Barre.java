//package cassebriques;

import java.awt.Color;
import java.awt.Graphics2D;

class Barre {
  // x es la coordonnée horizontale du centre de la barre
  // et y du haut de la barre
  private int miLargeur, hauteur, x, y;
  private Color couleur;

  public Barre() {
    x = 175;
    y = 310;
    miLargeur = 25;
    hauteur = 9;
    couleur = Color.BLUE;
  }

  public void dessine(Graphics2D motif) {
    motif.setColor(couleur);
    motif.fillRect(x - miLargeur, y, miLargeur * 2, hauteur);
  }

  public int getMiLargeur() {
    return miLargeur;
  }

  public void setMiLargeur(int miLargeur) {
    this.miLargeur = miLargeur;
  }

  public int getHauteur() {
    return hauteur;
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
}
