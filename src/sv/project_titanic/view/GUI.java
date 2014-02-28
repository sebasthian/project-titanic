package sv.project_titanic.view;

import sv.project_titanic.Controller;
import sv.project_titanic.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.*;

/**Main GUI class for Battleship.*/
public class GUI extends JFrame implements Runnable, Observer {
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

	/**Create a new GUI that can be run on the Swing Event Dispatch Thread.
	 *
	 * @param homeBoard home player's Board.
	 * @param awayBoard opponent's Board.
	 * @param controller ref to the Controller.
	 */
	public GUI(Board homeBoard, Board awayBoard, Controller controller) {
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

		homePlayer = new JLabel(controller.getHomePlayer().getName());
		awayPlayer = new JLabel(controller.getAwayPlayer().getName());
		turnMessage = new JLabel("Opponent's turn");

		controller.getHomePlayer().addObserver(this);
		controller.getAwayPlayer().addObserver(this);
		controller.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		if(o instanceof Controller)
			turnMessage.setText((String)arg);
		else if(o instanceof Player) {
			if(((Player)o).isLocal())
				homePlayer.setText((String)arg);
			else
				awayPlayer.setText((String)arg);
		}
	}

	/**Create a new Ship. Used when placing ships on the board during the init
	 * phase.
	 */
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

	/**Try placing a Ship on the homeBoard. If the ship can be placed, cycle
	 * to the next Ship. otherwise, do nothing.
	 */
	private void placeShip() {
		if(selectedShip != -1 && shipInitGrid.hasSelection()) {
			Ship ship = makeShip();

			if(controller.placeShip(ship)) {
				shipLengths.remove(selectedShip);

				if(shipLengths.size() == 0)
					selectedShip = -1;
				else
					selectedShip = shipLengths.get(0);
			}
		}
	}

	/**Make the preview text shown when placing ships during init.*/
	private String getPreviewText() {
		if(selectedShip == -1)
			return "All ships placed!";
		else
			return "Size: " + selectedShip + "\n" + "Orientation: " + orientation;
	}

	/**Spawn the game window.*/
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

	/**Create the main menu.*/
	private JPanel buildMenuCard() {
		JPanel menuCard = new JPanel(new GridBagLayout());

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		buttonPanel.setPreferredSize(new Dimension(100, 150));

		JButton hostGameButton = new JButton("Host Game");
		hostGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Enter your name:");
				controller.getHomePlayer().setName(name);

				controller.hostGame();
				CardLayout layout = (CardLayout)getContentPane().getLayout();
				layout.next(getContentPane());
			}
		});
		buttonPanel.add(hostGameButton);

		JButton joinGameButton = new JButton("Join Game");
		joinGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Enter your name:");
				controller.getHomePlayer().setName(name);

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

	/**Create the init panel for placing ships on the board.*/
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

	/**Create the main game panel.*/
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

