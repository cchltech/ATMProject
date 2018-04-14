package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtil {
	private static final String url="jdbc:mysql://localhost:3306/one?" 
			+"useSSL=true&user=root&password=17876253432&useUnicode=true&characterEncoding=UTF8";
	
	protected static Statement s=null;
	protected static ResultSet rs = null;
	protected static Connection conn = null;
	protected static PreparedStatement ps = null;
	
	public static Connection getConn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		return conn;
	}
	
	public static int executeUpdate(String sql) throws Exception {
		int result=0;
		s=getConn().createStatement();
		result=s.executeUpdate(sql);
		return result;
	}
	
	public static ResultSet executeQuery(String sql) throws Exception {
		s = getConn().createStatement(); 
		rs = s.executeQuery(sql);
		return rs;
	}
	
	/**
	 * ִ�ж�̬SQL���
	 * @param sql ���в����Ķ�̬SQL��䡣 
	 * @return ����PreparedStatement����
	 */
	public static PreparedStatement executePS(String sql) throws Exception {
		ps = getConn().prepareStatement(sql);
		return ps;
	}
	
	public static void close() throws Exception {
		if (rs !=null)	
			rs.close();
		if (s != null)
			s.close();
		if (ps !=null)
			ps.close();
		if (conn !=null)
			conn.close();	
	}
}