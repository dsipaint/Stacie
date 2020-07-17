package listeners;
import main.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.Tools;

public class StopListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		//staff type ">stop"
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "stop") && Tools.isStaff(e.getMember()))
		{
			e.getChannel().sendMessage("**STACIE SLEEPS ONCE MORE**").queue();
			Main.jda.shutdown();
			System.exit(0);
		}
	}
}
