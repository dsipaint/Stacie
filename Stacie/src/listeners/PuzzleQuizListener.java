package listeners;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.DataHandler;

public class PuzzleQuizListener extends ListenerAdapter
{
	private DataHandler dh;
	private Connection con;
	/*
	 * This listener specifically handles the quiz only
	 */
	
	public PuzzleQuizListener(DataHandler dh)
	{
		this.dh = dh;
		con = dh.getConnection();
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) //only capable of handling the one quiz
	{	
		if(!e.getAuthor().isBot()) //only responds to user messages
		{
			String userID = e.getAuthor().getId();	
			String currentStatus = "INIT";
			boolean entry_exists = false;
			if(dh.getResultSize("select * from userteams where discord_id = \"" + userID + "\"") > 0)
			{
				try
				{
					ResultSet rs = con.createStatement().executeQuery("select teamname from userteams where discord_id = \"" + userID + "\"");
					rs.absolute(1);
					currentStatus = rs.getString("teamname");
					entry_exists = true;
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			
			//update status
			currentStatus = PuzzleQuiz.playQuiz(e, currentStatus, e.getJDA().getGuildById("614260951167795204").getMemberById(userID));
			
			try
			{
				//Updating storage
				if(entry_exists) //if entry already exists update storage
					con.createStatement().executeUpdate("update userteams set teamname = \"" + currentStatus + "\" where discord_id = \"" + userID + "\"");
				else //add to storage
					con.createStatement().executeUpdate("insert into userteams (discord_id, teamname) values (\"" + userID + "\", \"" + currentStatus + "\")");
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}

		}
	}
}
