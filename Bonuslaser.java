import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Bonuslaser extends Bonus {
	public Bonuslaser() {
		super();
		effect = 5;
	}

	public void dessine(Graphics2D motif) {
		Image image = new ImageIcon(
				getClass().getResource(
						"ressources/images/BonusLaser.png"))
				.getImage();
		motif.drawImage(image, x, y, 16, 16, null);
	}
}
