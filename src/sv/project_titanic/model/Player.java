package sv.project_titanic.model;

public class Player {
	private String name;
	
	public Player(String name) {
		this.name = name;
	}

	/*
	 * Sets playername
	 */
	public void setName(String s) {
		name = s;
	}

	/*
	 * Return playername.
	 */
	public String getName() {
		return name;
	}
}
