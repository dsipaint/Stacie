package listeners;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.Tools;

public class SendListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "send") && Tools.isStaff(e.getMember()))
		{
			e.getMessage().delete().queue();
			e.getChannel().sendMessage(msg.substring(arguments[0].length())).queue();
		}
	}
}
