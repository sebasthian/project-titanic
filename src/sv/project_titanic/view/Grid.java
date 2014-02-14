package sv.project_titanic.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sv.project_titanic.Controller;
import sv.project_titanic.model.Coordinate;

import javax.swing.JOptionPane;

public class Grid extends JPanel implements Observer {
    private int columnCount;
    private int rowCount;
    private int cellWidth;
    private int cellHeight;
    private List<Cell> cells;
    private Coordinate selectedCell;
    private boolean gridInvalid;
    private final Color SELECTED_MASK = new Color(255, 255, 255, 50);

    public Grid(int rows, int columns) {
        rowCount = rows;
        columnCount = columns;
        cells = new ArrayList<>(columnCount * rowCount);

        for(int i = 0; i < rowCount*columnCount; i++)
            cells.add(new Cell());

        gridInvalid = true;
        selectedCell = null;
    }

    public Grid(int rows, int columns, final Controller controller) {
        this(rows, columns);

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

            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedCell != null) {
                    //JOptionPane.showMessageDialog(
                    //    getTopLevelAncestor(),
                    //    "Cell (" + selectedCell.x + ", " +
                    //    selectedCell.y + ") clicked."
                    //);
                    controller.shoot(selectedCell);
                }
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public void update(Observable o, Object arg) {
        int[] message = (int[])arg;

        int x = message[0];
        int y = message[1];
        int status = message[2];

        Cell cell = getCell(y, x);

        switch(status) {
            case 0: cell.setColor(GUI.EMPTY_COLOR);
                    break;
            case 1: cell.setColor(GUI.MISS_COLOR);
                    break;
            case 2: cell.setColor(GUI.SHIP_COLOR);
                    break;
            case 3: cell.setColor(GUI.HIT_COLOR);
                    break;
            case 4: cell.setColor(GUI.SUNK_COLOR);
                    break;
        }
    }

    public Cell getCell(int row, int col) {
        return cells.get(col + row*columnCount);
    }

    public Cell getCell(Coordinate coord) {
        return getCell(coord.getY(), coord.getX());
    }

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

