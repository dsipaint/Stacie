package listeners;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MiscListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		
		//stacie says "AWW MAN" when someone says "Creeper"
		if(msg.equalsIgnoreCase("Creeper"))
		{
			e.getChannel().sendMessage("AW MAN").queue();
			return;
		}
	}
}
