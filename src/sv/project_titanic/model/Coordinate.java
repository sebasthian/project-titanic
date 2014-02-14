package sv.project_titanic.model;

public class Coordinate
{
	private int x, y;
	
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	/*
	 * Compares Coordinate object with another. 
	 */
	public boolean equals(Coordinate coord)
	{
		if(coord.getX() == x && (coord.getY() == y))
		{
			return true;
		}
		return false;
	}
	/*
	 * Return X
	 */
	public int getX()
	{
		return x;
	}
	/*
	 * Return Y
	 */
	public int getY()
	{
		return y;
	}
	
}
