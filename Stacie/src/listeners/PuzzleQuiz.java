package listeners;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import utils.Constants;
import utils.DataHandler;
import utils.Puzzle;
import utils.Team;
import utils.Tools;

public class PuzzleQuiz
{
	
	private static final String 
		rules = "Type the number of the answer you would like",
		question1 = "Black or white?\n  **1. Black**\n  **2. White**",
		question2 = "You are given a code to crack - do you:\n  **1. Post it in a discord server for friends to solve**\n  **2. Keep the code to yourself to solve**",
		question3 = "You are given a string of numbers and letters - what is your first move?\n  **1. Try to identify a pattern**\n  **2. Google it**",
		question4 = "When are you most productive?\n **1. Mid-afternoon**\n  **2. Early morning**\n  **3. Late at night**",
		question5 = "What do you value more?\n **1. Analytical skills**\n  **2. Creativity**",
		question6 = "My ideal workspace is...\n  **1. At a desk with a computer**\n  **2. In an open space with pens and paper**",
		question7 = "You're about to meet a new person- do you:\n  **1. Go in for a hug**\n  **2. Wait to be introduced**\n  **3. Shake their hand**",
		question8 = "What is your preferred drink?\n  **1. Water**\n  **2. Fruit Juice**\n  **3. A soft drink**",
		question9 = "What is your favourite animal?\n  **1. Dog**\n  **2. Cat**\n  **3. Bird**\n **4. Snake**",
		question10 = "What is your favourite animal?\n  **1. Dog**\n  **2. Cat**\n  **3. Bird**\n **4. Snake**",
		question11 = "What is your favourite animal?\n  **1. Dog**\n  **2. Cat**\n  **3. Bird**\n **4. Snake**",
		solarisMsg = "Congratulations! You have been sorted into Solaris.",
		lumiousMsg = "Congratulations! You have been sorted into Lumious.",
		celestialMsg = "Congratulations! You have been sorted into Celestial.",
		lunarpineMsg = "Congratulations! You have been sorted into Lunarpine.";
	
	private static Connection con;
	private static DataHandler dh;
	public static LinkedList<Puzzle> published_puzzles, unpublished_puzzles;
	public static LinkedList<String> puzzledata; //this should not be accessed for data-handling -- it's messy, use the published_published_puzzles LinkedList
	static final String lunarpine_pfp_location = "https://cdn.discordapp.com/attachments/546683659218845727/598548589290979340/image1.jpg",
	lumious_pfp_location = "https://cdn.discordapp.com/attachments/546683659218845727/598548589290979344/image3.jpg",
	solaris_pfp_location = "https://cdn.discordapp.com/attachments/546683659218845727/598548589861666855/image0.jpg",
	celestial_pfp_location = "https://cdn.discordapp.com/attachments/546683659218845727/598548590360657981/image2.jpg";
	
	public PuzzleQuiz(DataHandler dh)
	{
		con = dh.getConnection();
		this.dh = dh;
		readIntoLinkedLists();
	}
	
	//this method will update the linkedlists with the database data
	public static void readIntoLinkedLists()
	{
		published_puzzles = new LinkedList<Puzzle>();
		unpublished_puzzles = new LinkedList<Puzzle>();
		
		try
		{
			ResultSet rs = con.createStatement().executeQuery("select * from published_puzzles");
			while(rs.next())
			{
				Puzzle p = new Puzzle();
				p.setName(rs.getString("puzzle_title"));
				p.setAnswer(rs.getString("answer"));
				p.setDesc(rs.getString("puzzle_desc"));
				p.setImgurl(rs.getString("img_url"));
				p.setPoints(rs.getInt("points"));
				published_puzzles.add(p);
			}
			
			rs = con.createStatement().executeQuery("select * from unpublished_puzzles");
			while(rs.next())
			{
				Puzzle p = new Puzzle();
				p.setName(rs.getString("puzzle_title"));
				p.setAnswer(rs.getString("answer"));
				p.setDesc(rs.getString("puzzle_desc"));
				p.setImgurl(rs.getString("img_url"));
				p.setPoints(rs.getInt("points"));
				unpublished_puzzles.add(p);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	public static String playQuiz(PrivateMessageReceivedEvent e, String currentStatus, Member m) //String emitted will be new quizStatus variable
	{
		String newStatus = "";
		String msg = e.getMessage().getContentRaw();
		
		switch (currentStatus)
		{
			case "INIT":
				if(e.getMessage().getContentRaw().equalsIgnoreCase(Constants.PREFIX + "quiz"))
				{
					e.getChannel().sendMessage(rules).queue();
					e.getChannel().sendMessage(question1).queue();
					newStatus = "1";
				}
				else
					newStatus = "INIT";
				break;
				
			case "1": //user is up to question 1, for example
				if(isValidAnswer(msg, 2))
				{
					switch(e.getMessage().getContentRaw())
					{
						//Black answer chosen
						case "1":
							e.getChannel().sendMessage(question2).queue();
							newStatus = "2";
							break;
						
						//White answer chosen
						case "2":
							e.getChannel().sendMessage(question3).queue();
							newStatus = "3";
							break;
					}
				}
				else
					newStatus = "1";
				break;	
			
			case "2":
				if(isValidAnswer(msg, 2))
				{
					switch(msg)
					{
						case "1":
							e.getChannel().sendMessage(question4).queue();
							newStatus = "4";
							break;
							
						case "2":
							e.getChannel().sendMessage(question5).queue();
							newStatus = "5";
							break;
					}
				}
				else
					newStatus = "2";
				break;
				
			case "3":
				if(isValidAnswer(msg, 2))
				{
					switch(msg)
					{
						case "1":
							e.getChannel().sendMessage(question5).queue();
							newStatus = "5";
							break;
							
						case "2":
							e.getChannel().sendMessage(question6).queue();
							newStatus = "6";
							break;
					}
				}
				else
					newStatus = "3";
				break;
				
			case "4":
				if(isValidAnswer(msg, 3))
				{
					switch(msg)
					{
						case "1":
							newStatus = "9";
							e.getChannel().sendMessage(question9).queue();
							break;
							
						case "2":
							newStatus = "10";
							e.getChannel().sendMessage(question10).queue();
							break;
							
						case "3":
							newStatus = "7";
							e.getChannel().sendMessage(question7).queue();
							break;
					}
				}
				else
					newStatus = "4";
				break;
				
			case "5":
				if(isValidAnswer(msg, 2))
				{
					switch(msg)
					{
						case "1":
							newStatus = "4";
							e.getChannel().sendMessage(question4).queue();
							break;
							
						case "2":
							newStatus = "8";
							e.getChannel().sendMessage(question8).queue();
							break;
					}
				}
				else
					newStatus = "5";
				break;
				
			case "6":
				if(isValidAnswer(msg, 2))
				{
					switch(msg)
					{
						case "1":
							newStatus = "7";
							e.getChannel().sendMessage(question7).queue();
							break;
							
						case "2":
							newStatus = "8";
							e.getChannel().sendMessage(question8).queue();
							break;
					}
				}
				else
					newStatus = "6";
				break;
				
			case "7":
				if(isValidAnswer(msg, 3))
				{
					switch(msg)
					{
						case "1":
							newStatus = "9";
							e.getChannel().sendMessage(question8).queue();
							break;
							
						case "2":
							newStatus = "11";
							e.getChannel().sendMessage(question11).queue();
							break;
							
						case "3":
							newStatus = "10";
							e.getChannel().sendMessage(question10).queue();
							break;
					}
				}
				else
					newStatus = "7";
				break;
				
			case "8":
				if(isValidAnswer(msg, 3))
				{
					switch(msg)
					{
						case "1":
							newStatus = "10";
							e.getChannel().sendMessage(question10).queue();
							break;
							
						case "2":
							newStatus = "9";
							e.getChannel().sendMessage(question9).queue();
							break;
							
						case "3":
							newStatus = "11";
							e.getChannel().sendMessage(question11).queue();
							break;
					}
				}
				else
					newStatus = "8";
				break;
				
			case "9":
				if(isValidAnswer(msg, 4))
				{
					switch(msg)
					{
						case "1":
							newStatus = "SOLARIS";
							e.getChannel().sendMessage(solarisMsg).queue();
							Tools.giveHouseRole(Team.SOLARIS, m);
							break;
							
						case "2":
							newStatus = "SOLARIS";
							e.getChannel().sendMessage(solarisMsg).queue();
							Tools.giveHouseRole(Team.SOLARIS, m);
							break;
							
						case "3":
							newStatus = "SOLARIS";
							e.getChannel().sendMessage(solarisMsg).queue();
							Tools.giveHouseRole(Team.SOLARIS, m);
							break;
							
						case "4":
							newStatus = "LUMIOUS";
							e.getChannel().sendMessage(lumiousMsg).queue();
							Tools.giveHouseRole(Team.LUMIOUS, m);
							break;
					}
				}
				else
					newStatus = "9";
				break;
				
			case "10":
				if(isValidAnswer(msg, 4))
				{
					switch(msg)
					{
						case "1":
							newStatus = "CELESTIAL";
							e.getChannel().sendMessage(celestialMsg).queue();
							Tools.giveHouseRole(Team.CELESTIAL, m);
							break;
							
						case "2":
							newStatus = "LUMIOUS";
							e.getChannel().sendMessage(lumiousMsg).queue();
							Tools.giveHouseRole(Team.CELESTIAL, m);
							break;
							
						case "3":
							newStatus = "CELESTIAL";
							e.getChannel().sendMessage(celestialMsg).queue();
							Tools.giveHouseRole(Team.CELESTIAL, m);
							break;
							
						case "4":
							newStatus = "LUMIOUS";
							e.getChannel().sendMessage(lumiousMsg).queue();
							Tools.giveHouseRole(Team.LUMIOUS, m);
							break;
					}
				}
				else
					newStatus = "10";
				break;
				
			case "11":
				if(isValidAnswer(msg, 4))
				{
					switch(msg)
					{
						case "1":
							newStatus = "LUNARPINE";
							e.getChannel().sendMessage(lunarpineMsg).queue();
							Tools.giveHouseRole(Team.LUNARPINE, m);
							break;
							
						case "2":
							newStatus = "LUNARPINE";
							e.getChannel().sendMessage(lunarpineMsg).queue();
							Tools.giveHouseRole(Team.LUNARPINE, m);
							break;
							
						case "3":
							newStatus = "LUNARPINE";
							e.getChannel().sendMessage(lunarpineMsg).queue();
							Tools.giveHouseRole(Team.LUNARPINE, m);
							break;
							
						case "4":
							newStatus = "CELESTIAL";
							e.getChannel().sendMessage(celestialMsg).queue();
							Tools.giveHouseRole(Team.CELESTIAL, m);
							break;
					}
				}
				else
					newStatus = "11";
				break;
				
			case "SOLARIS": //nothing happens once they reach an end status
				newStatus = "SOLARIS";
				break;
				
			case "LUMIOUS":
				newStatus = "LUMIOUS";
				break;
				
			case "CELESTIAL":
				newStatus = "CELESTIAL";
				break;
				
			case "LUNARPINE":
				newStatus = "LUNARPINE";
				break;
		}
		
		return newStatus;
	}
	
	private static boolean isValidAnswer(String ans, int noOfAnswers) //for the quiz
	{
		if(ans.matches("\\d+") && Integer.parseInt(ans) > 0 && Integer.parseInt(ans) <= noOfAnswers)
			return true;
		
		return false;
	}
		
	public static boolean removePuzzle(String entryno)
	{
		if(entryno.matches("\\d+") && entryno.length() < 6 && Integer.parseInt(entryno) <= published_puzzles.size()) //valid number
		{
			published_puzzles.remove(Integer.parseInt(entryno) - 1);
			dh.removeElementByEntryNo("published_puzzles", Integer.parseInt(entryno));
			return true;
		}
		
		return false;
	}
	
	public static String showPuzzleList()
	{
		if(published_puzzles.size() > 0)
		{
			String list = "**";
			
			for(int i = 0; i < published_puzzles.size(); i++)
				list += (i + 1) + ". " + published_puzzles.get(i).getName() + "\n";
			
			list += "**";
			return list;
		}
		
		return "no current puzzles";
	}
	
	
	public static boolean submitAnswer(GuildMessageReceivedEvent e)
	{
		String[] components = e.getMessage().getContentRaw().split(" ");
		if(!(components[2].matches("\\d+") && Integer.parseInt(components[2]) > 0 && Integer.parseInt(components[2]) <= published_puzzles.size()))
			return false;
		Puzzle actual_puzzle = published_puzzles.get(Integer.parseInt(components[2]) - 1);
		String userAnswer = "";
		for(int i = 3; i < components.length; i++) //support for answers of more than 1 word
			userAnswer += " " + components[i];
		
		if(userAnswer.trim().equalsIgnoreCase(actual_puzzle.getAnswer()))
		{		
			Puzzle p = published_puzzles.get(Integer.parseInt(components[2]) - 1);
			
			e.getJDA().getTextChannelById(Constants.puzzle_channel).sendMessage("**" 
					+ e.getMember().getAsMention() + " just solved puzzle " + components[2] 
					+ " \"" + p.getName() 
					+ "\"! The answer was " + p.getAnswer() + "!**").queue();
			//log the puzzle being answered
			Tools.createLog(e.getAuthor(),
					"Solved puzzle " + components[2] + " (answer:" + p.getAnswer() + ")",
					null, true);
			e.getJDA().getTextChannelById(Constants.log_channel).sendMessage(showPuzzle(p).build()).queue();
			//remove the solved puzzle
			published_puzzles.remove(Integer.parseInt(components[2]) - 1);
			dh.removeElementByEntryNo("published_puzzles", Integer.parseInt(components[2]));
			
			return true;
		}
		else
			e.getChannel().sendMessage("Incorrect answer :/").queue();
		
		return false;
	}
	
	public static EmbedBuilder showPuzzle(Puzzle puzzle) //creates an embed with the puzzle in
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(142, 18, 219));
		
		eb.setTitle(puzzle.getName()); //title is name of puzzle
		
		if(puzzle.getImgurl().contains("https://") || puzzle.getImgurl().contains("http://"))
		{
			//an image
			if(puzzle.getImgurl().contains(".jpg") || puzzle.getImgurl().contains(".jpeg") || puzzle.getImgurl().contains(".png") || puzzle.getImgurl().contains(".JPG"))
				eb.setImage(puzzle.getImgurl());
			else //not an image but still an attatchment
				eb.addField("", puzzle.getImgurl(), true);
		}
		
		if(puzzle.getDesc().length() > 0)
			eb.setDescription(puzzle.getDesc());
		
		return eb;
	}
	
	public static String showPuzzleAnswer(String entryno)
	{
		if(entryno.matches("\\d+") && Integer.parseInt(entryno) > 0 && Integer.parseInt(entryno) <= published_puzzles.size())
		{
			return published_puzzles.get(Integer.parseInt(entryno) - 1).getAnswer();
		}
		
		return null;
	}
	
	
	public static String getTeam(String userID)
	{
		String teamname = null;
		
		try
		{
			ResultSet rs = con.createStatement().executeQuery("select teamname from userteams where discord_id = \"" + userID + "\"");
			rs.absolute(1);
			teamname = rs.getString("teamname");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return teamname;
	}
	
	public static EmbedBuilder showAllAnswers()
	{
		EmbedBuilder eb = new EmbedBuilder();
			
		eb.setTitle("Puzzle Answers");
		String answers = "";
			
		for(int i = 1; i <= published_puzzles.size(); i++)
			answers += i + ". " + showPuzzleAnswer(Integer.toString(i)) + "\n";
			
		eb.addField("", answers, true);
			
		return eb;
	}
}
