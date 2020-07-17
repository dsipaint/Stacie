package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataHandler
{
	private static Connection con;
	
	public enum DataType{
		INTEGER,
		STRING
	}
	
	public Connection getConnection()
	{
		if(con == null)
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost/staciedb" //(my desktop)
						+ "?useUnicode=true"
						+ "&useJDBCCompliantTimezoneShift=true"
						+ "&useLegacyDatetimeCode=false"
						+ "&serverTimezone=BST"
						+ "&autoReconnect=true";
				String usr = "root";
				String pwd = "f1nl4y1999";
				con = DriverManager.getConnection(url, usr, pwd);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return con;
	}
	
	public void removeElementByEntryNo(String tablename, int entryno)
	{
		/*
		 * NOTE: Every table to have elements deleted has a primary key column
		 * called "id", which is int (auto_increment). This column exists purely to
		 * allow the code to be able to delete, as a primary key column is needed
		 * to delete from a table
		 */
		try
		{
			Statement s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = s.executeQuery("select * from " + tablename);
			
			rs.absolute(entryno);
			rs.deleteRow();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateElementByEntryNo(String tablename, int entryno, String column_name, String new_value, DataType type)
	{
		try
		{
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("select * from " + tablename);
			
			rs.absolute(entryno);
			
			switch(type)
			{
				case INTEGER:
					rs.updateInt(column_name, Integer.parseInt(new_value));
					break;
					
				case STRING:
					rs.updateString(column_name, new_value);
					break;
					
				default:
					rs.updateString(column_name, new_value);
					break;
			}

		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	public int getResultSize(String query)
	{
		int i = 0;
		
		try
		{
			ResultSet rs = con.createStatement().executeQuery(query);
			while(rs.next())
				i++;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return i;
	}
	
	public String retrieveElement(String table, String column_name, int entryno)
	{
		try
		{
			ResultSet rs = con.createStatement().executeQuery("select * from " + table);
			rs.absolute(entryno);
			return rs.getString(column_name);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;

	}
	
}
