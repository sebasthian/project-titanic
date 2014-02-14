package sv.project_titanic.model;

public class Player
{
	private String playerName;
	
	
	public Player(String name)
	{
		this.playerName = name;
	}
	/*
	 * Sets playername
	 */
	public void setPlayerName(String s)
	{
		playerName = s;
	}
	/*
	 * Return playername.
	 */
	public String getPlayerName()
	{
		return playerName;
	}
}
