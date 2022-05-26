import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public class SignUpFrame extends JFrame {
	private final int FRAME_WIDTH = (int) 22 * 25;
	private final int FRAME_HEIGHT = (int) 17.2 * 25;
	private final int FIELD_WIDTH = 16;
	private final int NAME_FIELD_WIDTH = 20;
	private final int GRAPH_HEIGHT = (int) 2.21 * 25;
	private final int GRAPH_WIDTH = (int) 3.01 * 25;

	private JLabel signUp, acc, password, name, profile;
	private JTextField accField, nameField;
	private JPasswordField passwordField;
	private JButton cancelBtn, signUpBtn;
	private JRadioButton img1, img2, img3, img4;
	private ButtonGroup imggroup;
	private Database database;

	public SignUpFrame() {
		signUp = new JLabel("Sign up");
		signUp.setFont(new Font(signUp.getText(), Font.PLAIN, 36));
		acc = new JLabel("Account Number");
		password = new JLabel("Password");
		name = new JLabel("Nickname");
		profile = new JLabel("Profile");
		accField = new JTextField(FIELD_WIDTH);
		passwordField = new JPasswordField(FIELD_WIDTH);
		passwordField.setEchoChar('*');
		nameField = new JTextField(NAME_FIELD_WIDTH);
		database = new Database();
		
		
		cancelBtn = new JButton("Cancel");
		class CancelListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				getUserInfoFromLogInFrame().setVisible(true);
				dispose();
			}
		}
		cancelBtn.addActionListener(new CancelListener());
		
		signUpBtn = new JButton("Sign up");
		class SignUpListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String accNum = accField.getText();
				char[] passwordchar = passwordField.getPassword();
				String passwordNum = "";
				for (char password : passwordchar) {
					passwordNum += password;
				}
				String nameString = nameField.getText();
				String imgName = "";
				if (img1.isSelected()) {
					imgName = "img1.jpg";
				}
				else if (img2.isSelected()) {
					imgName = "img2.jpg";
				}
				else if (img3.isSelected()) {
					imgName = "img3.jpg";
				}
				else if (img4.isSelected()) {
					imgName = "img4.jpg";
				}
				String today = LocalDate.now().toString();

				if (accNum.equals("") || passwordNum.equals("") || nameString.equals("")) {
					JOptionPane.showMessageDialog(null, "Account number/Password/Name should be filled up.");
				} 
				else {
					try {
						if (database.SQLQuery(String.format("SELECT * FROM UserInfo WHERE accountNum= '%s'", accNum)) != null) {
							JOptionPane.showMessageDialog(null, "That account number has been used!");
						}
						else {
							database.doSQL(String.format("INSERT INTO UserInfo VALUES ('%s', '%s', '%s', %d, %d, %d, '%s', '%s')", 
									accNum, passwordNum, nameString, 1000, 0, 0, imgName, today));
							JOptionPane.showMessageDialog(null, "You have signed in! Please log in to enjoy the game.");
							getUserInfoFromLogInFrame().setVisible(true);
							dispose();
						}
						
					} catch (SQLException exception) {
						System.out.print("Error");
					}
				}
			}
		}
		signUpBtn.addActionListener(new SignUpListener());
		
		img1 = new JRadioButton();
		img2 = new JRadioButton();
		img3 = new JRadioButton();
		img4 = new JRadioButton();
		
		JLabel imgLabel1 = new JLabel(imageResize("img1.jpg"));
		JLabel imgLabel2 = new JLabel(imageResize("img2.jpg"));
		JLabel imgLabel3 = new JLabel(imageResize("img3.jpg"));
		JLabel imgLabel4 = new JLabel(imageResize("img4.jpg"));

		imggroup = new ButtonGroup();
		imggroup.add(img1);
		imggroup.add(img2);
		imggroup.add(img3);
		imggroup.add(img4);
		
		JPanel labelPanel = new JPanel();
		labelPanel.add(signUp);
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(3, 2));
		fieldPanel.add(acc);
		fieldPanel.add(accField);
		fieldPanel.add(password);
		fieldPanel.add(passwordField);
		fieldPanel.add(name);
		fieldPanel.add(nameField);
		JPanel profilePanel = new JPanel();
		profilePanel.add(profile);
		profilePanel.add(img1);
		profilePanel.add(imgLabel1);
		profilePanel.add(img2);
		profilePanel.add(imgLabel2);
		profilePanel.add(img3);
		profilePanel.add(imgLabel3);
		profilePanel.add(img4);
		profilePanel.add(imgLabel4);
		JPanel btnPanel = new JPanel();
		btnPanel.add(cancelBtn);
		btnPanel.add(signUpBtn);
		
		// Add panels to frame
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(4, 1));
		add(labelPanel);
		add(fieldPanel);
		add(profilePanel);
		add(btnPanel);
		
	}

	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(GRAPH_WIDTH, GRAPH_HEIGHT,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
	
	private LoginFrame getUserInfoFromLogInFrame() {
		for (Frame frame : JFrame.getFrames()) {
			if (frame.getTitle().equals("Log in")) {
				LoginFrame LogInFrame = (LoginFrame) frame;
				return LogInFrame;
			}
		}
		return null;
	}
}
