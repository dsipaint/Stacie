package listeners;
import java.sql.Connection;
import java.sql.SQLException;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.DataHandler;
import utils.Puzzle;

public class PuzzleEditListener extends ListenerAdapter
{	
	private Connection con;
	
	public PuzzleEditListener(DataHandler dh)
	{
		con = dh.getConnection();
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
				
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "puzzle"))
		{
			if(arguments.length >= 2)
			{
				//>puzzle edittitle
				if(arguments[1].equalsIgnoreCase("editTitle"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size())
						{
							String title = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								title += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update published_puzzles set puzzle_title = \"" + title + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1).setName(title);
								e.getChannel().sendMessage("Puzzle title updated.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
				}
				
				//>puzzle editimage
				if(arguments[1].equalsIgnoreCase("editImage"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size())
						{
							try
							{
								Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update published_puzzles set img_url = \"" + arguments[3] + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1).setImgurl(arguments[3]);
								e.getChannel().sendMessage("Puzzle image updated.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
					else if(arguments.length >= 3 && e.getMessage().getAttachments().size() > 0)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							try
							{
								String url = e.getMessage().getAttachments().get(0).getUrl();
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update published_puzzles set img_url = \"" + url + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setImgurl(url);
								e.getChannel().sendMessage("Puzzle Image updated.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
				
				//>puzzle removeimage
				if(arguments[1].equalsIgnoreCase("removeimage"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
					{
						try
						{
							Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
							con.createStatement().executeUpdate("update published_puzzles set img_url = \"\" where puzzle_title = \"" + p.getName() + "\"");
							PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1).removeImage();
							e.getChannel().sendMessage("Puzzle image removed.").queue();
						}
						catch(SQLException e1)
						{
							e1.printStackTrace();
						}

					}
				}
				
				//>puzzle editanswer
				if(arguments[1].equalsIgnoreCase("editAnswer"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size())
						{
							String answer = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								answer += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update published_puzzles set answer = \"" + answer + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1).setAnswer(answer);
								e.getChannel().sendMessage("Puzzle answer updated.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
				}
								
				//>puzzle editdesc
				if(arguments[1].equalsIgnoreCase("editDesc"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.published_puzzles.size())
						{
							String desc = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								desc += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update published_puzzles set puzzle_desc = \"" + desc + "\" where puzzle_name = \"" + p.getName() + "\"");
								PuzzleQuiz.published_puzzles.get(Integer.parseInt(arguments[2]) - 1).setDesc(desc);
								e.getChannel().sendMessage("Puzzle description updated.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
