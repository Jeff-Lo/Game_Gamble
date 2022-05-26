import java.awt.Dimension;
import java.awt.Image;
import javax.swing.*;

public class Card extends JButton {
	private final int CARD_WIDTH = (int) 4.25 * 25;
	private final int CARD_HEIGHT = (int) 6.11 * 25;
	private int id;
	private String cardName;
	private boolean isClicked;
	
	public Card(int id) {
		this.id = id;
		this.cardName = id2Card(this.id);
		this.isClicked = false;		
		setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		setIcon(imageResize(this.cardName + ".jpg"));
	}

	public void setClicked() {
		this.isClicked = true;
	}
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getCardName() {
		return this.cardName;
	}
	
	public void setCardName() {
		this.cardName = id2Card(this.id);
	}

	public boolean getClicked() {
		return this.isClicked;
	}
	
	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
	
	public String id2Card(int id) {
		String card_name = "";
		
		// id == -1 為卡背
		if (id == -1) {
			return "cardBack";
		}
		int id_change = id % 13 + 1;
		if (id_change == 1) {
			card_name += "A";
		}
		else if (id_change == 11) {
			card_name += "J";
		}
		else if (id_change == 12) {
			card_name += "Q";
		}
		else if (id_change == 13) {
			card_name += "K";
		}
		else {
			card_name += "" + id_change;
		}
		
		if (0 <= id && id <= 12) {
			card_name += "_spade";
		} else if (13 <= id && id <= 25) {
			card_name += "_heart";
		} else if (26 <= id && id <= 38) {
			card_name += "_diamond";
		} else {
			card_name += "_club";
		}
		return card_name;
	}
	
}
