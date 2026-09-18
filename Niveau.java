import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class Niveau {
    // Taille du mur : 10 lignes x 20 colonnes
    private final int LIGNES = 10;
    private final int COLONNES = 20;

    // Représente le mur de briques sous forme de tableau 2D
    // mur[ligne][colonne]
    private Brique[][] mur = new Brique[10][20];

    // Liste simplifiée de toutes les briques du niveau
    // Utile pour parcourir le niveau plus facilement
    private ArrayList<Brique> briques;

    // Nombre de briques encore vivantes
    private int nbBriques;

    // Constructeur : crée un niveau vide puis le construit
    public Niveau() {
        briques = new ArrayList<>();
        construit();
    }

    // Génère un mur aléatoire de briques
    // Chaque case reçoit un type de brique différent selon le hasard
    public void construit() {
        briques.clear();
        Random random = new Random();

        for (int l = 0; l < LIGNES; l++) {
            for (int c = 0; c < COLONNES; c++) {
                switch (random.nextInt(6)) {
                    case 1:
                        mur[l][c] = new BriqueRetourNorme();
                        break;
                    case 2:
                        mur[l][c] = new BriqueBouleRapide();
                        break;
                    case 3:
                        mur[l][c] = new BriqueRenforcee();
                        break;
                    case 4:
                        mur[l][c] = new BriqueVie();
                        break;
                    case 5:
                        mur[l][c] = new BriqueBouleBonus();
                        break;
                    default:
                        mur[l][c] = new Brique();
                }

                // Positionnement de la brique dans l'espace de jeu
                mur[l][c].setX(c * (mur[l][c].getLargeur() + 1));
                mur[l][c].setY(l * (mur[l][c].getHauteur() + 1));

                // Ajout dans la liste globale
                briques.add(mur[l][c]);
            }
        }

        // Le nombre total de briques correspond au nombre d'éléments dans la liste
        nbBriques = briques.size();
    }

    // Ajoute une brique manuellement à une position donnée
    // Cette méthode est utile si on veut créer un niveau précis à la main
    public void ajouterBrique(Brique b, int x, int y) {
        b.setX(x);
        b.setY(y);
        briques.add(b);
        nbBriques = briques.size();
    }

    // Dessine uniquement les briques non détruites
    public void dessine(Graphics2D motif) {
        for (int l = 0; l < LIGNES; l++) {
            for (int c = 0; c < COLONNES; c++) {
                if (mur[l][c] != null && !mur[l][c].getIsDetruite()) {
                    mur[l][c].dessine(motif);
                }
            }
        }
    }

    // Retourne la liste des briques du niveau
    public ArrayList<Brique> getBriques() {
        return briques;
    }

    // Vérifie si le niveau est terminé : toutes les briques sont détruites
    public boolean estTermine() {
        return nbBriques == 0;
    }

    // Niveau de test simple : mur plein de briques standards
    // C'est pratique pour tester le jeu sans générer un niveau aléatoire
    public void chargerNiveauTest() {
        briques.clear();

        for (int l = 0; l < 10; l++) {
            for (int c = 0; c < 20; c++) {
                mur[l][c] = new Brique();
                mur[l][c].setX(c * (mur[l][c].getLargeur() + 1));
                mur[l][c].setY(l * (mur[l][c].getHauteur() + 1));
                briques.add(mur[l][c]);
            }
        }

        nbBriques = briques.size();
    }

    // Détermine si une brique existe à la position demandée et n'est pas détruite
    public boolean percute(int l, int c) {
        if (l < 0 || l >= LIGNES || c < 0 || c >= COLONNES) {
            return false;
        }
        if (mur[l][c] == null || mur[l][c].getIsDetruite()) {
            return false;
        }
        return true;
    }

    // Applique un choc sur une brique précise
    // Retourne la conséquence du choc (simple, rapide, bonus, etc.)
    public int casse(int l, int c) {
        int consequence = 0;

        if (l >= 0 && l < LIGNES && c >= 0 && c < COLONNES
                && mur[l][c] != null && !mur[l][c].getIsDetruite()) {
            consequence = mur[l][c].choc();

            // Si la brique a été détruite, on enlève une vie au compteur
            if (mur[l][c].getIsDetruite()) {
                nbBriques--;
            }
        }

        return consequence;
    }

    // Nombre de briques restantes dans le niveau
    public int getNbBriques() {
        return nbBriques;
    }

    // Taille d'une brique pour la logique de collision
    public int getLargeurBrique() {
        return mur[0][0].getLargeur();
    }

    public int getHauteurBrique() {
        return mur[0][0].getHauteur();
    }

    // Accès direct à une brique donnée
    public Brique getBrique(int l, int c) {
        return mur[l][c];
    }
}