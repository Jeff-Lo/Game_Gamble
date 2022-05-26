import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import javax.swing.*;

public class NinetyNineFrame extends JFrame {
	private final int FRAME_WIDTH = (int) 33.87 * 25;
	private final int FRAME_HEIGHT = 500;
	private final int CARD_WIDTH = (int) 4.25 * 25;
	private final int CARD_HEIGHT = (int) 6.11 * 25;
	private Card cardP1, cardP2, cardP3, cardP4, cardP5;
	private int stakeCount;
	private JLabel count99, stakeCountLabel, computerCardLabel;
	private GameRule_99 gameRule;
	private Player_99 player;
	private ComPlayer_99 comPlayer;
	private UserInfo userInfo;
	private JPanel mainPanel;
	private JPanel userInfoPanel;
	private JPanel topPanel;
	private JPanel cardPanel;
	private JPanel computerPanel;
	private JPanel cardsPanel;

	public NinetyNineFrame() {
		// Initialize Stuff
		count99 = new JLabel("0");
		count99.setFont(new Font(count99.getText(), Font.PLAIN, 36));
		userInfo = getUserInfoFromLogInFrame();
		gameRule = new GameRule_99();
		comPlayer = new ComPlayer_99();
		player = new Player_99();
		player.setMoney(userInfo.getMoney());

		topPanel = new JPanel();
		topPanel.setOpaque(false);
		for (int i = 0; i < 5; i++) {
			topPanel.add(new JLabel(imageResize("cardBack.jpg")));
		}

		cardPanel = new JPanel();
		cardPanel.setOpaque(false);
		for (int i = 0; i < 5; i++) {
			cardPanel.add(new Card(-1));
		}
		
		computerPanel = new JPanel();
		computerPanel.setLayout(new BorderLayout());
		computerCardLabel = new JLabel(imageResize("cardBack.jpg"));
		computerPanel.add(new JLabel("電腦上一回合出的牌:"), BorderLayout.NORTH);
		computerPanel.add(computerCardLabel, BorderLayout.CENTER);

		// Add all panels to main panel
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BorderLayout());
		userInfoPanel = new UserInfoPanel();
		userInfoPanel.setOpaque(false);
		mainPanel.add(userInfoPanel, BorderLayout.NORTH);

		cardsPanel = new JPanel();
		cardsPanel.setOpaque(false);
		cardsPanel.add(topPanel);
		cardsPanel.add(cardPanel);
		mainPanel.add(cardsPanel, BorderLayout.CENTER);
		mainPanel.add(stakePanel(), BorderLayout.SOUTH);
		mainPanel.add(computerPanel, BorderLayout.WEST);
		mainPanel.add(count99, BorderLayout.EAST);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("99 Game");
		add(mainPanel);
		setVisible(true);
	}

	// Resize Images
	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}

	public JPanel stakePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		// Stake Count Label
		// Stake Button
		JButton stakeBtn = new JButton("下注");
		stakeBtn.setContentAreaFilled(false);
		stakeBtn.setBackground(Color.orange);
		// 他是不是第一次被點的boolean值
		boolean[] clickedOnce = { true };
		// 這個Listener是把下注區域變成點第一次會顯示一排下注選項的panel
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
					int choice = JOptionPane.showConfirmDialog(null, "確定下注金額為" + stakeCount, "下注確認",
							JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION && stakeCount >= 30 && stakeCount <= player.getMoney()) {
						panel.removeAll();
						JLabel showStake = new JLabel("目前下注: " + stakeCount);
						showStake.setFont(new Font(showStake.getText(), Font.PLAIN, 24));
						panel.add(showStake);
						panel.repaint();
						startGame();
					} else if (choice == JOptionPane.NO_OPTION) {
						stakeCount = 0;
						stakeCountLabel.setText(stakeCount + "");
					} else if (stakeCount < 30) {
						JOptionPane.showMessageDialog(null, "你必須下注30元以上");
					} else if (stakeCount > player.getMoney()) {
						JOptionPane.showMessageDialog(null, "最多只能下注你目前所持有全部的錢");
						stakeCount = 0;
						stakeCountLabel.setText("下注金額: "+stakeCount);
					}
					stakeCountLabel.setText(stakeCount + "");
				}
			}
		}
		stakeBtn.addActionListener(new StakeListener());

		// Restake Button
		JButton clearStakeBtn = new JButton("重下注");
		class ClearStakeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stakeCount = 0;
				stakeCountLabel.setText(stakeCount + "");
			}
		}
		clearStakeBtn.addActionListener(new ClearStakeListener());

		// Add buttons to panel
		panel.add(stakeBtn);
		panel.add(clearStakeBtn);

		return panel;
	}

	public JButton stakeChoiceBtn(int howMuch) {
		StakeButton stakeChoice = new StakeButton(howMuch + "");
		// 依照選項的值，將stakeCount的金額往上累加，並更新stakeCountLabel的字
		class StakeChoiceListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stakeCount += howMuch;
				stakeCountLabel.setText(stakeCount + "");
			}
		}
		ActionListener listener = new StakeChoiceListener();
		stakeChoice.addActionListener(listener);
		return stakeChoice;
	}

	public MouseListener setCardListener(Card card) {
		class cardClicked implements MouseListener {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() % 2 == 1) {
					card.setFocusPainted(true);
					System.out.println("first clicked at " + card.getCardName());
				}
				if (e.getClickCount() % 2 == 0) {
					card.setClicked();
					card.setFocusPainted(false);
					System.out.println("second clicked at " + card.getCardName());
					cardChose(card.getId());
					int newCard = gameRule.nextOne();
					gameRule.setNumber(gameRule.getNumber());
					judge("player");
					for (int i = 0; i <= 4; i++) {
						if (card.getId() == player.getPlayer()[i]) {
							int[] cards = player.getPlayer();
							cards[i] = newCard;
							player.setPlayer(cards);
						}
					}
					card.setId(newCard);
					card.setCardName();
					card.setIcon(imageResize(card.getCardName() + ".jpg"));
					if(computerTurn()) {
						judge("computer");
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		}
		return new cardClicked();
	}

	public boolean computerTurn() {
		int counter = Integer.parseInt(count99.getText());
		if(counter > 99) {
			return false;
		}
		int[] cards = comPlayer.getComputer();
		for (int i = 0; i <= 4; i++) {
			int card = cards[i];
			computerCardLabel.setIcon(imageResize(showCard(card) + ".jpg"));
			if (card == 0) {
				count99.setText("0");
				cards[i] = gameRule.nextOne();
				comPlayer.setComputer(cards);
				System.out.println("0");
				return true;
			} else if (card % 13 == 9) {
				if (counter + 10 <= 99) {
					count99.setText((Integer.parseInt(count99.getText()) + 10) + "");
					cards[i] = gameRule.nextOne();
					comPlayer.setComputer(cards);
					System.out.println("+10");
					return true;
				} else {
					count99.setText((Integer.parseInt(count99.getText()) - 10) + "");
					cards[i] = gameRule.nextOne();
					comPlayer.setComputer(cards);
					System.out.println("-10");
					return true;
				}
			} else if (card % 13 == 10) {
				JOptionPane.showMessageDialog(null, "Computer has passed");
				cards[i] = gameRule.nextOne();
				comPlayer.setComputer(cards);
				return true;
			} else if (card % 13 == 11) {
				if (counter + 20 <= 99) {
					count99.setText((Integer.parseInt(count99.getText()) + 20) + "");
					cards[i] = gameRule.nextOne();
					comPlayer.setComputer(cards);
					System.out.println("+20");
					return true;
				} else {
					count99.setText((Integer.parseInt(count99.getText()) - 20) + "");
					cards[i] = gameRule.nextOne();
					comPlayer.setComputer(cards);
					System.out.println("-20");
					return true;
				}
			} else if (card % 13 == 12) {
				count99.setText("99");
				cards[i] = gameRule.nextOne();
				comPlayer.setComputer(cards);
				System.out.println("99");
				return true;
			}else {
				if (counter + card % 13 + 1 <= 99) {
					count99.setText((Integer.parseInt(count99.getText())  + 1 + card%13) + "");
					cards[i] = gameRule.nextOne();
					comPlayer.setComputer(cards);
					System.out.println(card%13+1);
					return true;
				} else {
					count99.setText((Integer.parseInt(count99.getText())  + 1 + card%13) + "");
					System.out.println(card%13+1);
					return true;
				}
			}
		}
		return false;
	}

	public void judge(String turn) {
		if (Integer.parseInt(count99.getText()) > 99) {
			String judge = "";
			int number = gameRule.getNumber();
			if (turn.equals("computer")) {
				judge = "You Win";
				try {
					userInfo.addWinCount();
					if (number >= 30 && number <= 50) {
						player.setMoney(player.getMoney() + player.getStake());

					} else if (number < 30) {
						if (player.getStake() * 1.5 < Math.round(player.getStake() * (30 - number) * 0.02)) {
							player.setMoney(player.getMoney() + (int) Math.round(player.getStake() * 1.5));
						} else {
							player.setMoney(
									player.getMoney() + (int) Math.round(player.getStake() * (30 - number) * 0.02));
						}
					} else {
						if (player.getStake() / 2 < Math.round(player.getStake() * Math.abs(number - 70) * 0.01)) {
							player.setMoney(player.getMoney() + (int) Math.round(player.getStake() / 2));
						} else {
							player.setMoney(
									player.getMoney() + (int) Math.round(player.getStake() * Math.abs(number - 70) * 0.01));
						}
					}
					userInfo.setMoney(player.getMoney());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else if (turn.equals("player")) {
				judge = "You Lose";
				try {
					userInfo.addLoseCount();
					if (number >= 30 && number <= 50) {
						player.setMoney(player.getMoney() - player.getStake());
					} else if (number < 30) {
						if (player.getStake() * 1.5 < Math.round(player.getStake() * (30 - number) * 0.02)) {
							player.setMoney(player.getMoney() - (int) Math.round(player.getStake() * 1.5));
						} else {
							player.setMoney(
									player.getMoney() - (int) Math.round(player.getStake() * (30 - number) * 0.02));
						}
					} else {
						if (player.getStake() / 2 < Math.round(player.getStake() * Math.abs(number - 70) * 0.01)) {
							player.setMoney(player.getMoney() - (int) Math.round(player.getStake() / 2));
						} else {
							player.setMoney(
									player.getMoney() - (int) Math.round(player.getStake() * Math.abs(number - 70) * 0.01));
						}
					}
					userInfo.setMoney(player.getMoney());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			String[] option = { "重新開始", "回主畫面" };
			int choice = JOptionPane.showOptionDialog(null, judge, "Game Over", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, option, null);
			if (choice == JOptionPane.YES_OPTION) {
				NinetyNineFrame newGame = new NinetyNineFrame();
				dispose();
			} else if (choice == JOptionPane.NO_OPTION) {
				MainFrame main = new MainFrame();
				dispose();
			}
		}
	}

	public void startGame() {
		System.out.println("Game has started");
		gameRule.setCounter(0);
		gameRule.setNumber(0);
		player.setStake(stakeCount);

		String[] handCard = new String[5];
		for (int i = 0; i < 5; i++) {
			player.getPlayer()[i] = gameRule.nextOne();
			comPlayer.getComputer()[i] = gameRule.nextOne();
		}
		handCard = gameRule.show(player.getPlayer(), gameRule.getN1(), 1);
		cardPanel.removeAll();
		cardP1 = new Card(player.getPlayer()[0]);
		cardP1.addMouseListener(setCardListener(cardP1));
		cardP2 = new Card(player.getPlayer()[1]);
		cardP2.addMouseListener(setCardListener(cardP2));
		cardP3 = new Card(player.getPlayer()[2]);
		cardP3.addMouseListener(setCardListener(cardP3));
		cardP4 = new Card(player.getPlayer()[3]);
		cardP4.addMouseListener(setCardListener(cardP4));
		cardP5 = new Card(player.getPlayer()[4]);
		cardP5.addMouseListener(setCardListener(cardP5));
		cardPanel.add(cardP1);
		cardPanel.add(cardP2);
		cardPanel.add(cardP3);
		cardPanel.add(cardP4);
		cardPanel.add(cardP5);
		cardPanel.revalidate();
		cardPanel.repaint();
	}

	public void cardChose(int card) {
		if (card == 0) {
			count99.setText("0");
		} else if (card % 13 == 9) {
			String[] options = { "+10", "-10" };
			int choice = JOptionPane.showOptionDialog(null, "你要選擇+10或是-10", "你打出了10", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, null);
			if (choice == JOptionPane.YES_OPTION) {
				count99.setText((Integer.parseInt(count99.getText()) + 10) + "");
			} else if (choice == JOptionPane.NO_OPTION) {
				count99.setText((Integer.parseInt(count99.getText()) - 10) + "");
			}
		} else if (card % 13 == 10) {
			JOptionPane.showMessageDialog(null, "You've passed");
		} else if (card % 13 == 11) {
			String[] options = { "+20", "-20" };
			int choice = JOptionPane.showOptionDialog(null, "你要選擇+20或是-20", "你打出了Q", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, null);
			if (choice == JOptionPane.YES_OPTION) {
				count99.setText((Integer.parseInt(count99.getText()) + 20) + "");
			} else if (choice == JOptionPane.NO_OPTION) {
				count99.setText((Integer.parseInt(count99.getText()) - 20) + "");
			}
		} else if (card % 13 == 12) {
			count99.setText("99");
		} else {
			count99.setText((Integer.parseInt(count99.getText()) + 1 + card%13) + "");
		}
	}
	
	public String showCard(int a) {
		int a_change = a % 13 + 1;
		String result = "";
		if (a_change == 1) {
			result += "A";
		} else if (a_change == 11) {
			result += "J";
		} else if (a_change == 12) {
			result += "Q";
		} else if (a_change == 13) {
			result += "K";
		} else {
			result +=  "" + a_change;
		}
		
		if (0 <= a && a <= 12) {
			result += "_spade";
		} else if (13 <= a && a <= 25) {
			result += "_heart";
		} else if (26 <= a && a <= 38) {
			result += "_diamond";
		} else {
			result += "_club";
		}
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
