package sv.project_titanic.view;

import sv.project_titanic.Controller;
import sv.project_titanic.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class GUI extends JFrame implements Runnable {
	public final static HashMap<Integer, Color> COLOR_MAP;
	static {
		COLOR_MAP = new HashMap<>();
		COLOR_MAP.put(0, Color.BLUE);
		COLOR_MAP.put(1, new Color(100, 100, 255));
		COLOR_MAP.put(2, Color.BLACK);
		COLOR_MAP.put(3, Color.RED);
		COLOR_MAP.put(4, Color.GREEN);
	}

	//Main game data
	private Grid homeGrid;
	private Grid awayGrid;
	private JLabel homePlayer;
	private JLabel awayPlayer;
	private JLabel turnMessage;
	private JButton initDoneButton;
	private Controller controller;

	//Init phase data
	private List<Integer> shipLengths;
	private Integer selectedShip;
	private String orientation;
	private Grid shipInitGrid;

	public GUI(Board homeBoard, Board awayBoard, Player homePlayer, Player awayPlayer, Controller controller) {
		this.controller = controller;

		homeGrid = new Grid(homeBoard.getYdim(), homeBoard.getYdim(), false);
		awayGrid = new Grid(awayBoard.getYdim(), awayBoard.getXdim(), true);
		shipInitGrid = new Grid(homeBoard.getYdim(), homeBoard.getXdim(), true);

		homeBoard.addObserver(homeGrid);
		homeBoard.addObserver(shipInitGrid);
		awayBoard.addObserver(awayGrid);

		shipLengths = new ArrayList<>(Arrays.asList(5,4,3,3,2));
		selectedShip = shipLengths.get(0);
		orientation = "horizontal";

		this.homePlayer = new JLabel(homePlayer.getPlayerName());
		this.awayPlayer = new JLabel(awayPlayer.getPlayerName());
		turnMessage = new JLabel("Your turn");
	}

	private Ship makeShip() {
		ArrayList<Coordinate> coords = new ArrayList<>();

		Coordinate cell = shipInitGrid.getSelectedCell();

		if(orientation == "horizontal") {
			for(int i = 0; i < selectedShip; i++)
				coords.add(new Coordinate(cell.getX() + i, cell.getY()));
		}
		else if(orientation == "vertical") {
			for(int i = 0; i < selectedShip; i++)
				coords.add(new Coordinate(cell.getX(), cell.getY() + i));
		}

		return new Ship(coords);
	}

	private void placeShip() {
		if(selectedShip != -1 && shipInitGrid.hasSelection()) {
			Ship ship = makeShip();

			if(controller.canPlaceShip(ship)) {
				controller.placeShip(ship);
				shipLengths.remove(selectedShip);

				if(shipLengths.size() == 0)
					selectedShip = -1;
				else
					selectedShip = shipLengths.get(0);
			}
		}
	}

	private String getPreviewText() {
		if(selectedShip == -1)
			return "All ships placed!";
		else
			return "Size: " + selectedShip + "\n" + "Orientation: " + orientation;
	}

	public void run() {
		setTitle("Project Titanic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new CardLayout());

		JPanel menuCard = buildMenuCard();
		add(menuCard);

		JPanel shipInitCard = buildShipInitCard();
		add(shipInitCard);

		JPanel mainGameCard = buildMainGameCard();
		add(mainGameCard);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel buildMenuCard() {
		JPanel menuCard = new JPanel(new GridBagLayout());

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		buttonPanel.setPreferredSize(new Dimension(100, 150));

		JButton hostGameButton = new JButton("Host Game");
		hostGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.hostGame();
				CardLayout layout = (CardLayout)getContentPane().getLayout();
				layout.next(getContentPane());
			}
		});
		buttonPanel.add(hostGameButton);

		JButton joinGameButton = new JButton("Join Game");
		joinGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String host = JOptionPane.showInputDialog("Enter IP Address of Host:");
				controller.joinGame(host);
				
				CardLayout layout = (CardLayout)getContentPane().getLayout();
				layout.next(getContentPane());
			}
		});
		buttonPanel.add(joinGameButton);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton);

		return buttonPanel;
	}


	private JPanel buildShipInitCard() {
		JPanel shipInitCard = new JPanel(new BorderLayout());

		JPanel mainArea = new JPanel(new GridLayout(1, 2, 6, 0));
		mainArea.add(shipInitGrid);

		JPanel shipPreviewPane = new JPanel(new GridLayout(0, 1));
		shipPreviewPane.add(new JLabel("Ship to place:"));

		final JTextArea shipPreview = new JTextArea(getPreviewText());
		shipPreview.setEditable(false);
		shipPreviewPane.add(shipPreview);

		shipInitGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				placeShip();

				if(selectedShip == -1) 
					initDoneButton.setEnabled(true);

				shipPreview.setText(getPreviewText());
				repaint();
			}
		});

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

		mainArea.add(shipPreviewPane);

		shipInitCard.add(mainArea, BorderLayout.CENTER);

		initDoneButton = new JButton("Finished");
		initDoneButton.setEnabled(false);
		initDoneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startGame();

				CardLayout layout = (CardLayout)getContentPane().getLayout();
				layout.next(getContentPane());
			}
		});

		shipInitCard.add(initDoneButton, BorderLayout.SOUTH);

		return shipInitCard;
	}
	
	private JPanel buildMainGameCard() {
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

		awayGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(awayGrid.hasSelection()) {
					controller.shoot(awayGrid.getSelectedCell());
					repaint();
				}
			}
		});

		mainGameCard.add(gameArea, BorderLayout.CENTER);

		return mainGameCard;
	}
}

