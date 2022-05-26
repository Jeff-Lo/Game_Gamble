import javax.swing.ImageIcon;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserInfo {
	private final int GRAPH_HEIGHT = (int) 2.21 * 25;
	private final int GRAPH_WIDTH = (int) 3.01 * 25;
	private final String IMAGE_FOLDER = "images/";
	private String name;
	private String accountNum;
	private String password;
	private int money;
	private int winCount;
	private int loseCount;
	private ImageIcon profile;
	private String imagePath;

	
	//connect database(INSERT INTO)
	public UserInfo (String name,String accountNum,String password,String imagePath, int money) throws SQLException {
		this.name=name;
		this.accountNum=accountNum;
		this.password=password;
		this.money=money;
		this.winCount=0;
		this.loseCount=0;
		this.imagePath = imagePath;
		this.profile = imageResize(imagePath);
		boolean exist=false;
		Connection conn = Database.getConnection();
		try {
			Statement stat1=conn.createStatement();
			ResultSet result=stat1.executeQuery("SELECT accountNum,password FROM UserInfo");
			while(result.next()) {
				if(result.getString("accountNum").equals(accountNum) && result.getString("password").equals(password)) {
					exist=true;
				}
			}
			if(!exist) {
				PreparedStatement stat = conn.prepareStatement("INSERT INTO UserInfo VALUES(?,?,?,?,?,?,?)" );
				stat.setString(1,accountNum);
				stat.setString(2,password);
				stat.setString(3,name);
				stat.setInt(4,money);
				stat.setInt(5,winCount);
				stat.setInt(6,loseCount);
				stat.setString(7,imagePath);
				stat.execute();
			}
		}
		finally {
		    conn.close();
		}
	}
	
	//update(UPDATE)
	public void setMoney(int money) throws SQLException {
		this.money=money;
		Connection conn = Database.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement("UPDATE UserInfo SET money=? WHERE accountNum=? AND password=?" );
			stat.setInt(1,money);
			stat.setString(2,accountNum);
			stat.setString(3,password);
			stat.executeUpdate();
		}
		finally
		  {
		   conn.close();
		  }
		
	}
	
	//update(UPDATE)
	public void setWinCount(int count) throws SQLException{
		this.winCount=count;
		Connection conn = Database.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement("UPDATE UserInfo SET winCount=? WHERE accountNum=? AND password=?" );
			stat.setInt(1,winCount);
			stat.setString(2,accountNum);
			stat.setString(3,password);
			stat.executeUpdate();
		}
		finally
		  {
		   conn.close();
		  }
	}
	
	//update(UPDATE)
	public void setLoseCount(int count) throws SQLException{
		this.loseCount=count;
		Connection conn = Database.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement("UPDATE UserInfo SET loseCount=? WHERE accountNum=? AND password=?" );
			stat.setInt(1,loseCount);
			stat.setString(2,accountNum);
			stat.setString(3,password);
			stat.executeUpdate();
		}
		finally
		  {
		   conn.close();
		  }
	}
	
	public String getName() {
		return name;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getWinCount() throws SQLException {
		Connection conn=null;
		try {
			conn=Database.getConnection();
			Statement stat1=conn.createStatement();
			ResultSet result=stat1.executeQuery(String.format("SELECT winCount FROM UserInfo WHERE accountNum='%s' AND password='%s'"
					,accountNum, password));
			while(result.next()) {
				int win=result.getInt("winCount");
				winCount=win;
			}
		}
		finally {
			conn.close();
		}
		return winCount;
	}
	
	public int getLoseCount() throws SQLException {
		Connection conn=null;
		try {
			conn=Database.getConnection();
			Statement stat1=conn.createStatement();
			ResultSet result=stat1.executeQuery(String.format("SELECT loseCount FROM UserInfo WHERE accountNum='%s' AND password='%s'"
					,accountNum, password));
			while(result.next()) {
				int lose=result.getInt("loseCount");
				loseCount=lose;
			}
		}
		finally {
			conn.close();
		}
		return loseCount;
	}
	
	public String getAccountNum() {
		return accountNum;
	}
	
	public String getPassword() {
		return password;
	}
	
	public ImageIcon getProfile() {
		return profile;
	}

	public String getImagePath() {
		return imagePath;
	}
	
	//update(UPDATE)
	public void addWinCount() throws SQLException{
		winCount++;
		Connection conn = Database.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement("UPDATE UserInfo SET winCount=? WHERE accountNum=? AND password=?" );
			stat.setInt(1,winCount);
			stat.setString(2,accountNum);
			stat.setString(3,password);
			stat.executeUpdate();
		}
		finally {
			conn.close();
		}
	}
	
	//update(UPDATE)
	public void addLoseCount() throws SQLException{
		loseCount++;
		Connection conn = Database.getConnection();
		try {
			PreparedStatement stat = conn.prepareStatement("UPDATE UserInfo SET loseCount=? WHERE accountNum=? AND password=?" );
			stat.setInt(1,loseCount);
			stat.setString(2,accountNum);
			stat.setString(3,password);
			stat.executeUpdate();
		}
		finally {
			conn.close();
		}
	}
	
	public ImageIcon imageResize(String imagePath) {
		ImageIcon graph = new ImageIcon(getClass().getClassLoader().getResource(imagePath)); //加這行才能把圖片包進去jar檔
		Image graphResized = graph.getImage().getScaledInstance(GRAPH_WIDTH, GRAPH_HEIGHT, java.awt.Image.SCALE_SMOOTH);
		ImageIcon result = new ImageIcon(graphResized);
		return result;
	}
}
