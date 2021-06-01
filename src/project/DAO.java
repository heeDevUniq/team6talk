package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DAO {

	private Connection con;
	PreparedStatement ps = null;
	ResultSet rs = null;

	public DAO() throws ClassNotFoundException, SQLException {
		con = new DBConn().getCon();
	}

	public void closeAll() throws SQLException {
		if(rs != null) rs.close();
		if(ps != null) ps.close();
		if(con != null) con.close();
	}

	public boolean insertTalker(String nickname) {
				
		String sql = "INSERT INTO userList VALUES (?, to_date(?))";
		
		try {
			ps = con.prepareStatement(sql);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String sysdate = sdf.format(Calendar.getInstance().getTime());
			
			ps.setString(1, nickname);
			ps.setString(2, sysdate);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}

}
