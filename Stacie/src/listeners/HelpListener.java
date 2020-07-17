package listeners;
import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.Tools;

public class HelpListener extends ListenerAdapter
{
	private final String[] COMMANDS = {"						**>elo**    delivers a cute pet picture to you",
			"						**>elo submit [image/ image url]**    submits your picture to the collection of pets",
			"                       **>elo request [entry no]**  Request a specific picture that has been submitted, using its entry number",
			"						**>elo [username]** Request a picture submitted by a particular user",
			"						**>quiz** (use in DMs) Initiates a quiz which will sort you into a team (see #faq)",
			"                       **>dgrt [number]** calculates the digital root of a given number",
			"                       **>puzzle list** Lists the names all the available puzzles that can be solved",
			"                       **>puzzle details [puzzle number]**  Shows the full puzzle listed on the puzzle list",
			"                       **>puzzle answer [puzzle number] [answer]** Answer the puzzle with the given list number"};
	
	private final String[] STAFF_COMMANDS = {"**>stop**  Stop the program should it destroy us all",
			"						**>elo remove [entry no]**	Remove submitted animals",
			"                       **>puzzle remove [puzzle entry number]** Staff-only command to remove a puzzle in the list of puzzles",
			"                       **>puzzle viewanswers**  Shows answers for all puzzles in the system",
			"                       **>puzzle viewanswers [puzzle number]** Shows the answer for a specified puzzle",
			"                       **>puzzle edittitle [puzzle number] [new title]** (use in DMs) Edit the title of the selected puzzle",
			"                       **>puzzle editimage [puzzle number] [image url]** (use in DMs) Edit the picture of the selected puzzle (replace the image url with an image attachment for the same result)",
			"                       **>puzzle editanswer [puzzle number] [new answer]** (use in DMs) Edit the answer of the selected puzzle",
			"                       **>puzzle editdesc [puzzle number] [new description]** (use in DMs) Edit the description of the selected puzzle",
			"                       **>puzzle removeimage [puzzle number]** (use in DMs) Remove the set image of the selected puzzle",
			"                       **>pcreate settitle [puzzle number] [new title]** (use in DMs) Edit the title of the selected puzzle",
			"                       **>pcreate setimage [puzzle number] [image url]** (use in DMs) Edit the picture of the selected puzzle (replace the image url with an image attachment for the same result)",
			"                       **>pcreate setanswer [puzzle number] [new answer]** (use in DMs) Edit the answer of the selected puzzle",
			"                       **>pcreate setdesc [puzzle number] [new description]** (use in DMs) Edit the description of the selected puzzle",
			"                       **>pcreate removeimage [puzzle number]** (use in DMs) Remove the set image of the selected puzzle",
			"                       **>pcreate publish [puzzle number]** (use in DMs) Release the selected puzzle to the server",
			"                       **>pcreate remove** (use in DMs) Delete the unpublished puzzle",
			"                       **>send [message]**  Sends a message through Stacie to the channel it is used in",
			"						**>module [enable/disable] [module name]** Enables or disables a given component of Stacie"};
	
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		
		if(msg.equalsIgnoreCase(Constants.PREFIX + "help"))
		{
			 //deletes the command and DMs the person the commands instead
			
			e.getMessage().delete().queue();
			
			String commandbatch = "";
			int count = 0;
			
			for(int i = 1; i < COMMANDS.length; i++)
			{
				commandbatch += COMMANDS[i - 1] + "\n\n";
				if(i%10 == 0) //this is why i is initialised at 1 instead of 0
				{
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(89,152,255));
					if(i == 10) //only title the first embed
						eb.setAuthor("Commands: ");
						
					eb.setDescription(commandbatch);
					
					e.getAuthor().openPrivateChannel().queue( (channel) -> 
					{
						channel.sendMessage(eb.build()).queue();
					});
					
					commandbatch = "";
				}
				count++;
			}
			
			if(COMMANDS.length%10 > 0) //above loop only loops terms in batches of 10, we have to account for if there is not a multiple of 10 commands
			{
				for(; count < COMMANDS.length; count++)
					commandbatch += COMMANDS[count] + "\n\n";
			
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(new Color(89,152,255));
				
				eb.setDescription(commandbatch);
				
				e.getAuthor().openPrivateChannel().queue( (channel) -> 
				{
					channel.sendMessage(eb.build()).queue();
				});
			}
			
			if(Tools.isStaff(e.getMember()))
			{
				commandbatch = "";
				count = 0;
				
				for(int i = 1; i < STAFF_COMMANDS.length; i++)
				{
					commandbatch += STAFF_COMMANDS[i - 1] + "\n\n";
					if(i%10 == 0) //this is why i is initialised at 1 instead of 0
					{
						
						EmbedBuilder eb = new EmbedBuilder();
						eb.setColor(new Color(89,152,255));
						if(i == 10) //only title the first embed
							eb.setAuthor("Staff commands: ");
							
						eb.setDescription(commandbatch);
						
						e.getAuthor().openPrivateChannel().queue( (channel) -> 
						{
							channel.sendMessage(eb.build()).queue();
						});
						
						commandbatch = "";
					}
					count++;
				}
				
				if(STAFF_COMMANDS.length%10 > 0) //above loop only loops terms in batches of 10, we have to account for if there is not a multiple of 10 commands
				{
					for(; count < STAFF_COMMANDS.length; count++)
						commandbatch += STAFF_COMMANDS[count] + "\n\n";
				
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(new Color(89,152,255));
					
					eb.setDescription(commandbatch);
					
					e.getAuthor().openPrivateChannel().queue( (channel) -> 
					{
						channel.sendMessage(eb.build()).queue();
					});
				}
			}
			return;
		}
	}
}
