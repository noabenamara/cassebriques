//package cassebriques;

import java.awt.*;

class Barre {
  // x es la coordonnée horizontale du centre de la barre
  // et y du haut de la barre
  private int x, y, miLargeur, hauteur;
  private Color couleur;

  // Largeur de référence (valeur d'origine) et largeur minimale autorisée
  private final int MI_LARGEUR_DEFAUT=25;
  private final int MI_LARGEUR_MIN=8;

  public Barre() {
    x=175;
    y=310;
    miLargeur=MI_LARGEUR_DEFAUT;
    hauteur=9;
    couleur = Color.blue;
  }

  // Réduit la demi-largeur de la barre, sans descendre sous le minimum
  public void retrecit(int valeur) {
    miLargeur = Math.max(MI_LARGEUR_MIN, miLargeur - valeur);
  }

  // Remet la barre à sa largeur d'origine (utilisé par la brique rose)
  public void reinitialiseLargeur() {
    miLargeur = MI_LARGEUR_DEFAUT;
  }

  public int getX() {
      return x;
  }

  public void setX(int newVal) {
       x=newVal;
  }

  public int getY() {
      return y;
  }

  public int getMiLargeur() {
      return miLargeur;
  }

  public void setMiLargeur(int newVal) {
       miLargeur=newVal;
  }

  public int getHauteur() {
      return hauteur;
  }

  public Color getCouleur() {
      return couleur;
  }

  public void dessine(Graphics2D motif) {
    motif.setColor(couleur);
    motif.fillRect(x-miLargeur,y,miLargeur*2,hauteur);
  }
 }
