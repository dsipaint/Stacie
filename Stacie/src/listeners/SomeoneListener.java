package listeners;
import java.util.LinkedList;
import java.util.Random;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;

public class SomeoneListener extends ListenerAdapter
{
	private String[] blacklisted_members = {
			"232952565334016000", //flared
			"329979171562586124" //puchimi
	};
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		if(e.getGuild().getId().equals(Constants.guild) && !e.getChannel().getId().equals(Constants.at_someone))
			return;
		
		String msg = e.getMessage().getContentRaw();
		Random r = new Random();
		
		if(msg.matches(".*@[Ss]omeone.*"))
		{
			Member m = getBlacklist(e.getGuild()).get(r.nextInt(getBlacklist(e.getGuild()).size()));
			e.getChannel().sendMessage(m.getAsMention()).queue();
			return;
		}
	}
		
	private LinkedList<Member> getBlacklist(Guild g)
	{
		LinkedList<Member> blacklist = new LinkedList<Member>(g.getMembers());
		
		for(String id : blacklisted_members)
		{
			if(g.getMemberById(id) != null)
				blacklist.remove(g.getMemberById(id));
		}
		
		return blacklist;
	}
}
