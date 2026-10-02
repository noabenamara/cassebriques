import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class bonusVie extends Bonus {
    public bonusVie() {
        super();
        effect = 3;
    }

    public void dessine(Graphics2D motif) {
        Image image = new ImageIcon(
                getClass().getResource(
                        "ressources/images/BonusVie.png"))
                .getImage();
        motif.drawImage(image, x, y, 16, 16, null);
    }
}
