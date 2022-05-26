import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame{
	private final int FRAME_WIDTH = (int)33.87*25;
	private final int FRAME_HEIGHT = (int)19.05*25;
	private final int GRAPH_HEIGHT = (int)11.47*25;
	private final int GRAPH_WIDTH = (int)9.83*25;

	private JButton blackJackBtn,dragonGateBtn, ninetyNineBtn;
	
	public MainFrame() {
		setTitle("Choose Game!");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		// 3 game panels here		
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(1, 3));
		ninetyNineBtn = new JButton("Ninety-Nine");
		JPanel ninetyNinePanel = new JPanel();
		ninetyNinePanel.add(new JLabel(imageResize("J_spade.jpg")));
		ninetyNinePanel.add(ninetyNineBtn);
		JPanel blackJackPanel=new JPanel();
		blackJackPanel.add(new JLabel(imageResize("Q_spade.jpg")));
		blackJackBtn = new JButton("Black Jack");
		blackJackPanel.add(blackJackBtn);
		JPanel dragonGatePanel = new JPanel();
		dragonGateBtn = new JButton("Dragon Gate");
		dragonGatePanel.add(new JLabel(imageResize("K_spade.jpg")));
		dragonGatePanel.add(dragonGateBtn);
		
		// Action Listeners for buttons
		class NinetyNineListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				NinetyNineFrame ninetyNine=new NinetyNineFrame();
				ninetyNine.setVisible(true);	
				dispose();
			}
		}
		ninetyNineBtn.addActionListener(new NinetyNineListener());
		
		class BlackJackListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				BlackJackFrame blackJack=new BlackJackFrame();
				blackJack.setVisible(true);
				dispose();
			}
		}
		blackJackBtn.addActionListener(new BlackJackListener());
		
		class DragonGateListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				DragonGateFrame dragonGate = new DragonGateFrame();
				dragonGate.setVisible(true);
				dispose();
			}
		}
		dragonGateBtn.addActionListener(new DragonGateListener());	
		
		panel.add(new UserInfoPanel(), BorderLayout.NORTH);
		gamePanel.add(ninetyNinePanel);
		gamePanel.add(blackJackPanel);
		gamePanel.add(dragonGatePanel);
		panel.add(gamePanel);
		panel.setVisible(true);
		add(panel);
	}
	
	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
		Image graphResized = graph.getImage().getScaledInstance(GRAPH_WIDTH, GRAPH_HEIGHT,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
}
