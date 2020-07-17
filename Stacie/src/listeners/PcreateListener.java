package listeners;

import java.sql.Connection;
import java.sql.SQLException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.DataHandler;
import utils.Puzzle;

public class PcreateListener extends ListenerAdapter
{
	private Connection con;
	
	public PcreateListener(DataHandler dh)
	{
		con = dh.getConnection();
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "pcreate"))
		{
			if(arguments.length >= 2)
			{
				//>pcreate new
				if(arguments[1].equalsIgnoreCase("new"))
				{
					try
					{
						Puzzle p = new Puzzle();
						con.createStatement().executeUpdate("insert into unpublished_puzzles (puzzle_title, img_url, answer, points, puzzle_desc) values ("
								+ "\"" + p.getName() + "\", "
								+ "\"" + p.getImgurl() + "\", "
								+ "\"" + p.getAnswer() + "\", "
								+ p.getPoints() + ", "
								+ "\"" + p.getDesc() + "\")");
						PuzzleQuiz.unpublished_puzzles.add(p);
						e.getChannel().sendMessage("New puzzle created.").queue();
					}
					catch(SQLException e1)
					{
						e1.printStackTrace();
					}
				}
				
				//>pcreate details
				if(arguments[1].equalsIgnoreCase("details"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
					{
						Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
						EmbedBuilder eb = PuzzleQuiz.showPuzzle(p);
						e.getChannel().sendMessage(eb.build()).queue();
						e.getChannel().sendMessage("Answer: " + p.getAnswer()).queue();
					}
				}
				
				//>pcreate list
				if(arguments[1].equalsIgnoreCase("list"))
				{
					if(PuzzleQuiz.unpublished_puzzles.size() > 0)
					{
						String list = "**";
						
						for(int i = 0; i < PuzzleQuiz.unpublished_puzzles.size(); i++)
							list += (i + 1) + ". " + PuzzleQuiz.unpublished_puzzles.get(i).getName() + "\n";
						
						list += "**";
						
						e.getChannel().sendMessage(list).queue();
					}
				}
				
				//>pcreate remove
				if(arguments[1].equalsIgnoreCase("remove"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
					{
						try
						{
							Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
							con.createStatement().executeUpdate("delete from unpublished_puzzles where puzzle_title = \"" + p.getName() + "\"");
							PuzzleQuiz.unpublished_puzzles.remove(p);
							e.getChannel().sendMessage("Puzzle removed.").queue();
						}
						catch(SQLException e1)
						{
							e1.printStackTrace();
						}

					}
				}
				
				//>pcreate settitle
				if(arguments[1].equalsIgnoreCase("setTitle"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							String title = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								title += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update unpublished_puzzles "
										+ "set puzzle_title = \"" + title + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setName(title);
								e.getChannel().sendMessage("Puzzle title set.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
				}
				
				//>pcreate setimage
				if(arguments[1].equalsIgnoreCase("setImage"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							try
							{
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update unpublished_puzzles set img_url = \"" + arguments[3] + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setImgurl(arguments[3]);
								e.getChannel().sendMessage("Puzzle image set.").queue();
								 //no input sanitisation occurs for the url here, but this is checked in the showPuzzle method
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
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update unpublished_puzzles set img_url = \"" + p.getImgurl() + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setImgurl(e.getMessage().getAttachments().get(0).getUrl());
								e.getChannel().sendMessage("Puzzle image set.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
				
				//>pcreate removeimage
				if(arguments[1].equalsIgnoreCase("removeimage"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
					{
						try
						{
							Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
							con.createStatement().executeUpdate("update unpublished_puzzles set img_url = \"\" where puzzle_title = \"" + p.getName() + "\"");
							PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).removeImage();
							e.getChannel().sendMessage("Puzzle image removed.").queue();
						}
						catch(SQLException e1)
						{
							e1.printStackTrace();
						}
					}
				}
				
				//>pcreate setanswer
				if(arguments[1].equalsIgnoreCase("setAnswer"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							String answer = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								answer += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update unpublished_puzzles set answer = \"" + answer + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setAnswer(answer);
								e.getChannel().sendMessage("Puzzle answer set.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
				}
				
				//>pcreate setpoints
				if(arguments[1].equalsIgnoreCase("setpoints"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							if(arguments[3].matches("\\d+")) //valid number of points
							{
								try
								{
									Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
									con.createStatement().executeUpdate("update unpublished_puzzles set points = " + Integer.parseInt(arguments[3]) + " where puzzle_title = \"" + p.getName() + "\"");
									PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setPoints(Integer.parseInt(arguments[3]));
									e.getChannel().sendMessage("Puzzle points set.").queue();
								}
								catch(SQLException e1)
								{
									e1.printStackTrace();
								}

							}
						}
					}
				}
				
				//>pcreate setdesc
				if(arguments[1].equalsIgnoreCase("setdesc"))
				{
					if(arguments.length >= 4)
					{
						if(arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							String desc = arguments[3];
							for(int i = 4; i < arguments.length; i++)
								desc += " " + arguments[i];
							
							try
							{
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("update unpublished_puzzles set puzzle_desc = \"" + desc + "\" where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1).setDesc(desc);
								e.getChannel().sendMessage("Puzzle description set.").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
				
				//>pcreate remove
				if(arguments[1].equalsIgnoreCase("remove"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+"))
					{
						if(Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
						{
							try
							{
								Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
								con.createStatement().executeUpdate("delete from unpublished_puzzles where puzzle_title = \"" + p.getName() + "\"");
								PuzzleQuiz.unpublished_puzzles.remove(Integer.parseInt(arguments[2]) - 1);
								e.getChannel().sendMessage("Puzzle ").queue();
							}
							catch(SQLException e1)
							{
								e1.printStackTrace();
							}

						}
					}
				}
				
				//>pcreate publish
				if(arguments[1].equalsIgnoreCase("publish"))
				{
					if(arguments.length >= 3 && arguments[2].matches("\\d+") && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= PuzzleQuiz.unpublished_puzzles.size())
					{
						Puzzle p = PuzzleQuiz.unpublished_puzzles.get(Integer.parseInt(arguments[2]) - 1);
						PuzzleQuiz.published_puzzles.add(p);
						PuzzleQuiz.unpublished_puzzles.remove(Integer.parseInt(arguments[2]) - 1); //no longer unpublished
						try
						{
							con.createStatement().executeUpdate("insert into published_puzzles (puzzle_title, img_url, answer, points, puzzle_desc) values (\""
									+ p.getName() + "\", \"" + p.getImgurl() + "\", \"" + p.getAnswer() + "\", " + p.getPoints() + ", \"" + p.getDesc() + "\")");
							con.createStatement().executeUpdate("delete from unpublished_puzzles where puzzle_title = \"" + p.getName() + "\"");
						}
						catch (SQLException e1)
						{
							e1.printStackTrace();
						}
						
						e.getChannel().sendMessage("Your puzzle was published.").queue();
						e.getChannel().sendMessage(PuzzleQuiz.showPuzzle(p).build()).queue();
						e.getJDA().getTextChannelById(Constants.puzzle_channel).sendMessage(
								e.getJDA().getGuildById(Constants.guild).getRoleById(Constants.puzzler_role).getAsMention() 
								+ " A new puzzle has been released! Use \">puzzle answer " + PuzzleQuiz.published_puzzles.size() 
								+ " [answer]\" to win points for your team!").queue();
						e.getJDA().getTextChannelById(Constants.puzzle_channel).sendMessage(PuzzleQuiz.showPuzzle(p).build()).queue();
					}
				}
			}
		}
	}
}
