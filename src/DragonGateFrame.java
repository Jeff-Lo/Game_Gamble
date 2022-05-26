import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class DragonGateFrame extends JFrame {
	
	private final int FRAME_WIDTH = (int)33.87 * 25;
	private final int FRAME_HEIGHT = 360;
	private final int CARD_WIDTH = (int) 4.25 * 25;
	private final int CARD_HEIGHT = (int) 6.11 * 25;
	private int stakeCount;
	private JLabel gate1, gate2, middle;
	private String gate1id, gate2id, middleid;
	private JLabel stakeCountLabel;
	private GameRule_DG gameRule;
	private UserInfo userInfo;
	private JPanel cardPanel, stakePanel;
	
	public DragonGateFrame(){
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new BorderLayout());
		setTitle("Dragon Gate");
		setLocationRelativeTo(null);
		userInfo = getUserInfoFromLogInFrame();
		gameRule = new GameRule_DG();
		gameRule.setMoney(userInfo.getMoney());

		// Card Panel
		cardPanel = new JPanel();
		
		gameRule.setGates();
		gameRule.sortGates();
		gate1id = gameRule.show(gameRule.getGate(0));
		gate2id = gameRule.show(gameRule.getGate(1));
		gate1 = new JLabel(imageResize(gate1id+".jpg"));
		gate2 = new JLabel(imageResize(gate2id+".jpg"));
		middle = new JLabel(imageResize("cardBack.jpg"));
		cardPanel.add(gate1);
		cardPanel.add(middle);
		cardPanel.add(gate2);
		
		// Stake Panel
		stakePanel = new JPanel();
		stakePanel.setLayout(new BorderLayout());
		stakePanel.add(stakePanel(), BorderLayout.CENTER);
		JButton clearStakeBtn = new JButton("重下注");
		class ClearStake implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stakeCount = 0;
				stakeCountLabel.setText(stakeCount + "");
			}
		}
		clearStakeBtn.addActionListener(new ClearStake());
		stakePanel.add(clearStakeBtn, BorderLayout.WEST);
		JButton noStakeBtn = new JButton("不要下注");
		class noStake implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				int confirmNoStake = JOptionPane.showConfirmDialog(null, "確定不下注嗎?", "確定不下注嗎?", JOptionPane.YES_NO_OPTION);
				if (confirmNoStake == JOptionPane.YES_OPTION) {
					int choice = JOptionPane.showConfirmDialog(null, "還想再玩一次嗎?", "繼續玩或是回到主畫面", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						DragonGateFrame DragonGate = new DragonGateFrame();
						DragonGate.setVisible(true);
						DragonGate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						dispose();
					}
					else {
						MainFrame main = new MainFrame();
						dispose();
					}
				}
				else {
					
				}
			}
		}
		noStakeBtn.addActionListener(new noStake());
		stakePanel.add(noStakeBtn, BorderLayout.EAST);

		add(new UserInfoPanel(), BorderLayout.NORTH);
		add(cardPanel, BorderLayout.CENTER);
		add(stakePanel, BorderLayout.SOUTH);		
	}
	
	public JPanel stakePanel() {
		JButton stakeBtn = new JButton("下注");
		JPanel panel = new JPanel();
		stakeBtn.setContentAreaFilled(false);
		Dimension size = stakeBtn.getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		stakeBtn.setPreferredSize(size);
		stakeBtn.setBackground(Color.orange);
		boolean[] clickedOnce = {true};
		
		class StakeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// 顯示目前下注金額的label
				stakeCountLabel = new JLabel(stakeCount + "");
				// 如果他是第一次點，就會把原本顯示"下注"的按鈕改成顯示"確認"，並且插入下注選項到這個panel，然後把第一次下注的boolean值改成false
				if (clickedOnce[0] == true) {
					panel.add(stakeCountLabel);
					stakeBtn.setText("確定");
					panel.add(stakeChoiceBtn(10));
					panel.add(stakeChoiceBtn(50));
					panel.add(stakeChoiceBtn(100));
					panel.add(stakeChoiceBtn(500));
					clickedOnce[0] = false;
				} 
				// 如果他不是第一次點
				// (也就是他點同樣叫stakeBtn的按鈕，但那時候stakeBtn的按鈕顯示的是確認)，就會跳出提示框問玩家是否確定是這個下注金額，若否，則下注值歸零，若是，下注區域會只剩顯示下注金額的label
				else {
					int choice = JOptionPane.showConfirmDialog(null, "確定下注金額為" + stakeCount + "?", "下注確認",
							JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION && stakeCount >= 30 && stakeCount <= gameRule.getMoney()) {
						panel.removeAll();
						panel.add(stakeCountLabel);
						gameRule.setStake(stakeCount);
						startGame();
					} else if (choice == JOptionPane.NO_OPTION) {
						stakeCount = 0;
						stakeCountLabel.setText(stakeCount + "");
					} else if (stakeCount < 30) {
						JOptionPane.showMessageDialog(null, "你必須下注30元以上");
					} else if (stakeCount > gameRule.getMoney()) {
						JOptionPane.showMessageDialog(null, "最多只能下注你目前所持有全部的錢");
						stakeCount = 0;
						stakeCountLabel.setText("下注金額: "+stakeCount);
					}
				}
			}
		}
		stakeBtn.addActionListener(new StakeListener());
		panel.add(stakeBtn);
		
		return panel;
	}

	/*
	 * 新增下注選項的按鈕
	 * 
	 * @parameter howMuch: 下注的金額
	 * 
	 * @return stakeChoice: 下注選項的按鈕
	 */
	public JButton stakeChoiceBtn(int howMuch) {
		StakeButton stakeChoice = new StakeButton(howMuch + "");

		// 依照選項的值，將stakeCount的金額往上累加，並更新stakeCountLabel的字
		class StakeChoiceListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stakeCount += howMuch;
				stakeCountLabel.setText("下注金額: "+stakeCount);
			}
		}
		ActionListener listener=new StakeChoiceListener();
		stakeChoice.addActionListener(listener);
		
		return stakeChoice;
	}
	
	public void startGame() {
		gameRule.Draw();
		middleid = gameRule.show(gameRule.getDraw());
		middle.setIcon(imageResize(middleid + ".jpg"));
		String message = gameRule.judge();
		JOptionPane.showMessageDialog(null, message);
		try {
			if (message.contains("你獲得了")) {
				userInfo.addWinCount();
			}
			else {
				userInfo.addLoseCount();
			}
			userInfo.setMoney(gameRule.getMoney());
		}
		catch(SQLException e) {
			e.getMessage();
		}
		int choice = JOptionPane.showConfirmDialog(null, "還想再玩一次嗎?", "繼續玩或是回到主畫面", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) {
			DragonGateFrame DragonGate = new DragonGateFrame();
			DragonGate.setVisible(true);
			DragonGate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			dispose();
		}
		else {
			MainFrame main = new MainFrame();
			dispose();
		}
	}
	
	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
	
	private UserInfo getUserInfoFromLogInFrame() {
		for (Frame frame : JFrame.getFrames()) {
			if (frame.getTitle().equals("Log in")) {
				LoginFrame LogInFrame = (LoginFrame) frame;
				return LogInFrame.getUserInfo();
			}
		}
		return null;
	}

}
