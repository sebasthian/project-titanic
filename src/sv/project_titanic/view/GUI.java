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
		if(o instanceof Controller) {
			if(arg instanceof String) {
				turnMessage.setText((String)arg);
			}
			else if(arg instanceof Player) {
				Player winner = (Player)arg;

				String message;
				if(winner.isLocal())
					message = "A winner is you!";
				else
					message = "You lose!";

				JOptionPane.showMessageDialog(this, message);

				controller.exitGame();
			}
		}
		else if(o instanceof Player) {
			if(((Player)o).isLocal())
				homePlayer.setText((String)arg);
			else
				awayPlayer.setText((String)arg);
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
		setTitle("Sink das Båt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new CardLayout());
		((JComponent)getContentPane()).setBorder(new EmptyBorder(6, 2, 2, 2));

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

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 20, 20));

		JButton hostGameButton = new JButton("Host Game");
		hostGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(GUI.this, "Enter your name:");
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
				String name = JOptionPane.showInputDialog(GUI.this, "Enter your name:");
				controller.getHomePlayer().setName(name);

				String host = JOptionPane.showInputDialog(GUI.this, "Enter IP Address of Host:");
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

		menuCard.add(buttonPanel);

		return menuCard;
	}

	/**Create the init panel for placing ships on the board.*/
	private JPanel buildShipInitCard() {
		JPanel shipInitCard = new JPanel(new BorderLayout());

		JPanel mainArea = new JPanel(new GridLayout(1, 2, 6, 0));
		mainArea.add(shipInitGrid);

		JPanel shipPreviewContainer = new JPanel(new GridBagLayout());

		JPanel shipPreviewPanel = new JPanel(new GridLayout(0, 1));
		shipPreviewPanel.add(new JLabel("Ship to place:"));

		final JTextArea shipPreview = new JTextArea(getPreviewText(), 3, 15);
		shipPreview.setEditable(false);
		shipPreviewPanel.add(shipPreview);

		shipInitGrid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(selectedShip != -1
				   && shipInitGrid.hasSelection()
				   && controller.placeShip(shipInitGrid.getSelectedCell(), orientation, selectedShip)
			    ) {
					shipLengths.remove(selectedShip);

					if(shipLengths.size() == 0) {
						selectedShip = -1;
						initDoneButton.setEnabled(true);
					}
					else {
						selectedShip = shipLengths.get(0);
					}

					shipPreview.setText(getPreviewText());
					repaint();
				}
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

		shipPreviewPanel.add(rotateButton);

		shipPreviewContainer.add(shipPreviewPanel);

		mainArea.add(shipPreviewContainer);

		shipInitCard.add(mainArea, BorderLayout.CENTER);

		JPanel finishButtonPanel = new JPanel(new GridBagLayout());

		initDoneButton = new JButton("Finished");
		initDoneButton.setEnabled(false);
		initDoneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startGame();

				CardLayout layout = (CardLayout)getContentPane().getLayout();
				layout.next(getContentPane());
			}
		});

		finishButtonPanel.add(initDoneButton);

		finishButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		shipInitCard.add(finishButtonPanel, BorderLayout.SOUTH);

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

