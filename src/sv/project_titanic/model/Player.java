package sv.project_titanic.model;

import java.util.Observable;
import java.io.Serializable;

/**Class for handling player data.*/
public class Player extends Observable implements Serializable {
	private String name;
	private boolean local;
	
	/**Create a new player.
	 *
	 * @param name the name of the Player.
	 */
	public Player(String name, boolean local) {
		this.name = name;
		this.local = local;
	}

	/**@param name a new name for this player.*/
	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers(name);
	}

	/**@return the name of this player.*/
	public String getName() {
		return name;
	}

	/**@return true if this is a local player*/
	public boolean isLocal() {
		return local;
	}
}
