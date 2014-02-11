package sv.project_titanic.view;

import sv.project_titanic.model.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.border.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI extends JFrame {
    public final static Color SHIP_COLOR = Color.BLACK;
    public final static Color EMPTY_COLOR = Color.BLUE;
    public final static Color MISS_COLOR = new Color(100, 100, 255);
    public final static Color HIT_COLOR = Color.RED;
    public final static Color SUNK_COLOR = Color.GREEN;

    private Grid homeGrid;
    private Grid awayGrid;
    private JLabel homePlayer;
    private JLabel awayPlayer;
    private JLabel turnMessage;

    public GUI(Board homeBoard, Player homePlayer, Player awayPlayer) {
        homeGrid = new Grid(10, 10, false);
        awayGrid = new Grid(10, 10, true);

        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                if(homeBoard.getFieldStatus(col, row) == 2)
                    homeGrid.getCell(row, col).setColor(SHIP_COLOR);
            }
        }

        //this.homePlayer = new JLabel(homePlayer.getName());
        //this.awayPlayer = new JLabel(awayPlayer.getname());
        this.homePlayer = new JLabel("player 1");
        this.awayPlayer = new JLabel("player 2");
        turnMessage = new JLabel("Your turn");

        buildFrame();
    }

    private void buildFrame() {
        setTitle("Project Titanic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel statusArea = new JPanel(new BorderLayout());
        statusArea.add(homePlayer, BorderLayout.WEST);
        statusArea.add(awayPlayer, BorderLayout.EAST);

        turnMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        turnMessage.setHorizontalAlignment(SwingConstants.CENTER);
        statusArea.add(turnMessage, BorderLayout.CENTER);

        add(statusArea, BorderLayout.NORTH);

        add(new JLabel("Maybe an exit button down here."), BorderLayout.SOUTH);

        JPanel gameArea = new JPanel(new GridLayout(1, 2, 6, 0));
        gameArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        gameArea.add(homeGrid);
        gameArea.add(awayGrid);

        add(gameArea, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

