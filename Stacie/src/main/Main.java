package main;
import javax.security.auth.login.LoginException;

import listeners.DigRootListener;
import listeners.EloListener;
import listeners.HelpListener;
import listeners.MiscListener;
import listeners.PcreateListener;
import listeners.PuzzleCmdListener;
import listeners.PuzzleEditListener;
import listeners.PuzzleQuiz;
import listeners.PuzzleQuizListener;
import listeners.SendListener;
import listeners.SomeoneListener;
import listeners.StopListener;
import listeners.UserJoinTeamListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import utils.Constants;
import utils.DataHandler;

public class Main
{
	public static JDA jda;
	
	/*
	 * v2.0 CHANGELOG:
	 * 	- remember all database features won't work as the database is down- hosted on my desktop
	 * 	- moved commands to individual listeners
	 *	- removed colton-responses
	 *	- removed >doc
	 *	- rewrote hard-coded variables
	 *	- fixed database disconnect after inactivity issue
	 *	- introduced packages to differentiate between listeners and util-based classes
	 *	- improved efficiency of isStaff method
	 *	- removed redundancy in isStaff and giveHouseRole by passing member parameters as opposed to user-ids, avoiding referencing Main.jda
	 *	- reduced scope of digital root method
	 *	- added team enums, as teams are designed to stay constant
	 *	- made UserJoinTeamListener depend on the utils package with Tools.giveHouseRole()
	 *	- removed points from the teams system
	 *	- added @someone
	 *	- moved createLog(), giveHouseRole(), and isStaff() to more universal locations
	 *	- changed parameters of createLog to accept a user and retrieve variables that way, also avoids referencing Main.jda
	 *	- removed Flared puzzle exclusion
	 *	- added >elo [username]
	 */
	
	/*
	 * TODO: 
	 * 	- make >elo self-correcting if dud-entries are found when an image is requested
	 */
	
	public static void main(String[] args)
	{
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("").build();
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		DataHandler dh = new DataHandler();
		new PuzzleQuiz(dh); //only declare this in one place, makes sense to do it before the listener declarations
		
		jda.addEventListener(new PuzzleQuizListener(dh));
		
		jda.addEventListener(new HelpListener()); //help command
		jda.addEventListener(new EloListener(dh)); //elo commands
		jda.addEventListener(new StopListener()); //stop command
		jda.addEventListener(new MiscListener()); //random jokes
		jda.addEventListener(new DigRootListener()); //dgrt command
		jda.addEventListener(new SendListener()); //send command
		jda.addEventListener(new PuzzleCmdListener()); //puzzle commands
		jda.addEventListener(new SomeoneListener()); //@someone
		jda.addEventListener(new UserJoinTeamListener(dh)); //user joins server, puts them in a team if they were in one
		jda.addEventListener(new PcreateListener(dh)); //pcreate (use in DMs)
		jda.addEventListener(new PuzzleEditListener(dh)); //editing puzzles in dms
		
		jda.getTextChannelById(Constants.bot_cmds_channel).sendMessage("**STACIE IS AWAKE**").queue();
	}
}
