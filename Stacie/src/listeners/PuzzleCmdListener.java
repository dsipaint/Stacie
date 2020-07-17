package listeners;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.Puzzle;
import utils.Tools;

public class PuzzleCmdListener extends ListenerAdapter
{
	private boolean deleteOwnMessage;
	private long delay;//DO NOT MANUALLY SET THESE VARIABLES, USE THE deleteOwnMessageAfter METHOD
	private TimeUnit unit;
	
	public PuzzleCmdListener()
	{
		deleteOwnMessage = false;
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		//stacie responses
		if(e.getAuthor().equals(e.getJDA().getSelfUser()))
		{
			if(deleteOwnMessage) //this is set to true for stacie responses to be deleted. It is set to true through deleteOwnMessageAfter
			{
				deleteOwnMessage = false; //bc this should only be used for one responce at a time
				e.getChannel().deleteMessageById(e.getMessageId()).queueAfter(delay, unit);
			}
			
			return; //stacie shouldn't respond to her own commands
		}
		
		//puzzle command
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "puzzle") || arguments[0].equalsIgnoreCase(Constants.PREFIX + "p"))
		{
			
			if(arguments.length >= 2)
			{				
				//  >puzzle list
				if(arguments[1].equalsIgnoreCase("list"))
				{
					String list = PuzzleQuiz.showPuzzleList();
					e.getChannel().sendMessage(list).queue();
					return;
				}
				
				//  >puzzle viewanswers (can only use the command if in staff botcommands)
				if(arguments[1].equalsIgnoreCase("viewanswers") && Tools.isStaff(e.getMember()) && e.getChannel().getId().equals(Constants.bot_cmds_channel))
				{
					if(arguments.length == 2)
					{
						e.getChannel().sendMessage(PuzzleQuiz.showAllAnswers().build()).queue();
						e.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
						deleteOwnMessageAfter(10, TimeUnit.SECONDS);
						return;
					}
					if(arguments.length > 2)
					{
						//  >puzzle viewanswers [puzzle entry]
						if(PuzzleQuiz.showPuzzleAnswer(arguments[2]) != null)
						{
							e.getChannel().sendMessage("Answer for puzzle " + arguments[2] + ": " + PuzzleQuiz.showPuzzleAnswer(arguments[2])).queue();
							e.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
							deleteOwnMessageAfter(10, TimeUnit.SECONDS);
							return;
						}
					}
				}
				
				//  >puzzle remove [entry number]
				if(arguments[1].equalsIgnoreCase("remove") && arguments.length == 3 && Tools.isStaff(e.getMember()))
				{
					Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
					if(PuzzleQuiz.removePuzzle(arguments[2]))
					{
						e.getChannel().sendMessage("Puzzle " + arguments[2] + " removed").queue();
						String logDesc = "Removed puzzle " + arguments[2] + " (answer: " + p.getAnswer() + ")";
						Tools.createLog(e.getAuthor(), logDesc, null, false);
						e.getJDA().getTextChannelById(Constants.log_channel).sendMessage(PuzzleQuiz.showPuzzle(p).build()).queue();
						return;
					}
				}
				
				//  >puzzle details [entry no]
				if(arguments[1].equalsIgnoreCase("details") && arguments.length > 2)
				{
					if(arguments[2].matches("\\d+") && arguments[2].length() < 6 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size() && Integer.parseInt(arguments[2]) > 0)
					{
						EmbedBuilder eb = PuzzleQuiz.showPuzzle(PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1));
						e.getChannel().sendMessage(eb.build()).queue();
						return;
					}
				}
				
				//  >puzzle answer [number] [answer]
				if(arguments[1].equalsIgnoreCase("answer") && e.getChannel().getId().equals(Constants.puzzle_channel))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size())
						{
							if(PuzzleQuiz.submitAnswer(e))
								e.getMessage().delete().queue();
							
							return;
						}
					}
				}
			}
		}
	}
	
	private void deleteOwnMessageAfter(long delay, TimeUnit unit)
	{
		deleteOwnMessage = true;
		this.delay = delay;
		this.unit = unit;
	}
}
