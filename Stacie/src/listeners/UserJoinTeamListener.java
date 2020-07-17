package listeners;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.DataHandler;
import utils.Team;
import utils.Tools;

public class UserJoinTeamListener extends ListenerAdapter
{
	private DataHandler dh;
	
	public UserJoinTeamListener(DataHandler dh)
	{
		this.dh = dh;
	}
	
	public void onGuildMemberJoin(GuildMemberJoinEvent e)
	{
		for(int i = 1; i <= dh.getResultSize("select * from userteams"); i++)
		{
			if(e.getUser().getId().equals(dh.retrieveElement("userteams", "discord_id", i))) //if a joining user is already in the database give them their team role
			{
				if(dh.retrieveElement("userteams", "teamname", i).equals("CELESTIAL"))
					Tools.giveHouseRole(Team.CELESTIAL, e.getMember());
				else if(dh.retrieveElement("userteams", "teamname", i).equals("LUNARPINE"))
					Tools.giveHouseRole(Team.LUNARPINE, e.getMember());
				else if(dh.retrieveElement("userteams", "teamname", i).equals("SOLARIS"))
					Tools.giveHouseRole(Team.SOLARIS, e.getMember());
				else if(dh.retrieveElement("userteams", "teamname", i).equals("LUMIOUS"))
					Tools.giveHouseRole(Team.LUMIOUS, e.getMember());
			}
		}
	}
}
