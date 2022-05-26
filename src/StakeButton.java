import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class StakeButton extends JButton {
	private String label;
	public StakeButton(String label) {
		super(label);
		this.label = label;
		// 這些宣告把按鈕擴充套件為一個圓而不是一個橢圓
		Dimension size = getPreferredSize();
		size.setSize(Math.max(size.width, size.height), Math.max(size.width, size.height));
		setPreferredSize(size);
		// 這個呼叫使JButton不畫背景，而允許我們畫一個圓的背景
		setContentAreaFilled(false);
	}

	// 畫圓的背景和標籤
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			switch (label) {
			case "10":
				g.setColor(Color.RED);
				break;
			case "50":
				g.setColor(Color.cyan);
				break;
			case "100":
				g.setColor(Color.lightGray);
				break;
			case "500":
				g.setColor(Color.green);
				break;
			}
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
		// 這個呼叫會畫一個標籤和焦點矩形。
		super.paintComponent(g);
	}

	// 用簡單的弧畫按鈕的邊界
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	// 偵測點選事件
	Shape shape;

	public boolean contains(int x, int y) {
		// 如果按鈕改變大小，產生一個新的形狀物件
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}