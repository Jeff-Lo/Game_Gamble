import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

// 這邊就是固定會出現的userInfo
public class UserInfoPanel extends JPanel {
	private UserInfo userInfo;
	private final int PANEL_HEIGHT = 60;
	private JLabel name, money, win, lose;
	private ImageIcon profile;
	private JButton hint;

	public UserInfoPanel() {
		setPreferredSize(new Dimension((int) 33.87 * 25, PANEL_HEIGHT));
		userInfo = getUserInfoFromLogInFrame();
		profile = userInfo.getProfile();
		add(new JLabel(profile));
		name = new JLabel("Name: " + userInfo.getName());
		add(name);
		money = new JLabel("Money: " + userInfo.getMoney());
		add(money);
		try {
			win = new JLabel("Win: " + userInfo.getWinCount());
			lose = new JLabel("Lose: " + userInfo.getLoseCount());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(win);
		add(lose);
		hint = new JButton("Hint");
		class HintListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				HintFrame hintFrame = new HintFrame();
				hintFrame.setVisible(true);
			}
		}
		hint.addActionListener(new HintListener());
		add(hint);
	}

	private UserInfo getUserInfoFromLogInFrame() {
		for (Frame frame : JFrame.getFrames()) {
			if (frame.getTitle().equals("Log in")) {
				LoginFrame LoginFrame = (LoginFrame) frame;
				return LoginFrame.getUserInfo();
			}
		}
		return null;
	}
}
