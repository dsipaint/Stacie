package listeners;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.DataHandler;
import utils.Tools;

public class EloListener extends ListenerAdapter
{
	private Random r;
	private Connection con;
	private DataHandler dh;
	
	public EloListener(DataHandler dh)
	{
		r = new Random();
		con = dh.getConnection();
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] arguments = msg.split(" ");
		
		if(arguments[0].equalsIgnoreCase(Constants.PREFIX + "elo"))
		{
			if(arguments.length == 1) //request a pic
			{
				EmbedBuilder eb = new EmbedBuilder();
				int animalentry = r.nextInt(dh.getResultSize("select * from animals")) + 1;
				String animal_url = dh.retrieveElement("animals", "img_url", animalentry);
				Member user = e.getGuild().getMemberById(dh.retrieveElement("animals", "discord_id", animalentry));
				String userName;
				
				if(user == null) //user no longer exists in the server
					userName = "[REDACTED]";
				else
					userName = user.getEffectiveName();
				
				eb.setTitle("Here is your adorable pet, submitted by " + userName + " (entry #" + (animalentry + 1) + ")!" );
				eb.setImage(animal_url);
				eb.setColor(new Color(76,187,23));
				
				e.getChannel().sendMessage(eb.build()).queue();
			}
			else
			{
				if(arguments[1].equalsIgnoreCase("submit"))
				{
					boolean added = false;
					String imgurl = null;
					
					if(arguments.length == 2)
					{
						if(e.getMessage().getAttachments().size() != 0 && e.getMessage().getAttachments().get(0).isImage())
						{
							imgurl = e.getMessage().getAttachments().get(0).getUrl();
							try
							{
								con.createStatement().executeUpdate("insert into animals (discord_id, img_url) values (\"" + e.getMember().getUser().getId() + "\", \"" + imgurl + "\")");
							}
							catch (SQLException e1)
							{
								e1.printStackTrace();
							}
							added = true;
						}
					}
					else //arguments.length > 2 (3 or more items in command)
					{	
						imgurl = arguments[2];
						if( imgurl.length() < 130 && (imgurl.contains("https://") || imgurl.contains("http://")))//input sanitisation
						{
							try
							{
								con.createStatement().executeUpdate("insert into animals (discord_id, img_url) values (\"" + e.getMember().getUser().getId() + "\", \"" + imgurl + "\")");
							}
							catch (SQLException e1)
							{
								e1.printStackTrace();
							}
							added = true;
						}
					}
					
					if(added)
					{
						e.getChannel().sendMessage("Your pet was added!").queue();
						String logDesc = "Submitted " + imgurl + " to animals.txt (entry " + dh.getResultSize("select * from animals") + ")"; //logging all image submissions to the logs channel
						Tools.createLog(e.getAuthor(), logDesc, imgurl, true);
					}
					else
						e.getChannel().sendMessage("Your pet was not able to be added :(").queue();
					
					return;
				}
				else if(arguments[1].equalsIgnoreCase("remove") && Tools.isStaff(e.getMember()))
				{
					int entryno = -1;
					boolean removed = false;
					String imgurl = null, imgusr = null;
					
					if(arguments.length == 2)
						e.getChannel().sendMessage("No entry specified").queue();
					else //arguments.length > 2
					{	
						if(arguments[2].matches("\\d+")) //we have a positive integer
						{
							entryno = Integer.parseInt(arguments[2]);
							if(entryno > 0 && entryno <= dh.getResultSize("select * from animals")) //within the list
							{
								imgurl = dh.retrieveElement("animals", "img_url", entryno);
								imgusr = dh.retrieveElement("animals", "discord_id", entryno);
								dh.removeElementByEntryNo("animals", entryno);
								removed = true;
							}
						}
					}
					
					if(removed)
					{
						e.getChannel().sendMessage("Image " + arguments[2] + " removed from list.").queue();
						String logDesc = "Removed " + imgurl + " from animals.txt (entry " + arguments[2] + ", submitted by " + e.getGuild().getMemberById(imgusr).getUser().getAsTag() + ")";
						Tools.createLog(e.getAuthor(), logDesc, imgurl, false);
					}
					else
						e.getChannel().sendMessage("Invalid entry number.").queue();
					
					return;
				}
				else if(arguments[1].equalsIgnoreCase("request")) //>elo request
				{
					if(arguments.length >= 3)
					{
						if(arguments[2].matches("\\d+") && arguments[2].length() < 6 && Integer.parseInt(arguments[2]) > 0 && Integer.parseInt(arguments[2]) <= dh.getResultSize("select * from animals")) //  >elo request entrynumber
						{
							EmbedBuilder eb = new EmbedBuilder();
							int animalentry = Integer.parseInt(arguments[2]);
							String animal_url = dh.retrieveElement("animals", "img_url", animalentry);
							Member user = e.getGuild().getMemberById(dh.retrieveElement("animals", "discord_id", animalentry));
							String userName;
							
							if(user == null) //user no longer exists in the server
								userName = "[REDACTED]";
							else
								userName = user.getEffectiveName();
							
							eb.setTitle("Here is your adorable pet, submitted by " + userName + " (entry #" + animalentry + ")!" );
							eb.setImage(animal_url);
							eb.setColor(new Color(76,187,23));
							
							e.getChannel().sendMessage(eb.build()).queue();
						}
						else
							e.getChannel().sendMessage("Invalid Entry!").queue();
						
						return;
					}
				}
				else //after all commands have been checked, last resort is to accept the input as a username
				{
					if(Tools.getClosestMember(e.getGuild(), arguments[2]) != null)
					{
						Member requested_member = Tools.getClosestMember(e.getGuild(), arguments[2]);
						
						int randomno = r.nextInt(dh.getResultSize("select * from animals"
								+ " where discord_id = " + requested_member.getId())) + 1;
						
						try
						{
							ResultSet rs = con.createStatement().executeQuery("select id, img_url from animals"
									+ " where discord_id = " + requested_member.getId());
							rs.absolute(randomno);
							EmbedBuilder eb = new EmbedBuilder();
							eb.setTitle("Here is your adorable pet, submitted by " + requested_member.getEffectiveName()
								+ " (entry #" + rs.getInt("id") + ")!" );
							eb.setImage(rs.getString("img_url"));
							eb.setColor(new Color(76,187,23));
							
							e.getChannel().sendMessage(eb.build()).queue();
						}
						catch (SQLException e1)
						{
							e1.printStackTrace();
						}
					}
					else
						e.getChannel().sendMessage("No user found in this server with that name").queue();
				}
			}
		}
	}
}
