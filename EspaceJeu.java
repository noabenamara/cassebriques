//package cassebriques;

import java.awt.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class EspaceJeu extends JPanel implements Runnable, MouseListener,
    MouseMotionListener {

  // valeur par défaut du joueur
  private final int VIE = 3;
  private final int VAGUE = 1;

  // délai entre 2 déplacements
  private final int DELAI = 16;

  // Constantes rattachées aux phases de jeu
  private final int ATTEND = 1;
  private final int ROULE = 2;
  private final int SORT = 3;
  private final int GAGNE = 4;

  // Constantes rattachées aux types de briques
  private final int SIMPLE = 0;
  private final int NORME = 1;
  private final int RAPIDE = 2;
  private final int BONUS_VIE = 3;
  private final int laser = 5;

  // Champs d'instance
  private Thread action;
  private boolean fini;
  private int phase;
  private int delai;
  private Barre barre;
  private Mur mur;
  private int vie;
  private int vague;
  private Clip backgroudSound;
  private Boolean laserBonus;

  // liste des nouveaux objet actif sur l’écran
  private List<Bonus> Lbonus;
  private List<Boule> Lboule;
  private List<Laser> Llaser;

  public EspaceJeu() {

    // Création de la barre
    barre = new Barre();

    // Délai entre 2 déplacements
    delai = DELAI;

    // phase d'attente
    phase = ATTEND;

    // vie du joueur
    vie = VIE;

    // nombre de vague
    vague = VAGUE;

    // objet en jeu
    Lbonus = new ArrayList<>();
    Lboule = new ArrayList<>();
    Llaser = new ArrayList<>();

    laserBonus = false;

    // Création de la boule
    Lboule.add(new Boule());

    // Gestion des événement liés à la souris
    addMouseMotionListener(this);
    addMouseListener(this);
  }

  public void initialiseNiveau() {

    // Arrêt du thread action s'il est en cours d'exécution.
    fini = true;
    if (action != null) {
      while (action.isAlive())
        ;
    }

    // Création du mur de brique
    if (mur == null) {
      mur = new Mur();
    }
    mur.construit();
    // première phase du jeu
    phase = ATTEND;
    delai = DELAI;

    // vie du joueur
    vie = VIE;

    // nombre de vague
    vague = VAGUE;

    // objet en jeu
    Lbonus = new ArrayList<>();
    Lboule = new ArrayList<>();
    Llaser = new ArrayList<>();


    laserBonus = false;

    // Création de la boule
    Lboule.add(new Boule());

    try {
      if (backgroudSound == null || !backgroudSound.isRunning())
        backgroudSound = playRandomBackgroundSound();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Lancement de l'exécution du jeu dans un thread
    action = new Thread(this);
    action.start();
  }

  public void changeNiveaux() {
    vague++;
    mur.construit();
    phase = ATTEND;
    delai = DELAI;
    vie = VIE;
    laserBonus = false;
    Lbonus.clear();
    Lboule.clear();
    Llaser.clear();
    Lboule.add(new Boule());
  }

  // Traitement central exécuté avec une périodicité précise
  public void run() {
    try {
      if (backgroudSound == null || !backgroudSound.isRunning())
        backgroudSound = playRandomBackgroundSound();
    } catch (Exception e) {
      e.printStackTrace();
    }
    fini = false;
    vie = VIE;
    while (!fini) {
      // Selon la phase du jeu ...
      switch (phase) {
        // Attente de lancement de la boule
        case ATTEND:
          // Placement de la boule au milieu de la barre
          Lboule.getFirst().setX(barre.getX());
          Lboule.getFirst().setY(barre.getY() - Lboule.getFirst().getRayon());
          break;

        // La boule roule
        case ROULE:
          new ArrayList<>(Lboule).forEach(boule -> {
            boule.deplace();
            bordsCollisionBoule(boule);
            rebondBarre(boule);
            rebondBriqueBoule(boule);
          });
          isWin();
          break;

        case SORT:
          try {
            playClip("ressources/SoundEffect/Game/finishGame.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
          showMessageDialog("C'est perdu !", "Casse briques");
          fini = true;
          break;

        case GAGNE:
          try {
            playClip("ressources/SoundEffect/Game/finishGame.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
          showMessageDialog("Bravo, vous avez fini la vague " + vague, "Casse briques");
          changeNiveaux();
          break;
      }
      BonusManager();
      laserManager();

      // on redessine l'espace de jeu
      repaint();

      try {
        Thread.sleep(delai);
      } catch (InterruptedException e) {
      }
    }
  }

  public void bordsCollisionBoule(Boule boule) {
    if (boule.getX() < boule.getRayon()) {
      // Rebond sur le bord gauche
      boule.chocH();
      try {
        playClip("ressources/SoundEffect/Game/rebondBoule.wav");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      // Rebond sur le bord droit
      if (boule.getX() > getSize().width - boule.getRayon()) {
        boule.chocH();
        try {
          playClip("ressources/SoundEffect/Game/rebondBoule.wav");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    if (boule.getY() < boule.getRayon()) {
      // Rebond sur le haut
      boule.chocV();
      boule.setX(boule.getX());
      boule.setY(boule.getRayon());
      try {
        playClip("ressources/SoundEffect/Game/rebondBoule.wav");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // Si la boule touche le fond ...
    if (boule.getY() > getSize().height - boule.getRayon()) {
      if (Lboule.size() <= 1) {
        vie--;
        phase = (vie <= 0) ? SORT : ATTEND;
      } else
        Lboule.remove(boule);
    }
  }

  public void rebondBarre(Boule boule) {
    if (boule.getRayon() <= barre.getY()
        && boule.getY() + boule.getRayon() >= barre.getY()
        &&
        boule.getX() - boule.getRayon() < barre.getX() + barre.getMiLargeur()
        &&
        boule.getX() + boule.getRayon() > barre.getX() - barre.getMiLargeur()) {
      // Rebond sur la barre
      // Le rebond dépend de la zone de la barre touchée
      // Rebond sur la barre
      int impact = boule.getX() - barre.getX();
      boule.chocV();
      try {
        playClip("ressources/SoundEffect/Game/rebondBoule.wav");
      } catch (Exception e) {
        e.printStackTrace();
      }
      // La barre est divisée en 5 parties. Chaque partie provoque un rebond différent
      // Partie extrême gauche : Augmentation de l'angle de 30 degrés
      if (impact < -(barre.getMiLargeur() * 0.6))
        boule.modifAngle(30);
      else
      // Partie suivante : Augmentation de l'angle de 15 degrés
      if (impact < -(barre.getMiLargeur() * 0.2))
        boule.modifAngle(15);

      // Partie extreme droite : Diminution de l'angle de 30 degrés
      if (impact > (barre.getMiLargeur() * 0.6))
        boule.modifAngle(-30);
      else
      // Partie précédente : Diminution de l'angle de 15 degrés
      if (impact > (barre.getMiLargeur() * 0.2))
        boule.modifAngle(-15);

      // La partie centrale de la barre provoque un rebond normal
      boule.setY(barre.getY() - boule.getRayon());
    }
  }

  public void rebondBriqueBoule(Boule boule) {
    // Gestion du choc avec une brique
    // Récupération de la hauteur d'une brique
    int hauteur = mur.getHauteurBrique();
    // Récupération de la largeur d'une brique
    int largeur = mur.getLargeurBrique();
    // Si la boule se trouve dans la zone du mur de briques ...
    if (boule.getY() - boule.getRayon() < 10 * (hauteur + 1)) {
      // l1, c1 sont les coordonnées du coin supérieur gauche de la boule
      // l2, c2 sont les coordonnées du coin inférieur droit de la boule
      int l1, l2, c1, c2;
      l1 = (int) ((boule.getY() - boule.getRayon()) / (hauteur + 1));
      l2 = (int) ((boule.getY() + boule.getRayon()) / (hauteur + 1));
      c1 = (int) ((boule.getX() - boule.getRayon()) / (largeur + 1));
      c2 = (int) ((boule.getX() + boule.getRayon()) / (largeur + 1));

      // Le rebond dépend des coins (1 ou 2) en contact avec une brique
      // Coin supérieur gauche ...
      if (mur.percute(l1, c1)) {
        // et coin supérieur droit
        if (mur.percute(l1, c2)) {
          // Choc vertical
          boule.chocV();
          try {
            playClip("ressources/SoundEffect/Game/rebondBoule.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          // et coin inférieur gauche
          boule.chocH();
          if (!mur.percute(l2, c1))
            // Choc vertical
            boule.chocV();
          try {
            playClip("ressources/SoundEffect/Game/rebondBoule.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } else {
        // Coin supérieur droit ...
        if (mur.percute(l1, c2)) {
          // et coin inférieur droit
          boule.chocH();
          if (!mur.percute(l2, c2))
            // Choc vertical
            boule.chocV();
          try {
            playClip("ressources/SoundEffect/Game/rebondBoule.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          // Coin inférieur gauche ...
          if (mur.percute(l2, c1)) {
            // et coin inférieur droit
            boule.chocV();
            if (!mur.percute(l2, c2))
              // Choc horizontal
              boule.chocH();
            try {
              playClip("ressources/SoundEffect/Game/rebondBoule.wav");
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            // Coin inférieur droit
            if (mur.percute(l2, c2)) {
              // Double choc
              boule.chocV();
              boule.chocH();
              try {
                playClip("ressources/SoundEffect/Game/rebondBoule.wav");
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
      // Casse effective des brique du mur
      // (et mise en place des conséquences)
      // Frappe la brique 1 seul fois si 2 coins de la boule la touche
      // pour éviter que briqueRenforcee se fassent toucher plusieurs fois en une
      // iteration
      // voir coin_brique.png:
      // ainsi que le spawn des bonus
      for (int[] lc : new int[][] { { l1, c1 }, { l1, c2 }, { l2, c1 }, { l2, c2 } }) {
        if (mur.percute(lc[0], lc[1])) {
          modifJeu(mur.casse(lc[0], lc[1]));
          dropManager(lc[0], lc[1]);
        }
      }
    }
  }

  public void dropManager(int l, int c) {
    if (mur.getBrique(l, c).getObjet() instanceof Bonus)
      Lbonus.add((Bonus) mur.getBrique(l, c).getObjet());
    if (mur.getBrique(l, c).getObjet() instanceof Boule)
      Lboule.add((Boule) mur.getBrique(l, c).getObjet());
  }

  public void BonusManager() {
    List<Bonus> removable_bonus = new ArrayList<>();
    for (Bonus bonus : Lbonus) {
      bonus.deplace();
      if (bonus.getX() - bonus.getLargeur() < barre.getX() + barre.getMiLargeur()
          &&
          bonus.getX() + bonus.getLargeur() > barre.getX() - barre.getMiLargeur()
          &&
          bonus.getY() - bonus.getHauteur() < barre.getY()
          &&
          bonus.getY() + bonus.getHauteur() > barre.getY()) {
        modifJeu(bonus.getEffect());
        removable_bonus.add(bonus);
        try {
          playClip("ressources/SoundEffect/Game/getBonus.wav");
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (bonus.getY() - bonus.getHauteur() > 310)
        removable_bonus.add(bonus);
    }
    Lbonus.removeAll(removable_bonus);
  }

  private void laserManager() {
    List<Laser> removable_laser = new ArrayList<>();
    for (Laser laser : Llaser) {
      laser.deplace();
      if (collisionBriquelaser(laser) || laser.getY() + laser.getHauteur() / 2 < 0)
        removable_laser.add(laser);
    }
    Llaser.removeAll(removable_laser);
  }

  private boolean collisionBriquelaser(Laser laser) {
    int hauteur = mur.getHauteurBrique();
    int largeur = mur.getLargeurBrique();

    for (int l = 0; l < 10; l++) {
      for (int c = 0; c < 20; c++) {
        if (!mur.percute(l, c))
          continue;

        int xBrique = c * (largeur + 1);
        int yBrique = l * (hauteur + 1);

        if (laser.getX() < xBrique + largeur
            && laser.getX() + laser.getLargeur() > xBrique
            && laser.getY() < yBrique + hauteur
            && laser.getY() + laser.getHauteur() > yBrique) {
          mur.casse(l, c);
          return true;
        }
      }
    }
    return false;
  }

  public void modifJeu(int action) {
    switch (action) {
      case NORME:
        // Retour aux valeurs de base
        delai = DELAI;
        break;

      case RAPIDE:
        // Accélération du traitement
        delai = (int) (DELAI / 2);
        break;
      case BONUS_VIE:
        vie++;
        break;
      case laser:
        if (!laserBonus) {
          laserBonus = true;
          try {
            playClip("ressources/SoundEffect/Game/getBonusLaser.wav");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
  }

  void lanceBoule(int angle, Boule boule) {
    if (phase == ATTEND) {
      phase = ROULE;
      boule.angleDep(angle);
      try {
        playClip("ressources/SoundEffect/Game/bouleLaunch.wav");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void isWin() {
    // Si toutes les briques sont cassées ...
    if (mur.getNbBriques() == 0)
      // Le joueur à gagné
      phase = GAGNE;
  }

  public void showMessageDialog(String message, String titre) {
    JOptionPane.showMessageDialog(this, message,
        titre, JOptionPane.INFORMATION_MESSAGE);
  }

  public void paintComponent(Graphics comp) {
    Graphics2D comp2D = (Graphics2D) comp;
    // Effacement de l'espace de jeu
    comp2D.setColor(getBackground());
    comp2D.fillRect(0, 0, getSize().width, getSize().height);
    // Dessin de la barre
    barre.dessine(comp2D);
    // dessine les bonus
    Lbonus.forEach((bonus) -> {
      bonus.dessine(comp2D);
    });
    Lboule.forEach((boule) -> {
      boule.dessine(comp2D);
    });
    Llaser.forEach((laser) -> {
      laser.dessine(comp2D);
    });
    // Dessin du mur de brique
    // Au tout départ le mur n'existe pas
    if (mur != null)
      mur.dessine(comp2D);
  }

  // Méthodes de l'interface MouseMotionListener
  public void mouseMoved(MouseEvent evt) {
    // Si le pointeur est trop à gauche ...
    if (evt.getX() < barre.getMiLargeur())
      // barre contre le bord gauche
      barre.setX(barre.getMiLargeur());
    else
    // Si de pointeur est trop à droite ...
    if (evt.getX() > getSize().width - barre.getMiLargeur())
      // barre contre le bord droit
      barre.setX(getSize().width - barre.getMiLargeur());
    else
      // barre centrée sur le pointeur
      barre.setX(evt.getX());
  }

  public void mouseDragged(MouseEvent evt) {
  }

  // Méthodes de l'interface MouseListener
  public void mouseClicked(MouseEvent evt) {
    lanceBoule((int) (Math.random() * 120) + 30, Lboule.getFirst());
    if (laserBonus) {
      Llaser.add(new Laser(barre.getX(), barre.getY() - barre.getHauteur()));
      try {
        playClip("ressources/SoundEffect/Game/laserSound.wav");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void mouseEntered(MouseEvent evt) {
  }

  public void mouseExited(MouseEvent evt) {
  }

  public void mousePressed(MouseEvent evt) {
  }

  public void mouseReleased(MouseEvent evt) {
  }

  public Clip playClip(String filePath) throws Exception {
    Clip clip = AudioSystem.getClip();
    clip.open(
        AudioSystem.getAudioInputStream(
            new File(filePath).getAbsoluteFile()));
    clip.start();

    return clip;
  }

  private Clip playRandomBackgroundSound() throws Exception {
    File ambianceFolder = new File("ressources/SoundEffect/Ambiance");
    File[] musicFiles = ambianceFolder.listFiles(file -> file.getName().endsWith(".wav"));

    if (musicFiles == null || musicFiles.length <= 0)
      return null;

    File musicFile = musicFiles[new Random().nextInt(0, musicFiles.length)];
    return playClip(musicFile.getPath());
  }
}
