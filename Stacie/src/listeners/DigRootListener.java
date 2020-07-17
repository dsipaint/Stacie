package listeners;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;

public class DigRootListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		//Digital root calculator
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "dgrt"))
		{
			if(arguments.length == 1)
				e.getChannel().sendMessage("No number specified.").queue();
			else //more than 1 argument
			{
				if(arguments[1].matches("\\d+"))
					e.getChannel().sendMessage("The digital root of " + arguments[1] + " is " + Integer.toString(digRoot(arguments[1]))).queue();
				else
					e.getChannel().sendMessage("That is not an integer!").queue();
			}
			
			return;
		}
	}
	
	public static int digRoot(String num)
	{
		char[] digits = num.toCharArray();
		
		if(digits.length < 2) //i.e. if number < 10
			return Integer.parseInt(num);
		else
		{
			int returnNum = 0;
			
			for(int i = 0; i < digits.length; i++)
				returnNum += Integer.parseInt(Character.toString(digits[i]));
			
			return digRoot(Integer.toString(returnNum));
		}
	}
}
