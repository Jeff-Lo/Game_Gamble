import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HintFrame extends JFrame {
	private ImageIcon graphDG1, graphDG2, graphDG3, graph21, graph99;
	private JLabel pic;
	private int page;

	public HintFrame() {
		JPanel panel = new JPanel();
		graphDG1 = imageResize("DG1.jpg");
		graphDG2 = imageResize("DG2.jpg");
		graphDG3 = imageResize("DG3.jpg");
		graph21 = imageResize("21.jpg");
		graph99 = imageResize("99.jpg");
		pic = new JLabel(graphDG1);
		page = 1;
		JButton nextOne = new JButton("Next Page");
		class NextListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				pic.setIcon(changePage("next"));
			}
		}
		nextOne.addActionListener(new NextListener());
		JButton lastOne = new JButton("Last Page");
		class LastListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				pic.setIcon(changePage("last"));
			}
		}
		lastOne.addActionListener(new LastListener());
		panel.setLayout(new BorderLayout());
		panel.add(pic, BorderLayout.CENTER);
		panel.add(lastOne, BorderLayout.WEST);
		panel.add(nextOne, BorderLayout.EAST);
		setSize((int) 33.87 * 25, (int) 19.05 * 25);
		setTitle("Hint");
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(panel);
	}

	public ImageIcon changePage(String nextOrLast) {
		switch (page) {
		case 1:
			if (nextOrLast.equals("next")) {
				page++;
				return graphDG2;
			}
			if (nextOrLast.equals("last")) {
				page = 5;
				return graph99;
			}
			break;
		case 2:
			if (nextOrLast.equals("next")) {
				page++;
				return graphDG3;
			}
			if (nextOrLast.equals("last")) {
				page--;
				return graphDG1;
			}
			break;
		case 3:
			if (nextOrLast.equals("next")) {
				page++;
				return graph21;
			}
			if (nextOrLast.equals("last")) {
				page--;
				return graphDG2;
			}
			break;
		case 4:
			if (nextOrLast.equals("next")) {
				page++;
				return graph99;
			}
			if (nextOrLast.equals("last")) {
				page--;
				return graphDG3;
			}
			break;
		case 5:
			if (nextOrLast.equals("next")) {
				page = 1;
				return graphDG1;
			}
			if (nextOrLast.equals("last")) {
				page--;
				return graph21;
			}
			break;
		}
		return null;
	}

	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(graph.getIconWidth()/2, graph.getIconHeight()/2, java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
}
