package utils;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Tools
{
	public static void giveHouseRole(Team team, Member m)
	{
		Guild server = m.getGuild();
		server.addRoleToMember(m, server.getRoleById(team.getRoleId())).queue();
	}
	
	public static boolean isStaff(Member m)
	{		
		//check user roles for staff
		List<Role> usrRoles = m.getRoles();
		if(usrRoles.contains(m.getGuild().getRoleById(Constants.admin1))
				|| usrRoles.contains(m.getGuild().getRoleById(Constants.admin2))
				|| usrRoles.contains(m.getGuild().getRoleById(Constants.mod_admin))
				|| usrRoles.contains(m.getGuild().getRoleById(Constants.mod))
				|| usrRoles.contains(m.getGuild().getRoleById(Constants.helper)))
					return true;
		
		return false;
	}
	
	public static void createLog(User u, String logDesc, String imgurl, boolean added)
	{
		String usertag = u.getAsTag();
		String avatarurl = u.getAvatarUrl();
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription(logDesc);
		
		if(added)
			eb.setColor(new Color(21,214,133)); //green
		else
			eb.setColor(new Color(237, 93, 88)); //red
		
		eb.setAuthor(usertag, null, avatarurl);
		if(imgurl != null)
			eb.setImage(imgurl);
		
		u.getJDA().getTextChannelById(Constants.log_channel).sendMessage(eb.build()).queue();
	}
	
	public static Member getClosestMember(Guild guild, String request)
	{
		for(Member m : guild.getMembers())
		{
			if(m.getNickname().startsWith(request))
				return m;
			if(m.getUser().getName().startsWith(request))
				return m;
		}
		
		return null;
	}
}
