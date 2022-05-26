import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

//簡單說明一下用法: 
//輸入帳號密碼如果通過了，這個frame就會隱藏起來，但是會儲存userInfo，之後遊戲跟userInfoPanel get到的都是這裡的userInfo
public class LoginFrame extends JFrame {
	private final int FRAME_WIDTH = 400;
	private final int FRAME_HEIGHT = 200;
	private final int FIELD_WIDTH = 16;
	private UserInfo userInfo;
	private JLabel logIn;
	private JLabel acc;
	private JLabel password;
	private JTextField accField;
	private JPasswordField passwordField;
	private JButton logInBtn;
	private JButton signUpBtn;

	public LoginFrame() {
		setTitle("Log in");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.add(titlePanel());
		panel.add(fieldPanel());
		panel.add(logInPanel());
		panel.add(signUpPanel());
		add(panel);
	}

	public JPanel titlePanel() {
		JPanel panel = new JPanel();
		logIn = new JLabel("Login");
		logIn.setFont(new Font(logIn.getText(), Font.PLAIN, 36));
		panel.add(logIn);
		return panel;
	}
	

	public JPanel fieldPanel() {
		JPanel panel = new JPanel();
		acc = new JLabel("Account Number");
		password = new JLabel("Password");
		accField = new JTextField(FIELD_WIDTH);
		passwordField = new JPasswordField(FIELD_WIDTH);
		passwordField.setEchoChar('*');
		panel.setLayout(new GridLayout(2, 2));
		panel.add(acc);
		panel.add(accField);
		panel.add(password);
		panel.add(passwordField);
		return panel;
	}

	public JPanel logInPanel() {
		JPanel panel = new JPanel();
		logInBtn = new JButton("Log In");
		class LogInListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Database database = new Database();
				String[] info = new String[7];
				String passwordString = "";

				if (accField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please fill in your account number and password");
				} else {
					for (char word : passwordField.getPassword()) {
						passwordString += word;
						// This message is never shown
//						if (passwordString.length() == 0) {
//							JOptionPane.showMessageDialog(null, "Please fill in your password");
//							break;
//						}
					}
					
					try {
						info = database.doSQL(String.format("SELECT * FROM UserInfo WHERE accountNum='%s' AND password='%s'", 
								accField.getText(), passwordString));
					} catch (SQLException exception) {
						JOptionPane.showMessageDialog(null, exception.getMessage() + "1");
					}
					
					// 如果登入成功，會建立一個userInfo的物件，然後判斷登入獎勵
					try {
						userInfo = new UserInfo(info[2], info[0], info[1], info[6], Integer.parseInt(info[3]));
						setVisible(false);
						
						LocalDate lastLogIn = LocalDate.parse(info[7]);
						LoginReward reward = new LoginReward(lastLogIn, userInfo.getMoney());						
						
						
						if (reward.getReward()) {
							userInfo.setMoney(100 + userInfo.getMoney());
							JOptionPane.showMessageDialog(null,
									String.format("You've earned a log in reward.\nNow you have %d.", userInfo.getMoney()));
							try {
								database.doSQL(String.format("UPDATE UserInfo SET last_log_in_date = '%s' WHERE accountNum='%s' AND password='%s'",
										reward.getLastLoginDate().toString(), accField.getText(), passwordString));
								
							} catch (SQLException exception) {
								JOptionPane.showMessageDialog(null, exception.getMessage() + "2");
							}
						}
						MainFrame mainFrame = new MainFrame();
					} catch (ArrayIndexOutOfBoundsException exception) {
						JOptionPane.showMessageDialog(null,
								"Account number/Password is wrong.\nClick sign up if this is your first time playing this game.");
					} catch (SQLException exception) {
						JOptionPane.showMessageDialog(null,
								"SQL Error");
						exception.printStackTrace();
					}
					
				}
				
			}
		}
		logInBtn.addActionListener(new LogInListener());
		panel.add(logInBtn);
		return panel;
	}
	
	public JPanel signUpPanel() {
		JPanel panel = new JPanel();
		signUpBtn = new JButton("Sign Up");
		class SignUpListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				SignUpFrame signUpFrame = new SignUpFrame();
				signUpFrame.setVisible(true);
				setVisible(false);
			}
		}
		signUpBtn.addActionListener(new SignUpListener());
		panel.add(signUpBtn);
		return panel;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

}
