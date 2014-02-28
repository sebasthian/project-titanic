package sv.project_titanic.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sv.project_titanic.Controller;
import sv.project_titanic.model.Coordinate;

/**A visual representation of a game board.*/
public class Grid extends JPanel implements Observer {
	private final Color SELECTED_MASK = new Color(255, 255, 255, 50);

	private int columnCount;
	private int rowCount;
	private int cellWidth;
	private int cellHeight;
	private List<Cell> cells;
	private Coordinate selectedCell;
	private boolean gridInvalid;

	/**Create a new grid of specified size, with or without highlighting of
	 * cells.
	 *
	 * @param rows the height of the grid
	 * @param columns the width of the grid
	 * @param active Cells are highlighted on mouseover if true.
	 */
	public Grid(int rows, int columns, boolean active) {
		rowCount = rows;
		columnCount = columns;
		cells = new ArrayList<>(columnCount * rowCount);

		for(int i = 0; i < rowCount*columnCount; i++)
			cells.add(new Cell());

		gridInvalid = true;
		selectedCell = null;

		if(active) {
			MouseAdapter mouseListener = new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					selectedCell = translatePosition(e.getPoint());
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					selectedCell = null;
					repaint();
				}
			};

			addMouseMotionListener(mouseListener);
			addMouseListener(mouseListener);
		}
	}

	/**
	 * @return true if a cell is selected
	 */
	public boolean hasSelection() {
		return selectedCell != null;
	}

	/**
	 * @return the coordinates of the selected cell, or null if no cell
	 *         selected
	 */
	public Coordinate getSelectedCell() {
		return selectedCell;
	}

	/**
	 * @param row the row (y-coordinate) of the cell to get
	 * @param column the column (x-coordinate) of the cell to get
	 *
	 * @return the cell at (column, row)
	 */
	public Cell getCell(int row, int column) {
		return cells.get(column + row*columnCount);
	}

	/**
	 * @param coord the coordinates of the cell to get
	 *
	 * @return the cell at coord
	 */
	public Cell getCell(Coordinate coord) {
		return getCell(coord.getY(), coord.getX());
	}

	/**Update this grid when an observed object changes. Observable objects
	 * that have registered this grid as an observer will call this method
	 * when they change.
	 *
	 * @param o the observable that has changed
	 * @param arg the data that has changed. Should be an int array on the
	 *            form [x, y, status], representing a changed cell.
	 */
	public void update(Observable o, Object arg) {
		int[] message = (int[])arg;

		int column = message[0];
		int row = message[1];
		int status = message[2];

		Cell cell = getCell(row, column);

		cell.setColor(GUI.COLOR_MAP.get(status));

		repaint();
	}

	/**Translate a pixel position into a logical coordinate on this grid. Used
	 * for translating mouse event positions.
	 *
	 * @param p the pixel coordinates to translate
	 *
	 * @return the logical coordinates corresponding to the pixel position
	 */
	public Coordinate translatePosition(Point p) {
		int column = p.x / cellWidth;
		if(column >= columnCount)
			column = columnCount-1;

		int row = p.y / cellHeight;
		if(row >= rowCount)
			row = rowCount-1;

		return new Coordinate(column, row);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}

	@Override
	public void invalidate() {
		gridInvalid = true;
		selectedCell = null;
		super.invalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();

		if(gridInvalid) {
			resizeCells();
			gridInvalid = false;
		}

		for(Cell cell : cells) {
			g2d.setColor(cell.getColor());
			g2d.fill(cell);
		}

		if(selectedCell != null) {
			Cell cell = getCell(selectedCell);
			g2d.setColor(SELECTED_MASK);
			g2d.fill(cell);
		}

		g2d.setColor(Color.GRAY);
		for(Cell cell : cells) 
			g2d.draw(cell);

		g2d.dispose();
	}

	/**Resize the Cells of this Grid. Used if this Grid has become invalidated,
	 * by resizing or similar.
	 */
	private void resizeCells() {
		int width = getWidth();
		int height = getHeight();

		cellWidth = width / columnCount;
		cellHeight = height / rowCount;

		int xOffset = (width - (columnCount * cellWidth)) / 2;
		int yOffset = (height - (rowCount * cellHeight)) / 2;

		for(int row = 0; row < rowCount; row++) {
			for(int col = 0; col < columnCount; col++) {
				cells.get(col + row*columnCount).setBounds(
						xOffset + (col * cellWidth),
						yOffset + (row * cellHeight),
						cellWidth,
						cellHeight
						);
			}
		}
	}
}

