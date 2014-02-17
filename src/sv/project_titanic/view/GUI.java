package sv.project_titanic.view;

import sv.project_titanic.Controller;
import sv.project_titanic.model.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class GUI extends JFrame implements Runnable {
    public final static Color SHIP_COLOR = Color.BLACK;
    public final static Color EMPTY_COLOR = Color.BLUE;
    public final static Color MISS_COLOR = new Color(100, 100, 255);
    public final static Color HIT_COLOR = Color.RED;
    public final static Color SUNK_COLOR = Color.GREEN;

    private Grid homeGrid;
    private Grid awayGrid;
    private Grid shipInitGrid;
    private JLabel homePlayer;
    private JLabel awayPlayer;
    private JLabel turnMessage;
    private JButton initDoneButton;
    private JTextArea shipPreview;
    private Controller controller;
    private List<Integer> shipLengths;
    private Integer selectedShip;
    private String orientation;

    public GUI(Board homeBoard, Board awayBoard, Player homePlayer, Player awayPlayer, Controller controller) {
        this.controller = controller;

        homeGrid = new Grid(10, 10, false);
        awayGrid = new Grid(10, 10, true);
        shipInitGrid = new Grid(10, 10, true);

        awayGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(awayGrid.hasSelection()) {
                    GUI.this.controller.shoot(awayGrid.getSelectedCell());
                }
            }
        });

        shipInitGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedShip != -1 && shipInitGrid.hasSelection()) {
                    Ship ship = makeShip();
                    if(GUI.this.controller.canPlaceShip(ship)) {
                        GUI.this.controller.placeShip(ship);
                        shipLengths.remove(selectedShip);
                        if(shipLengths.size() == 0) {
                            selectedShip = -1;
                            shipPreview.setText("All ships placed");
                            initDoneButton.setEnabled(true);
                        }
                        else {
                            selectedShip = shipLengths.get(0);
                            shipPreview.setText(getPreviewText());
                        }
                    }
                }
            }
        });

        homeBoard.addObserver(homeGrid);
        homeBoard.addObserver(shipInitGrid);
        awayBoard.addObserver(awayGrid);

        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                if(homeBoard.getFieldStatus(col, row) == 2)
                    homeGrid.getCell(row, col).setColor(SHIP_COLOR);
            }
        }

        shipLengths = new ArrayList<>(Arrays.asList(5,4,3,3,2));
        selectedShip = shipLengths.get(0);
        orientation = "horizontal";

        this.homePlayer = new JLabel(homePlayer.getName());
        this.awayPlayer = new JLabel(awayPlayer.getName());
        turnMessage = new JLabel("Your turn");
    }

    private Ship makeShip() {
        ArrayList<Coordinate> coords = new ArrayList<>();

        Coordinate cell = shipInitGrid.getSelectedCell();

        for(int i = 0; i < selectedShip; i++) {
            if(orientation == "horizontal")
                coords.add(new Coordinate(cell.getX() + i, cell.getY()));
            else if(orientation == "vertical") {
                coords.add(new Coordinate(cell.getX(), cell.getY() + i));
            }
        }

        return new Ship(coords);
    }

    private String getPreviewText() {
        return "Size: " + selectedShip + "\n" + "Orientation: " + orientation;
    }


    public void run() {
        setTitle("Project Titanic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new CardLayout());

        //Ship init
        JPanel shipInitCard = new JPanel(new BorderLayout());

        JPanel shipInitArea = new JPanel(new GridLayout(1, 2, 6, 0));

        shipInitArea.add(shipInitGrid);

        JPanel shipPreviewPane = new JPanel(new GridLayout(0, 1));
        shipPreviewPane.add(new JLabel("Ship to place:"));
        shipPreview = new JTextArea(getPreviewText());
        shipPreview.setEditable(false);
        shipPreviewPane.add(shipPreview);
        JButton rotateButton = new JButton("Rotate");

        rotateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(orientation == "horizontal")
                    orientation = "vertical";
                else
                    orientation = "horizontal";

                shipPreview.setText(getPreviewText());
            }
        });

        shipPreviewPane.add(rotateButton);
        shipInitArea.add(shipPreviewPane);

        shipInitCard.add(shipInitArea, BorderLayout.CENTER);
 
        initDoneButton = new JButton("Finished");
        initDoneButton.setEnabled(false);
        initDoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout)getContentPane().getLayout();
                layout.next(getContentPane());
            }
        });

        shipInitCard.add(initDoneButton, BorderLayout.SOUTH);

        add(shipInitCard);

        //Main game
        JPanel mainGameCard = new JPanel(new BorderLayout());

        JPanel statusArea = new JPanel(new BorderLayout());
        statusArea.add(homePlayer, BorderLayout.WEST);
        statusArea.add(awayPlayer, BorderLayout.EAST);

        turnMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        turnMessage.setHorizontalAlignment(SwingConstants.CENTER);
        statusArea.add(turnMessage, BorderLayout.CENTER);

        mainGameCard.add(statusArea, BorderLayout.NORTH);

        mainGameCard.add(new JLabel("Maybe an exit button down here."), BorderLayout.SOUTH);

        JPanel gameArea = new JPanel(new GridLayout(1, 2, 6, 0));
        gameArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        gameArea.add(homeGrid);
        gameArea.add(awayGrid);

        mainGameCard.add(gameArea, BorderLayout.CENTER);

        add(mainGameCard);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

