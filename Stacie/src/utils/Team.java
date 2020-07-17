package utils;

public enum Team
{
	CELESTIAL("Celestial", "650187928164761625"),
	SOLARIS("Solaris", "650188401504419840"),
	LUNARPINE("Lunarpine", "650188166183124992"),
	LUMIOUS("Lumious", "650188350141235202");
	
	private String name, roleid;
	
	Team(String name, String roleid)
	{
		this.name = name;
		this.roleid = roleid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getRoleId()
	{
		return roleid;
	}
}