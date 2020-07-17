package utils;

public class Puzzle
{
	
	/*
	 * This class represents a puzzle in the system, and makes referencing puzzle data easier
	 */
	private String name, imgurl, answer, desc;
	private int points;
	
	public Puzzle()
	{
		this.name = "Default name";
		this.imgurl = "";
		this.answer = "Default answer";
		this.points = 0;
		this.desc = "Default description";
	}
	
	public Puzzle(String name, String imgurl, String answer, int points, String desc)
	{
		this.name = name;
		this.imgurl = imgurl;
		this.answer = answer;
		this.points = points;
		this.desc = desc;
	}
			
	public String toString() //returns the same output the database has stored
	{
		String output = name + "\t";
		output += (imgurl.length() > 0 ? imgurl + "\t" : "");
		output += answer + "\t";
		output += points;
		output += (desc.length() > 0 ? "\t" + desc : "");
		
		return output;
	}
	
	public void removeImage()
	{
		imgurl = "";
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImgurl()
	{
		return imgurl;
	}

	public void setImgurl(String imgurl)
	{
		this.imgurl = imgurl;
	}

	public String getAnswer()
	{
		return answer;
	}

	public void setAnswer(String answer)
	{
		this.answer = answer;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}
}
