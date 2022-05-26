import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class BlackJackFrame extends JFrame {
	private final int FRAME_WIDTH = (int)33.87*25;
	private final int FRAME_HEIGHT = 500;
	private final int CARD_WIDTH = (int) 4.25 * 25;
	private final int CARD_HEIGHT = (int) 6.11 * 25;

	private JLabel cardP1Label, cardP2Label, cardP3Label, cardP4Label, cardP5Label;
	private JLabel cardC1Label, cardC2Label, cardC3Label, cardC4Label, cardC5Label;
	private int stakeCount;
	private JLabel stakeCountLabel, scoreCountLabel;
	private GameRule_21 gameRule;
	private ComPlayer_21 comPlayer;
	private Player_21 player;
	private UserInfo userInfo;

	public BlackJackFrame() {
		// initialize everything
		userInfo = getUserInfoFromLogInFrame();
		
		gameRule = new GameRule_21();
		comPlayer = new ComPlayer_21();
		player = new Player_21();
		player.setMoney(userInfo.getMoney());
		
		cardP1Label = new JLabel();
		cardP2Label = new JLabel();
		cardP3Label = new JLabel();
		cardP4Label = new JLabel();
		cardP5Label = new JLabel();
		
		cardC1Label = new JLabel();
		cardC2Label = new JLabel();
		cardC3Label = new JLabel();
		cardC4Label = new JLabel();
		cardC5Label = new JLabel();
		
		scoreCountLabel = new JLabel("總和:0");
		
		//電腦牌面
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,5));
		topPanel.add(cardC1Label);
		topPanel.add(cardC2Label);
		topPanel.add(cardC3Label);
		topPanel.add(cardC4Label);
		topPanel.add(cardC5Label);
		
		//玩家牌面
		JPanel cardPanel = new JPanel();
		cardPanel.setLayout(new GridLayout(1,5));
		cardPanel.add(scoreCountLabel);
		cardPanel.add(cardP1Label);
		cardPanel.add(cardP2Label);
		cardPanel.add(cardP3Label);
		cardPanel.add(cardP4Label);
		cardPanel.add(cardP5Label);
		
		//把電腦牌面和玩家牌面放到CENTER
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,1));
		centerPanel.add(topPanel);
		centerPanel.add(cardPanel);
		
		//SOUTH
		JPanel southPanel=new JPanel();
		southPanel.add(stakePanel());
		southPanel.add(scoreCountLabel);
		
		// 塞物件到frame
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new UserInfoPanel(),BorderLayout.NORTH);
		panel.add(centerPanel,BorderLayout.CENTER);
		panel.add(southPanel,BorderLayout.SOUTH);
		add(panel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Blackjack");
	}

	public boolean ask(Player_21 player) {
		if (gameRule.checkPoints(player.getPlayer(), gameRule.getN1()) >= 21) {
			return false;
		}
		int answer = JOptionPane.showConfirmDialog(null, "需要加牌嗎", "", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	public void startGame()  {
		player.setStake(stakeCount);
		
		// 開局玩家先抽2張牌
		player.getPlayer()[0] = gameRule.nextOne();
		player.getPlayer()[1] = gameRule.nextOne();
		cardP1Label.setIcon(imageResize(gameRule.show(player.getPlayer(), gameRule.getN1())[0] + ".jpg"));
		cardP2Label.setIcon(imageResize(gameRule.show(player.getPlayer(), gameRule.getN1())[1] + ".jpg"));
		
		scoreCountLabel.setText("總和:"+gameRule.checkPoints(player.getPlayer(), gameRule.getN1()));
		
		// 開局電腦先抽2張牌
		comPlayer.getComputer()[0] = gameRule.nextOne();
		comPlayer.getComputer()[1] = gameRule.nextOne();
		cardC1Label.setIcon(imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[0] + ".jpg"));
		cardC2Label.setIcon(imageResize("cardBack.jpg"));
		
		// 下面用while來判斷玩家已持有牌數還需不需要抽下一張牌
		while(gameRule.getN1() < 5) {
			if (ask(player)) {
				player.getPlayer()[gameRule.getN1()] = gameRule.nextOne();
				gameRule.setN1(gameRule.getN1() + 1);
				switch (gameRule.getN1()) {
				case 3:
					cardP3Label.setIcon(imageResize(
							gameRule.show(player.getPlayer(), gameRule.getN1())[2] + ".jpg"));
					break;
				case 4:
					cardP4Label.setIcon(imageResize(
							gameRule.show(player.getPlayer(), gameRule.getN1())[3] + ".jpg"));
					break;
				case 5:
					cardP5Label.setIcon(imageResize(
							gameRule.show(player.getPlayer(), gameRule.getN1())[4] + ".jpg"));
					break;
				}
				scoreCountLabel.setText("總和:"+gameRule.checkPoints(player.getPlayer(), gameRule.getN1()));
			} else {
				break;
			}
			
			// 玩家爆掉就直接判定結果
			if (gameRule.checkPoints(player.getPlayer(), gameRule.getN1()) > 21) {
				switch (gameRule.getN2()) {
				case 2:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					break;
				case 3:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					break;
				case 4:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					cardC4Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
					break;
				case 5:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					cardC4Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
					cardC5Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[4] + ".jpg"));
					break;
				}
				JOptionPane.showMessageDialog(null, gameRule.judge(player, comPlayer, gameRule.getN1()));
				try {
					userInfo.setMoney(player.getMoney());
					if(gameRule.getWinOrLose()==0) {
						userInfo.addWinCount();
					}
					else if(gameRule.getWinOrLose()==2) {
						userInfo.addLoseCount();
					}
				}
				catch(SQLException e){
					e.getMessage();
				}
				int choice = JOptionPane.showConfirmDialog(null, "還想再玩一次嗎?", "繼續玩或是回到主畫面", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					BlackJackFrame blackJack = new BlackJackFrame();
					blackJack.setVisible(true);
					setVisible(false);
				}
				else {
					MainFrame main = new MainFrame();
					main.setVisible(true);
					setVisible(false);
				}
				return;
			}
			// 玩家已經拿到21點就不用再詢問玩家還要不要抽牌
			else if (gameRule.checkPoints(player.getPlayer(), gameRule.getN1()) == 21) {
				break;
			}
		}

		// 下面用while來判斷電腦已持有牌數還需不需要抽下一張牌
		while (gameRule.getN2() < 5) {
			if (gameRule.computerDraw(comPlayer)) {
				comPlayer.getComputer()[gameRule.getN2()] = gameRule.nextOne();
				gameRule.setN2(gameRule.getN2() + 1);
				switch (gameRule.getN2()) {
				case 3:
					cardC3Label.setIcon(imageResize("cardBack.jpg"));
					break;
				case 4:
					cardC4Label.setIcon(imageResize("cardBack.jpg"));
					break;
				case 5:
					cardC5Label.setIcon(imageResize("cardBack.jpg"));
					break;
				}
			} else {
				break;
			}
			// 電腦爆掉就直接判定結果
			if (gameRule.checkPoints(comPlayer.getComputer(), gameRule.getN2()) > 21) {
				switch (gameRule.getN2()) {
				case 2:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					break;
				case 3:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					break;
				case 4:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					cardC4Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
					break;
				case 5:
					cardC2Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
					cardC3Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
					cardC4Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
					cardC5Label.setIcon(imageResize(
							gameRule.show(comPlayer.getComputer(), gameRule.getN2())[4] + ".jpg"));
					break;
				}
				JOptionPane.showMessageDialog(null, gameRule.judge(player, comPlayer, gameRule.getN1()));
				try {
					userInfo.setMoney(player.getMoney());
					if(gameRule.getWinOrLose()==0) {
						userInfo.addWinCount();
					}
					else if(gameRule.getWinOrLose()==2) {
						userInfo.addLoseCount();
					}
				}
				catch(SQLException e){
					e.getMessage();
				}
				int choice = JOptionPane.showConfirmDialog(null, "還想再玩一次嗎?", "繼續玩或是回到主畫面", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					BlackJackFrame blackJack = new BlackJackFrame();
					blackJack.setVisible(true);
					setVisible(false);
				}
				else {
					MainFrame main = new MainFrame();
					main.setVisible(true);
					setVisible(false);
				}
				return;
			}
		}

		// 玩家和電腦都沒爆掉，判定最終結果
		switch (gameRule.getN2()) {
		case 2:
			cardC2Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
			break;
		case 3:
			cardC2Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
			cardC3Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
			break;
		case 4:
			cardC2Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
			cardC3Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
			cardC4Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
			break;
		case 5:
			cardC2Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[1] + ".jpg"));
			cardC3Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[2] + ".jpg"));
			cardC4Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[3] + ".jpg"));
			cardC5Label.setIcon(
					imageResize(gameRule.show(comPlayer.getComputer(), gameRule.getN2())[4] + ".jpg"));
			break;
		}
		JOptionPane.showMessageDialog(null, gameRule.judge(player, comPlayer, gameRule.getN1()));
		try {
			userInfo.setMoney(player.getMoney());
			if(gameRule.getWinOrLose()==0) {
				userInfo.addWinCount();
			}
			else if(gameRule.getWinOrLose()==2) {
				userInfo.addLoseCount();
			}
		}
		catch(SQLException e){
			e.getMessage();
		}
		int choice = JOptionPane.showConfirmDialog(null, "還想再玩一次嗎?", "繼續玩或是回到主畫面", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) {
			BlackJackFrame blackJack = new BlackJackFrame();
			blackJack.setVisible(true);
			dispose();
		}
		else {
			MainFrame main = new MainFrame();
			dispose();
		}
	}

	public JPanel stakePanel() {
		JPanel panel = new JPanel();

		JButton stakeBtn = new JButton("下注");
		stakeBtn.setContentAreaFilled(false);
		stakeBtn.setBackground(Color.orange);
		
		// 他是不是第一次被點的boolean值
		boolean[] clickedOnce = {true};
		
		// 這個Listener是把下注區域變成點第一次會顯示一排下注選項的panel
		class StakeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// 顯示目前下注金額的label
				stakeCountLabel = new JLabel("下注金額: "+stakeCount);
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
					int choice = JOptionPane.showConfirmDialog(null, "確定下注金額為" + stakeCount, "下注確認",
							JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION && stakeCount >= 30 && stakeCount <= player.getMoney()) {
						panel.removeAll();
						panel.add(stakeCountLabel);
						panel.repaint();
						startGame();
					} else if (choice == JOptionPane.NO_OPTION) {
						stakeCount = 0;
						stakeCountLabel.setText("下注金額: "+stakeCount);
					} else if (stakeCount < 30) {
						JOptionPane.showMessageDialog(null, "你必須下注30元以上");
					} else if (stakeCount > player.getMoney()) {
						JOptionPane.showMessageDialog(null, "最多只能下注你目前所持有全部的錢");
						stakeCount = 0;
						stakeCountLabel.setText("下注金額: "+stakeCount);
					}
				}
			}
		}
		ActionListener listener=new StakeListener();
		stakeBtn.addActionListener(listener);
		
		//重下注按鈕
		JButton clearStakeBtn = new JButton("重下注");
		class ClearStake implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stakeCount = 0;
				stakeCountLabel.setText("下注金額: "+stakeCount);
			}
		}
		clearStakeBtn.addActionListener(new ClearStake());
		
		// Add buttons to panel
		panel.add(stakeBtn);
		panel.add(clearStakeBtn);
		
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

	/*
	 * 重新設定ImageIcon的圖片大小
	 * 
	 * @parameter imagePath: 圖片路徑
	 * 
	 * @return graph: 調整大小後的ImageIcon
	 */
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
