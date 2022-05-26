import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

//這邊database的用法，就是直接在doSQL()的括號裡面打要打的SQL code，要注意的是如果有要穿插變數，請注意空格。
//例如"WHERE Student_id="+studentID+" AND Course_id="+courseID"，結尾分號可加可不加，如果只打算一次跑一行的話。
public class Database {
	private static String url;
	private static String username;
	private static String password;
	
	public static Connection getConnection() throws SQLException {
		String server = "jdbc:mysql://140.119.19.73:9306/";
		String database = "MG12";
		String config= "?useUnicode=true&characterEncoding=utf8";
		url = server + database + config;
		username = "MG12"; 
		password = "mfau2h";
		return DriverManager.getConnection(url, username, password);
	}
	
	public ResultSet SQLQuery(String SQLCode) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			boolean hasUser = stat.execute(SQLCode);
			if (hasUser) {
				//System.out.print(hasUser);
				ResultSet rs = stat.executeQuery(SQLCode);
				while (rs.next()) {
					return rs;
				}

			}
			
			
		} 
		catch (Exception e) {
			e.getMessage();
		} 
		finally {
			if (conn != null) 
			{
				conn.close();
			}
		}
		return null;
	}

	
	public String[] doSQL(String SQLcode) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			boolean hasResultSet = stat.execute(SQLcode);
			if (hasResultSet) {
				ResultSet result = stat.getResultSet();			
				ResultSetMetaData metaData = result.getMetaData();
				String[] info = new String[metaData.getColumnCount() - 1];
				String getResult = "";
				int x = metaData.getColumnCount();
				while (result.next()) {
					for (int i = 1; i <= x; i++) {
						if (i > 1)
							getResult += ",";
						getResult += result.getString(i);
					}
				}
				info = getResult.split(",");
				return info;
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (conn != null) {
				conn.close();
			}
			
		}
		return null;
	}
	
	public void SQLInsert(String SQLcode) throws SQLException{
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.execute(SQLcode);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
}