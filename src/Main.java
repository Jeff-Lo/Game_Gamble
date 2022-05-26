import java.sql.SQLException;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws SQLException {
		LoginFrame game = new LoginFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}

}
