package sv.project_titanic.view;

import java.awt.Color;
import java.awt.Rectangle;

/**Representation of a cingle coordinate on the game board.*/
class Cell extends Rectangle {
	private Color color;

	/**Create a new cell. Defaults to the color of an empty coordinate.*/
	public Cell() {
		super();

		this.color = GUI.EMPTY_COLOR;
	}

	/**
	 * @return the color of this cell
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to change this cell to
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}

