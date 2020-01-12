
//imports
import java.awt.Font;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class LabyrinthGUI extends JFrame implements ActionListener, MouseListener {

	boolean aiMoved = false;

	int highestPlayer = 1;

	// 2d array of the open spots for each tile
	String[][] openPointsArray = new String[7][7];

	// open spots for the extra tile
	String extraOpenPoints;

	// Array for holding all the tile objects used to display the board
	Tile[][] tiles = new Tile[7][7];

	// array holding unmovable tiles to be placed in the exact same place each time
	Tile[] unmoveableTreasureTilesArray = new Tile[16];

	// array holding movable tiles to be randomly placed
	Tile[] moveableTreasureTilesArray = new Tile[12];

	// Extra tile object used as initial tile to shift for the first turn
	Tile extraTile;

	// Deck object that holds the hands of each player
	Deck deck = new Deck();

	// Array of player objects to hold score and their treasures
	Player[] players = new Player[5];

	// Arrays to hold coordinates of each player
	int[] playersX = new int[5];
	int[] playersY = new int[5];

	// default string for temporary moving of owner
	String prevOwner = "00000";
	String nextOwner = "00000";

	// arrays of coordinates of locations where a player can move through open
	// pathways
	ArrayList<Integer> validX = new ArrayList<Integer>();
	ArrayList<Integer> validY = new ArrayList<Integer>();


	ArrayList<Integer> treasuresX = new ArrayList<Integer>();
	ArrayList<Integer> treasuresY = new ArrayList<Integer>();


	int maxTreasuresCount;
	int maxMovesCount;

	Point maxTreasures;
	Point maxMoves;

	int minTreasuresCount;

	Point minTreasures;

	int maxTreasuresRotate;
	int maxMovesRotate;

	int rotateTileTreasures;
	int rotateTileMoves;


	// Array of buttons for shifting tiles
	JButton[] buttonArray = new JButton[12];

	// Button to rotate the current extra tile
	JButton rotateExtraTile = new JButton();

	JButton rotateExtraTile2 = new JButton();

	// Labels to show all the player's treasures
	JLabel[][] playerCards = new JLabel[5][5];

	// Displays each player's score
	JLabel[] playerScores = new JLabel[5];

	// Displays the image of each player
	JLabel[] playerScoreLabels = new JLabel[5];

	// Denotes which hand each player owns
	JLabel[] playerHandLabels = new JLabel[5];

	// Displays current player's turn
	JLabel turnLabel = new JLabel();

	// Displays "SCORES:"
	JLabel scoresLabel = new JLabel();

	// Displays "Extra tile"
	JLabel extraTileLabel = new JLabel();

	JLabel highlight[][] = new JLabel[7][7];

	// Background images
	JLabel backgroundLabel = new JLabel();

	// ensures when moving owners, process only occurs once
	int swapped = 0;

	// Integer to hold which player's turn it is
	int turn = 1;

	// Boolean to hold if a player has shifted the board during their turn
	boolean shifted = false;

	// Boolean so help box only appears when applicable
	boolean helpboxsentinel = false;

	// Save button
	JButton save = new JButton("SAVE");

	// Load button
	JButton load = new JButton("LOAD");

	public int aiTurn = 3;

	public boolean defenseMoved = false;

	// constructor method
	public LabyrinthGUI() {

		// format frame
		setResizable(false);
		setSize(1300, 1000);
		setLayout(null);

		// initialize highlights and tiles
		setupHighlights();
		initializeTiles();

		// Displays board
		setBoard();

		// Displays all labels
		setGUI();

		// Adds the extra tile to the frame
		add(extraTile);

		// Displays background image
		add(backgroundLabel);
		backgroundLabel.setIcon(new ImageIcon("backgroundbackground2.png"));
		backgroundLabel.setBounds(0, 0, 1300, 1000);

		// Displays the players treasures
		displayHands();
		extraTile.setBounds(1150, 850, 70, 70);

		// sets the array of open points
		setOpenPoints();

		// Updates the GUI
		updateBoard();

		// make frame visible
		setVisible(true);

		// locate players
		findPlayers();

		// remove players 3 and 4 if only 2 playing
		if (SetupScreen.numPlayers == 2) {

			int px = playersX[3];
			int py = playersY[3];
			tiles[px][py].setOwner("00000");

			px = playersX[4];
			py = playersY[4];
			tiles[px][py].setOwner("00000");

			// remove player 4 if only 3 playing
		} else if (SetupScreen.numPlayers == 2) {

			int px = playersX[4];
			int py = playersY[4];
			tiles[px][py].setOwner("00000");

		}

		// Close program upon exiting window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void save() {

		try {

			// Writes in tile object parameters onto a text file
			PrintWriter writer = new PrintWriter("boardSave.txt");

			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					writer.print(tiles[i][j].getShape() + ",");
					writer.print(tiles[i][j].isMoveable() + ",");
					writer.print(tiles[i][j].getTreasure() + ",");
					writer.print(tiles[i][j].getOrientation() + ",");
					writer.print(tiles[i][j].getOwner() + ",");
					writer.print(tiles[i][j].getFile() + ",");
					writer.println();
				}

			}

			writer.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		try {

			// Writes player scores onto a text file
			PrintWriter writer = new PrintWriter("playerSave.txt");

			for (int i = 1; i < players.length; i++) {

				writer.print(players[i].getScore() + ",");
				writer.println();

			}

			writer.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	private void load() {

		try {

			// Reads in text file
			Scanner input = new Scanner(new File("boardSave.txt"));
			input.useDelimiter(",");

			for (int i = 0; i < tiles.length; i++) {

				int index = 0;
				while (input.hasNextLine() && index < 7) {

					String shape = input.next();
					boolean moveable = input.nextBoolean();
					int treasure = input.nextInt();
					int orientation = input.nextInt();
					String owner = input.next();
					String file = input.next();

					tiles[i][index].setShape(shape);
					tiles[i][index].setMoveable(moveable);
					tiles[i][index].setTreasure(treasure);
					tiles[i][index].setOrientation(orientation);
					tiles[i][index].setOwner(owner);
					tiles[i][index].setFile(file);
					index++;
				}
			}

			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("Load file not found");
		}


		updateBoard();
	}

	// Reads in treasures from text file and creates objects with corresponding
	// parameters
	public void readTreasures() {

		try {

			// read in scanner
			Scanner input = new Scanner(new File("tilesInfo.txt"));
			// each value separated by comma
			input.useDelimiter(",");

			// counter
			int index = 0;

			// while text file still continues, first 16 unmovable
			while (input.hasNextLine() && index < 16) {

				// get parameters
				String shape = input.next();
				boolean moveable = input.nextBoolean();
				int treasure = input.nextInt();
				int orientation = input.nextInt();
				String owner = input.next();
				String file = input.next();

				// add parameters to array
				unmoveableTreasureTilesArray[index] = new Tile(shape, moveable, treasure, orientation, owner, file);
				index++;

			}

			index = 0;

			while (input.hasNextLine()) {

				// get parameters
				String shape = input.next();
				boolean moveable = input.nextBoolean();
				int treasure = input.nextInt();
				int orientation = input.nextInt();
				String owner = input.next();
				String file = input.next();

				int randOrientation = (int) (Math.random() * 4);

				// set parameters to array
				moveableTreasureTilesArray[index] = new Tile(shape, moveable, treasure, randOrientation, owner,
						file + randOrientation + ".png");

				index++;

			}

			// close file
			input.close();

			// if file not found
		} catch (FileNotFoundException error) {

			System.out.println("File not found");

		}

		// randomize moveable treasures
		shuffle();

	}

	// recursive method to find free pathways
	private void setPath(int x, int y) {

		// find open points of the current tile
		String currentOpenPoints = openPointsArray[x][y];

		// ensures process occurs once
		boolean repeated = false;

		// add coordinates of open pathway to arraylist
		validX.add(x);
		validY.add(y);

		// check if upwards is open
		if (y - 1 >= 0)
			if (currentOpenPoints.charAt(0) == '1' && openPointsArray[x][y - 1].charAt(2) == '1') {
				for (int i = 0; i < validX.size(); i++)
					if (x == validX.get(i) && (y - 1) == validY.get(i))
						repeated = true;
				if (!repeated)
					setPath(x, y - 1);
				repeated = false;

			}

		// check if path to right is open
		if (x + 1 < openPointsArray.length)
			if (currentOpenPoints.charAt(1) == '1' && openPointsArray[x + 1][y].charAt(3) == '1') {
				for (int i = 0; i < validX.size(); i++)
					if ((x + 1) == validX.get(i) && (y) == validY.get(i))
						repeated = true;
				if (!repeated)
					setPath(x + 1, y);
				repeated = false;
			}

		// check if path downwards is open
		if (y + 1 < openPointsArray.length)
			if (currentOpenPoints.charAt(2) == '1' && openPointsArray[x][y + 1].charAt(0) == '1') {
				for (int i = 0; i < validX.size(); i++)
					if (x == validX.get(i) && (y + 1) == validY.get(i))
						repeated = true;
				if (!repeated)
					setPath(x, y + 1);
				repeated = false;
			}

		// check if path to left is open
		if (x - 1 >= 0)
			if (currentOpenPoints.charAt(3) == '1' && openPointsArray[x - 1][y].charAt(1) == '1') {
				for (int i = 0; i < validX.size(); i++)
					if ((x - 1) == validX.get(i) && (y) == validY.get(i))
						repeated = true;
				if (!repeated)
					setPath(x - 1, y);
				repeated = false;
			}

	}

	// method that checks if a path from a tile to a direction is valid
	public boolean checkPath(int x, int y, int direction) {

		// initial tile
		String currentOpenPoints = openPointsArray[x][y];

		// checks upwards
		if (direction == 0) {

			// next tile
			String nextOpenPoints = openPointsArray[x][y - 1];

			if (currentOpenPoints.charAt(0) == '1' && nextOpenPoints.charAt(2) == '1') {

				return true;
			} else {
				return false;
			}

			// checks to the right
		} else if (direction == 1) {

			String nextOpenPoints = openPointsArray[x + 1][y];

			if (currentOpenPoints.charAt(1) == '1' && nextOpenPoints.charAt(3) == '1') {
				return true;
			} else {
				return false;
			}

			// checks downwards
		} else if (direction == 2) {

			String nextOpenPoints = openPointsArray[x][y + 1];

			if (currentOpenPoints.charAt(2) == '1' && nextOpenPoints.charAt(0) == '1') {
				return true;
			} else {
				return false;
			}

			// check to the right
		} else if (direction == 3) {

			String nextOpenPoints = openPointsArray[x - 1][y];

			if (currentOpenPoints.charAt(3) == '1' && nextOpenPoints.charAt(1) == '1') {
				return true;
			} else {
				return false;
			}

		} else {

			return false;

		}

	}

	// method that sees for a specific tile, which direction is left open
	public String openPoints(String shape, int orientation) {

		// if shape is T
		if (shape.contains("T")) {

			if (orientation == 0) {
				return "0111";
			} else if (orientation == 1) {
				return "1011";
			} else if (orientation == 2) {
				return "1101";
			} else if (orientation == 3) {
				return "1110";
			} else {
				return "InvalidT";
			}

			// if shape is I
		} else if (shape.contains("I")) {

			if (orientation == 0) {
				return "1010";
			} else if (orientation == 1) {
				return "0101";
			} else if (orientation == 2) {
				return "1010";
			} else if (orientation == 3) {
				return "0101";
			} else {
				return "InvalidI";
			}

			// if shape is L
		} else if (shape.contains("L")) {

			if (orientation == 0) {
				return "1100";
			} else if (orientation == 1) {
				return "0110";
			} else if (orientation == 2) {
				return "0011";
			} else if (orientation == 3) {
				return "1001";
			} else {
				return "Invalid";
			}

		} else {

			return "Invalid";

		}

	}

	// cycles through each tile and adds where open paths are for each tile
	public void setOpenPoints() {

		// cycle through all tiles
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				openPointsArray[x][y] = openPoints(tiles[x][y].getShape(), tiles[x][y].getOrientation());

			}

			// check open paths for extra tile
			extraOpenPoints = openPoints(extraTile.getShape(), extraTile.getOrientation());

		}
	}

	public void initializeTiles() {

		// read in text file
		readTreasures();

		// create counters
		int unmoveableCounter = 0;
		int LTally = 0;
		int ITally = 0;
		int treasureTally = 0;

		// check if there are enough of each type of tile
		boolean typeValid = false;

		// checks which type will have 1 less of its type
		int typeModifier[] = new int[3];

		// gets random tile type for extra tile
		int extraTileType = (int) (Math.random() * 3);

		// initializes type modifier
		for (int i = 0; i < 3; i++) {

			if (extraTileType == i)
				typeModifier[i] = 1;

			else
				typeModifier[i] = 0;

		}

		// cycle through each tile
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				// if on moveable tile
				if (x % 2 == 1 || y % 2 == 1) {

					// get random orientation
					int randOrientation = (int) (Math.random() * 4);

					do {

						int randTileType = (int) (Math.random() * 3);
						typeValid = false;

						// if tile type blank I
						if (randTileType == 0 && ITally < 13 - typeModifier[0]) {

							tiles[x][y] = new Tile("I", true, 10000, randOrientation, "00000",
									"Images/I" + randOrientation + ".png");
							ITally++;
							typeValid = true;

							// if tile type blank L
						} else if (randTileType == 1 && LTally < 9 - typeModifier[1]) {

							tiles[x][y] = new Tile("L", true, 10000, randOrientation, "00000",
									"Images/L" + randOrientation + ".png");
							LTally++;

							typeValid = true;

							// if tile type treasure
						} else if (treasureTally < 12 - typeModifier[2]) {

							typeValid = true;

							tiles[x][y] = moveableTreasureTilesArray[treasureTally];

							treasureTally++;
						}

					} while (typeValid == false);

					// when on an unmoveable piece
				} else {

					tiles[x][y] = unmoveableTreasureTilesArray[unmoveableCounter];
					unmoveableCounter++;

				}

			}

		}

		// get random orientation for extra tile
		int randOrientation = (int) (Math.random() * 4);

		// sets extra tile based on modifier
		if (typeModifier[0] == 1) {

			extraTile = new Tile("I", true, 10000, randOrientation, "00000", "Images/I1.png"); // temp
			// extra tile

		} else if (typeModifier[1] == 1) {

			extraTile = new Tile("L", true, 10000, randOrientation, "00000", "Images/L" + randOrientation + ".png");

		} else if (typeModifier[2] == 1) {

			extraTile = moveableTreasureTilesArray[11];

		}

	}

	// shuffles array of moveable tiles
	private void shuffle() {

		Tile tempHold;

		int randomSwitch;

		for (int i = 0; i < moveableTreasureTilesArray.length; i++) {

			randomSwitch = (int) (Math.random() * moveableTreasureTilesArray.length);

			tempHold = moveableTreasureTilesArray[i];
			moveableTreasureTilesArray[i] = moveableTreasureTilesArray[randomSwitch];
			moveableTreasureTilesArray[randomSwitch] = tempHold;

		}

	}

	// sets board
	public void setBoard() {

		// initialize players
		for (int i = 1; i < players.length; i++) {

			players[i] = new Player(deck.getHand(i), "Images/player" + i + ".png", 0);

		}

		// set hands for 1 and 2
		players[1] = new Player(deck.getHand(1), "Images/player1.png", 0);
		players[2] = new Player(deck.getHand(2), "Images/player2.png", 0);

		// set hands if enough players
		if (SetupScreen.numPlayers == 3)
			players[3] = new Player(deck.getHand(3), "Images/player3.png", 0);

		else if (SetupScreen.numPlayers == 4) {

			players[3] = new Player(deck.getHand(3), "Images/player3.png", 0);
			players[4] = new Player(deck.getHand(4), "Images/player4.png", 0);

		}

		// add players to board
		for (int i = 1; i < players.length; i++) {

			add(players[i]);

		}

		// place players at correct position
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				if (tiles[x][y].getOwner().contains("1")) {

					players[tiles[x][y].getOwner().indexOf("1")].setBounds(x * 70 + 200, y * 70 + 200, 70, 70);

				}

			}

		}

		// sets up tiles
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				tiles[x][y].setBounds(70 * x + 200, 70 * y + 200, 70, 70);
				add(tiles[x][y]);
				tiles[x][y].addMouseListener(this);

			}

		}

		// allows clicks to be listened to
		extraTile.addMouseListener(this);

	}

	// set up invisible highlights over every tile
	public void setupHighlights() {

		// cycle through tiles
		for (int x = 0; x < highlight.length; x++) {

			for (int y = 0; y < highlight[0].length; y++) {

				highlight[x][y] = new JLabel();
				highlight[x][y].setIcon(new ImageIcon("highlight.png"));
				highlight[x][y].setBounds(70 * x + 200, 70 * y + 200, 70, 70);
				add(highlight[x][y]);
				highlight[x][y].setVisible(false);

			}

		}

	}

	// Displays all labels
	public void setGUI() {

		add(save);
		add(load);
		save.addActionListener(this);
		load.addActionListener(this);
		save.setBounds(690, 800, 90, 60);
		load.setBounds(790, 800, 90, 60);
		// Displays player treasure cards
		for (int i = 0; i < playerCards.length; i++) {
			for (int j = 0; j < playerCards.length; j++) {

				playerCards[i][j] = new JLabel();
				add(playerCards[i][j]);
			}

		}

		// Displays all shifting buttons
		for (int i = 0; i < buttonArray.length; i++) {
			buttonArray[i] = new JButton();
			add(buttonArray[i]);
			buttonArray[i].addActionListener(this);

		}
		for (int i = 0; i < 3; i++) {
			buttonArray[i].setBounds(135 + (142 * (i + 1)), 715, 50, 50);
			buttonArray[i].setIcon(
					new ImageIcon(new ImageIcon("Images/" + "arrow2.png").getImage().getScaledInstance(50, 50, 0)));
		}
		for (int i = 0; i < 3; i++) {
			buttonArray[i + 3].setBounds(135 + (142 * (i + 1)), 135, 50, 50);
			buttonArray[i + 3].setIcon(
					new ImageIcon(new ImageIcon("Images/" + "arrow0.png").getImage().getScaledInstance(50, 50, 0)));
		}
		for (int i = 0; i < 3; i++) {
			buttonArray[i + 6].setBounds(715, 140 + ((i + 1) * 142), 50, 50);
			buttonArray[i + 6].setIcon(
					new ImageIcon(new ImageIcon("Images/" + "arrow1.png").getImage().getScaledInstance(50, 50, 0)));
		}
		for (int i = 0; i < 3; i++) {
			buttonArray[i + 9].setBounds(135, 130 + ((i + 1) * 142), 50, 50);
			buttonArray[i + 9].setIcon(
					new ImageIcon(new ImageIcon("Images/" + "arrow3.png").getImage().getScaledInstance(50, 50, 0)));
		}

		// Displays the scores of all players
		for (int i = 1; i <= SetupScreen.numPlayers; i++) {
			playerScores[i] = new JLabel();
			add(playerScores[i]);

			playerScores[i].setBounds(1200, 40 + (i * 120), 100, 100);
			playerScores[i].setText(String.valueOf(players[i].getScore()));
			playerScores[i].setFont(new Font("HelveticaNeue", Font.BOLD, 50));

		}

		rotateExtraTile
		.setIcon(new ImageIcon(new ImageIcon("Images/rotate.png").getImage().getScaledInstance(50, 50, 0)));

		rotateExtraTile2
		.setIcon(new ImageIcon(new ImageIcon("Images/rotate2.png").getImage().getScaledInstance(50, 50, 0)));

		// Displays the player image next to their corresponding score
		for (int i = 1; i <= SetupScreen.numPlayers; i++) {
			playerScoreLabels[i] = new JLabel();
			add(playerScoreLabels[i]);
			playerScoreLabels[i].setIcon(new ImageIcon(
					new ImageIcon("Images/" + "player" + i + ".png").getImage().getScaledInstance(90, 90, 0)));
			playerScoreLabels[i].setBounds(1060, 65 + (i * 110), 90, 90);
		}

		// Displays which hand each player owns
		for (int i = 1; i <= SetupScreen.numPlayers; i++) {
			playerHandLabels[i] = new JLabel();
			add(playerHandLabels[i]);
			playerHandLabels[i].setText(SetupScreen.pName[i] + "'s hand:");
			playerScores[i].setFont(new Font("HelveticaNeue", Font.BOLD, 50));

		}
		playerHandLabels[1].setBounds(100, 30, 100, 100);
		playerHandLabels[2].setBounds(780, 150, 100, 100);
		if (SetupScreen.numPlayers == 3) {

			playerHandLabels[3].setBounds(120, 790, 100, 100);

		} else if (SetupScreen.numPlayers == 4) {

			playerHandLabels[3].setBounds(120, 790, 100, 100);
			playerHandLabels[4].setBounds(30, 140, 100, 100);

		}

		// Displays the button to rotate the extra tile
		add(rotateExtraTile);
		rotateExtraTile.addActionListener(this);
		rotateExtraTile.setBounds(1050, 820, 50, 50);

		// Displays the button to rotate the extra tile
		add(rotateExtraTile2);
		rotateExtraTile2.addActionListener(this);
		rotateExtraTile2.setBounds(1050, 880, 50, 50);

		// Displays current player's turn
		add(turnLabel);
		turnLabel.setBounds(1000, 600, 200, 210);
		turnLabel.setText(SetupScreen.pName[turn] + "'s turn:");

		playerScoreLabels[0] = new JLabel();
		add(playerScoreLabels[0]);
		playerScoreLabels[0].setIcon(new ImageIcon(
				new ImageIcon("Images/" + "player" + turn + ".png").getImage().getScaledInstance(70, 70, 0)));

		playerScoreLabels[0].setBounds(1170, 675, 70, 70);
		turnLabel.setFont(new Font("HelveticaNeue", Font.BOLD, 20));

		add(scoresLabel);
		scoresLabel.setText("SCORES:");
		scoresLabel.setBounds(1020, 0, 250, 200);
		scoresLabel.setFont(new Font("HelveticaNeue", Font.BOLD, 50));

		add(extraTileLabel);
		extraTileLabel.setText("Extra tile");
		extraTileLabel.setFont(new Font("HelveticaNeue", Font.BOLD, 30));
		extraTileLabel.setBounds(1080, 710, 200, 200);

	}

	// Checks if the player is on the edge of the board
	public void playerEdge(int direction, Tile endTile, int index) {

		// Checks if the end tile has an owner
		if (endTile.getOwner().contains("1")) {

			// Checks if the board is being shifted left and a player is on the edge of the
			// left side of the board
			if (direction == 1) {

				String tempOwner = endTile.getOwner();
				endTile.setOwner(tiles[tiles.length - 1][index].getOwner());
				tiles[0][index].setOwner(tempOwner);

				// Checks if board is being shifted right and player is on the edge of the right
				// side of the board
			} else if (direction == 3) {

				String tempOwner = endTile.getOwner();
				endTile.setOwner(tiles[0][index].getOwner());
				tiles[tiles.length - 1][index].setOwner(tempOwner);

				// Checks if board is being shifted down and player is on the edge of the bottom
				// of the board
			} else if (direction == 0) {

				String tempOwner = endTile.getOwner();
				endTile.setOwner(tiles[index][0].getOwner());
				tiles[index][0].setOwner(tempOwner);

				// Checks if board is being shifted up and player is on the edge of the top of
				// the board
			} else if (direction == 2) {

				String tempOwner = endTile.getOwner();
				endTile.setOwner(tiles[index][0].getOwner());
				tiles[index][tiles.length - 1].setOwner(tempOwner);

			}

		}

	}

	//shift method
	public void move(int index, Tile tile, int direction) {

		// Inserts a tile from the top and shifts tiles downwards
		if (direction == 0) {
			Tile tempExtraTile = tiles[index][tiles.length - 1];

			for (int i = 2; i < tiles.length + 1; i++) {

				Tile tempTile = tiles[index][tiles.length - i];
				tiles[index][tiles.length - i] = tiles[index][(tiles.length - i) + 1];
				tiles[index][(tiles.length - i) + 1] = tempTile;

			}

			tiles[index][0] = tile;
			extraTile = tempExtraTile;
			playerEdge(direction, tempExtraTile, index);

		}

		// Inserts a tile from the right and shifts tiles to the left
		if (direction == 1) {

			Tile tempExtraTile = tiles[tiles.length - 1][index];
			for (int i = 2; i < tiles.length + 1; i++) {

				Tile tempTile = tiles[tiles.length - i][index];
				tiles[tiles.length - i][index] = tiles[(tiles.length - i) + 1][index];
				tiles[(tiles.length - i) + 1][index] = tempTile;

			}

			tiles[0][index] = tile;
			extraTile = tempExtraTile;
			playerEdge(direction, tempExtraTile, index);

		}

		// Inserts a tile from the bottom and shifts tiles upward
		if (direction == 2) {

			Tile tempExtraTile = tiles[index][0];
			for (int i = 0; i < tiles.length - 1; i++) {

				Tile tempTile = tiles[index][i + 1];
				tiles[index][i] = tiles[index][i + 1];
				tiles[index][i + 1] = tempTile;

			}

			tiles[index][tiles.length - 1] = tile;
			extraTile = tempExtraTile;
			playerEdge(direction, tempExtraTile, index);
			//		updateBoard();

		}

		// Inserts a tile from the left and shifts tiles to the right
		if (direction == 3) {

			Tile tempExtraTile = tiles[0][index];
			for (int i = 0; i < tiles.length - 1; i++) {

				Tile tempTile = tiles[i + 1][index];
				tiles[i][index] = tiles[i + 1][index];
				tiles[i + 1][index] = tempTile;

			}

			tiles[tiles.length - 1][index] = tile;
			extraTile = tempExtraTile;
			playerEdge(direction, tempExtraTile, index);
			//			updateBoard();

		}

	}

	// finds coordinates of each player
	public void findPlayers() {
		for (int playerX = 0; playerX < tiles.length; playerX++) {

			for (int playerY = 0; playerY < tiles[0].length; playerY++) {

				// find player 1
				if (tiles[playerX][playerY].getOwner().charAt(1) == '1') {

					playersX[1] = playerX;
					playersY[1] = playerY;

					// find player 2
				} else if (tiles[playerX][playerY].getOwner().charAt(2) == '1') {

					playersX[2] = playerX;
					playersY[2] = playerY;

					// find player 3
				} else if (tiles[playerX][playerY].getOwner().charAt(3) == '1') {

					playersX[3] = playerX;
					playersY[3] = playerY;

					// find player 4
				} else if (tiles[playerX][playerY].getOwner().charAt(4) == '1') {
					playersX[4] = playerX;
					playersY[4] = playerY;

				}

			}

		}

	}

	// Updates the tiles to be at their current position, updates current
	// highlighted paths, updates the current turn, updates player scores
	public void updateBoard() {

		// Updates player tiles
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				tiles[x][y].setBounds(70 * x + 200, 70 * y + 200, 70, 70);

				if (tiles[x][y].getOwner().contains("1")) {

					players[tiles[x][y].getOwner().indexOf("1")].setBounds(x * 70 + 200, y * 70 + 200, 70, 70);

				}

			}

		}

		// Updates scores
		for (int i = 1; i < SetupScreen.numPlayers + 1; i++)
			playerScores[i].setText(String.valueOf(players[i].getScore()));

		// Updates extra tile
		extraTile.setBounds(1150, 850, 70, 70);

		findPlayers();
		setOpenPoints();

		// originally sets all highlights to invisible
		for (int x = 0; x < highlight.length; x++) {

			for (int y = 0; y < highlight[0].length; y++) {

				highlight[x][y].setVisible(false);
			}

		}

		// resets possible locations
		validX.clear();
		validY.clear();

		setOpenPoints();
		// finds possible locations for player
		setPath(playersX[turn], playersY[turn]);

		// highlight possible locations
		for (int x = 0; x < validX.size(); x++) {

			highlight[validX.get(x)][validY.get(x)].setVisible(true);

		}

		// sets up score lables
		playerScoreLabels[0].setIcon(new ImageIcon(
				new ImageIcon("Images/" + "player" + turn + ".png").getImage().getScaledInstance(70, 70, 0)));

	}




	// Displays the treasures each player has
	public void displayHands() {

		// Sets the icons corresponding to the treasure that each player has in their
		// hands
		for (int i = 1; i < playerCards.length; i++) {
			int index = 0;

			for (int currentCard : players[i].getHand()) {

				playerCards[i][index].setIcon(new ImageIcon("Cards/" + Deck.CARDS[currentCard] + ".png"));
				index++;
			}
		}

		// Displays the treasure cards
		for (int player = 1; player < playerCards.length; player++) {
			for (int j = 0; j < playerCards.length; j++) {

				if (player == 1)
					playerCards[player][j].setBounds(120 + ((j + 1) * 100), 35, 60, 80);
				if (player == 2)
					playerCards[player][j].setBounds(800, 120 + ((j + 1) * 100), 60, 80);

				if (SetupScreen.numPlayers == 3) {
					if (player == 3)
						playerCards[player][j].setBounds(120 + ((j + 1) * 100), 800, 60, 80);
				}
				if (SetupScreen.numPlayers == 4) {
					if (player == 3)
						playerCards[player][j].setBounds(120 + ((j + 1) * 100), 800, 60, 80);
					if (player == 4)
						playerCards[player][j].setBounds(40, 120 + ((j + 1) * 100), 60, 80);

				}
			}
		}
	}

	//TODO SHIFT HAPPENING	
	@Override
	public void actionPerformed(ActionEvent click) {


		updateBoard();

		// Inserts tiles from each direction for possible rows/columns

		// Inserts tile from the bottom at the 1st column
		if (click.getSource() == buttonArray[0] && shifted == false) {
			move(1, extraTile, 2);
			shifted = true;

			// If the player has already shifted
		} else if (shifted == true)

			// Display help box
			JOptionPane.showMessageDialog(backgroundLabel, "You have already shifted.");

		// Inserts tile from the bottom at the 3rd column
		if (click.getSource() == buttonArray[1] && shifted == false) {
			move(3, extraTile, 2);
			shifted = true;
		}

		// Inserts tile from the bottom at the 5th column
		if (click.getSource() == buttonArray[2] && shifted == false) {
			move(5, extraTile, 2);
			shifted = true;
		}

		// Inserts tile from the top at the 1st column
		if (click.getSource() == buttonArray[3] && shifted == false) {
			move(1, extraTile, 0);
			shifted = true;
		}

		// Inserts tile from the top at the 3rd column
		if (click.getSource() == buttonArray[4] && shifted == false) {
			move(3, extraTile, 0);
			shifted = true;
		}

		// Inserts tile from the top at the 5th column
		if (click.getSource() == buttonArray[5] && shifted == false) {
			move(5, extraTile, 0);
			shifted = true;
		}

		// Inserts tiles from the left at the 1st row
		if (click.getSource() == buttonArray[6] && shifted == false) {
			move(1, extraTile, 3);
			shifted = true;
		}

		// Inserts tiles from the left at the 3rd row
		if (click.getSource() == buttonArray[7] && shifted == false) {
			move(3, extraTile, 3);
			shifted = true;
		}

		// Inserts tiles from the left at the 5th row
		if (click.getSource() == buttonArray[8] && shifted == false) {
			move(5, extraTile, 3);
			shifted = true;
		}

		// Inserts tiles from the right at the 1st row
		if (click.getSource() == buttonArray[9] && shifted == false) {
			move(1, extraTile, 1);
			shifted = true;
		}

		// Inserts tiles from the right at the 3rd row
		if (click.getSource() == buttonArray[10] && shifted == false) {
			move(3, extraTile, 1);
			shifted = true;
		}

		// Inserts tiles from the right at the 5th row
		if (click.getSource() == buttonArray[11] && shifted == false) {
			move(5, extraTile, 1);
			shifted = true;
		}

		// Rotates the extra tile
		if (click.getSource() == rotateExtraTile) {

			if (extraTile.getOrientation() < 3) {

				extraTile.setOrientation(extraTile.getOrientation() + 1);

			} else {

				extraTile.setOrientation(0);
			}

			int fileNameLength = extraTile.getFile().length();

			String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
			+ extraTile.getFile().substring(fileNameLength - 4);
			extraTile.setFile(newFileName);
		}

		if (click.getSource() == rotateExtraTile2) {

			if (extraTile.getOrientation() > 0) {

				extraTile.setOrientation(extraTile.getOrientation() - 1);

			} else {

				extraTile.setOrientation(3);

			}

			int fileNameLength = extraTile.getFile().length();

			String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
			+ extraTile.getFile().substring(fileNameLength - 4);
			extraTile.setFile(newFileName);


		}

		updateBoard();

		if (click.getSource() == save) {
			save();
		}
		if (click.getSource() == load) {
			load();
		}
	}

	// if mouse is pressed

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO MOUSEPRESSED

		swapped = 0;

		helpboxsentinel = false;

		Arrays.fill(playersX, 0);
		Arrays.fill(playersY, 0);

		setOpenPoints();
		findPlayers();

		int i = turn;

		// cycle through all tiles
		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[0].length; y++) {

				// if certain tile is clicked
				if (e.getSource() == tiles[x][y]) {

					// only if your own tile isn't clicked
					if (!(playersX[i] == x && playersY[i] == y) && shifted == true) {

						// ensures you don't go where another player already is
						if (!(playersX[1] == x && playersY[1] == y) && !(playersX[2] == x && playersY[2] == y)
								&& !(playersX[3] == x && playersY[3] == y) && !(playersX[4] == x && playersY[4] == y)) {

							// clear open pathways
							validX.clear();
							validY.clear();

							// find open pathways
							setPath(x, y);

							// sets up temporary variables to change owners
							nextOwner = tiles[playersX[i]][playersY[i]].getOwner();
							prevOwner = tiles[x][y].getOwner();

							// checks to make sure equal index in both arrays are checked
							for (int k = 0; k < validX.size(); k++) {

								if (validX.get(k) == x && validY.get(k) == y) {

									for (int l = 0; l < validX.size(); l++) {

										if (validX.get(l) == playersX[i] && validY.get(l) == playersY[i]) {

											// ensures process occurs once
											if (swapped == 0) {

												// change players
												for (int cycleOwners = 0; cycleOwners < 5; cycleOwners++) {

													if (prevOwner.indexOf("1") == cycleOwners) {

														prevOwner = prevOwner.substring(0, cycleOwners) + "0"
																+ prevOwner.substring(cycleOwners + 1);
														nextOwner = nextOwner.substring(0, cycleOwners) + "1"
																+ nextOwner.substring(cycleOwners + 1);
														break;

													}

												}

												// set to changed location
												tiles[x][y].setOwner(nextOwner);
												tiles[playersX[i]][playersY[i]].setOwner(prevOwner);
												swapped = 1;

												// Iterates turn
												if (turn < SetupScreen.numPlayers)
													turn++;
												else
													turn = 1;

												// Player has successfully moved
												helpboxsentinel = true;
											}

											// Checks if player is on treasure
											if (tiles[x][y].getTreasure() <= 23) {

												for (int j = 0; j < 5; j++) {

													if (playerCards[i][j].getIcon().toString()
															.contains(deck.CARDS[tiles[x][y].getTreasure()])) {

														// Removes card from hand
														playerCards[i][j].setIcon(new ImageIcon(" "));

														// Adds score
														players[i].setScore(players[i].getScore() + 1);

													}

												}

											}

											// Updates turn counter
											turnLabel.setText(SetupScreen.pName[turn] + "'s turn:");

											// Resets shifted boolean
											shifted = false;

											updateBoard();

										}

									}

								}

							}

						}

						// if you click where you already were
					} else if (shifted == true) {

						// change turn
						if (turn < SetupScreen.numPlayers)
							turn++;
						else
							turn = 1;

						// updates turn counter
						turnLabel.setText(SetupScreen.pName[turn] + "'s turn:");

						// resets shifted boolean
						shifted = false;

						updateBoard();
						helpboxsentinel = true;

						// If player hasn't shifted
					} else if (shifted == false) {

						// Display help box
						JOptionPane.showMessageDialog(backgroundLabel, "You must shift before you can move.");

						// So both help boxes don't show up
						helpboxsentinel = true;
					}
				}

			}

		}

		// If player tried to make an invalid move
		if (helpboxsentinel == false) {

			// Display help box
			JOptionPane.showMessageDialog(backgroundLabel, "Invalid move");
			helpboxsentinel = true;
		}



		if (turn == aiTurn) {

			aiMoved = false;
			defenseMoved = false;


			//			chooseShift();
			chooseShiftAI();




			if (defenseMoved == false) {
				moveAI();				
			}
			turn++;
			turnLabel.setText(SetupScreen.pName[turn] + "'s turn:");
			updateBoard();

		}

		for (int end = 1; end < players.length; end++) {

			if (players[end].getScore() > 4) {

				new EndScreen(SetupScreen.pName[end]);

			}

		}


	}


	public void chooseShiftAI() {

		Tile [][] tempTiles = new Tile[7][7];		
		Tile tempExtraTile;
		tempExtraTile = extraTile;


		int[][] countTreasures = new int [6][4];
		int[][] countMoves = new int [6][4];
		maxMoves = new Point();
		maxTreasures = new Point();

		maxMovesCount = 0;
		maxTreasuresCount = 0;


		int[][] countRotateTreasures = new int[6][4];
		int[][] countRotateMoves = new int[6][4];


		maxMoves.setLocation(0, 0);
		maxTreasures.setLocation(0, 0);



		for (int x = 0; x < 7; x++) {

			for (int y = 0; y < 7; y++) {

				tempTiles[x][y] = tiles[x][y];

			}

		}


		for (int r = 0; r < 4; r++) {


			extraTile.setOrientation(r);

			int fileNameLength = extraTile.getFile().length();

			String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
			+ extraTile.getFile().substring(fileNameLength - 4);
			extraTile.setFile(newFileName);


			for (int i = 1; i < 6; i +=2) {

				for (int d = 0; d < 4; d++) {


					move(i, extraTile, d);




					// clear open pathways
					validX.clear();
					validY.clear();

					findPlayers();
					// find open pathways
					setOpenPoints();
					setPath(playersX[aiTurn], playersY[aiTurn]);

					for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {

						int currentValidX = validX.get(cyclePaths);
						int currentValidY = validY.get(cyclePaths);

						if (tiles[currentValidX][currentValidY].getTreasure() <= 23) {

							for (int cycleDeck = 0; cycleDeck <5 ; cycleDeck++) {

								if(playerCards[aiTurn][cycleDeck].getIcon().toString().contains(deck.CARDS[tiles[currentValidX][currentValidY].getTreasure()])) {

									countTreasures[i][d]++;
									countRotateTreasures[i][d] = r;


								}

							}

						}

						countMoves[i][d]++;
						countRotateMoves[i][d] = r;


					}


					int tempD = 0;				
					if (d == 0) 
						tempD = 2;
					if (d == 1) 
						tempD = 3;
					if (d == 2)
						tempD = 0;
					if (d == 3)
						tempD = 1;
					move(i, extraTile, tempD);

				}

			}


			for (int i = 0; i < 6; i++) {

				for (int d = 0; d < 4; d++) {

					if (countMoves[i][d] > maxMovesCount) {
						maxMoves.setLocation(i,d);
						maxMovesCount = countMoves[i][d];
						rotateTileMoves = countRotateMoves[i][d];

					}

					if (countTreasures[i][d] > maxTreasuresCount) {

						maxTreasures.setLocation(i,d);
						maxTreasuresCount = countTreasures[i][d];
						rotateTileTreasures = countRotateTreasures[i][d];

					}

				}

			}

		}

		updateBoard();

	}


	public void moveAI () {

		ifTreasure:
			if (maxTreasuresCount > 0) {

				extraTile.setOrientation(rotateTileTreasures);

				int fileNameLength = extraTile.getFile().length();

				String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
				+ extraTile.getFile().substring(fileNameLength - 4);
				extraTile.setFile(newFileName);

				move(maxTreasures.x, extraTile, maxTreasures.y);

				updateBoard();
				findPlayers();

				// find open pathways
				setOpenPoints();
				setPath(playersX[aiTurn], playersY[aiTurn]);

				for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {

					int currentValidX = validX.get(cyclePaths);
					int currentValidY = validY.get(cyclePaths);

					System.out.println(currentValidX + "   :    " + currentValidY);

					if (tiles[currentValidX][currentValidY].getTreasure() <= 23) {

						for (int cycleDeck = 0; cycleDeck <5 ; cycleDeck++) {

							if(playerCards[aiTurn][cycleDeck].getIcon().toString().contains(deck.CARDS[tiles[currentValidX][currentValidY].getTreasure()])) {

								if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
										&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {

									System.out.println("got treasure");
									nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
									prevOwner = tiles[currentValidX][currentValidY].getOwner();

									System.out.println(playersX[aiTurn] + "     " + playersY[aiTurn]);
									System.out.println(currentValidX + "     " + currentValidY);

									// set to changed location
									tiles[currentValidX][currentValidY].setOwner(nextOwner);
									tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);

									// Removes card from hand
									playerCards[aiTurn][cycleDeck].setIcon(new ImageIcon(" "));
									tiles[currentValidX][currentValidY].setTreasure(10000);

									// Adds score
									players[aiTurn].setScore(players[aiTurn].getScore() + 1);
									aiMoved = true;

									break ifTreasure;

								}

							}

						}

					}

				}


			} else {

				if (aiMoved == false) {
					checkFirstPlace();					
				}

				chooseShiftAI();

				if (aiMoved == false) {

					extraTile.setOrientation(rotateTileMoves);

					int fileNameLength = extraTile.getFile().length();

					String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
					+ extraTile.getFile().substring(fileNameLength - 4);
					extraTile.setFile(newFileName);

					move(maxMoves.x, extraTile, maxMoves.y);

					updateBoard();
					findPlayers();

					// find open pathways
					setOpenPoints();
					setPath(playersX[aiTurn], playersY[aiTurn]);


					for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {
						int currentValidX = validX.get(cyclePaths);
						int currentValidY = validY.get(cyclePaths);

						if (currentValidX == 0 || currentValidX == 6 && aiMoved == false) {
							if (currentValidY % 2 == 1) {

								if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
										&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {



									nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
									prevOwner = tiles[currentValidX][currentValidY].getOwner();

									// set to changed location
									tiles[currentValidX][currentValidY].setOwner(nextOwner);
									tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
									aiMoved = true;

									System.out.println("EDGE X");
								}
							}
						}
						if (currentValidY == 0 || currentValidY == 6 && aiMoved == false) {
							if (currentValidX % 2 == 1) {
								if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
										&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {

									nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
									prevOwner = tiles[currentValidX][currentValidY].getOwner();
									// set to changed location
									tiles[currentValidX][currentValidY].setOwner(nextOwner);
									tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
									aiMoved = true;
									System.out.println("EDGE Y");
								}

							}

						}

					}

				}

				if (aiMoved == false) {

					extraTile.setOrientation(rotateTileMoves);

					int fileNameLength = extraTile.getFile().length();

					String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
					+ extraTile.getFile().substring(fileNameLength - 4);
					extraTile.setFile(newFileName);

					move(maxMoves.x, extraTile, maxMoves.y);

					updateBoard();

					findPlayers();
					// find open pathways
					setOpenPoints();
					setPath(playersX[aiTurn], playersY[aiTurn]);


					for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {
						int currentValidX = validX.get(cyclePaths);
						int currentValidY = validY.get(cyclePaths);

						if (tiles[currentValidX][currentValidY].getShape().contains("T")) {

							if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
									&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {

								nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
								prevOwner = tiles[currentValidX][currentValidY].getOwner();

								// set to changed location
								tiles[currentValidX][currentValidY].setOwner(nextOwner);
								tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
								aiMoved = true;
								System.out.println("Went to T");
							}

						}

					}

				}

			}

	updateBoard();

	}


	public void checkFirstPlace() {
		
		boolean highestGetsTreasure = false;
		int maxScore = 0;

		for (int x = 1; x < 5; x ++) {

			if (players[x].getScore() >= maxScore) {
				
				maxScore = players[x].getScore();
				highestPlayer = x;

			}

		}


		if (firstPlaceReachesTreasure()) {

			Tile [][] tempTiles = new Tile[7][7];		
			Tile tempExtraTile;
			tempExtraTile = extraTile;


			int[][] countTreasures = new int [6][4];
			minTreasures = new Point();

			minTreasuresCount = 0;


			int[][] countRotateTreasures = new int[6][4];


			minTreasures.setLocation(0, 0);

			for (int x = 0; x < 7; x++) {

				for (int y = 0; y < 7; y++) {

					tempTiles[x][y] = tiles[x][y];

				}

			}


			for (int r = 0; r < 4; r++) {


				extraTile.setOrientation(r);

				int fileNameLength = extraTile.getFile().length();

				String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
				+ extraTile.getFile().substring(fileNameLength - 4);
				extraTile.setFile(newFileName);


				for (int i = 1; i < 6; i +=2) {

					for (int d = 0; d < 4; d++) {


						move(i, extraTile, d);

						// clear open pathways
						validX.clear();
						validY.clear();

						findPlayers();
						// find open pathways
						setOpenPoints();
						setPath(playersX[highestPlayer], playersY[highestPlayer]);

						for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {

							int currentValidX = validX.get(cyclePaths);
							int currentValidY = validY.get(cyclePaths);

							if (tiles[currentValidX][currentValidY].getTreasure() <= 23) {

								for (int cycleDeck = 0; cycleDeck <5 ; cycleDeck++) {

									if(playerCards[highestPlayer][cycleDeck].getIcon().toString().contains(deck.CARDS[tiles[currentValidX][currentValidY].getTreasure()])) {

										countTreasures[i][d]++;

										//										System.out.println(playerCards[highestPlayer][cycleDeck].getIcon().toString());
										countRotateTreasures[i][d] = r;
										highestGetsTreasure = true;

									}

								}

							}
							
						}

						if (countTreasures[i][d] == 0) {
							minTreasures.setLocation(i,d);
						}

						int tempD = 0;				
						if (d == 0) 
							tempD = 2;
						if (d == 1) 
							tempD = 3;
						if (d == 2)
							tempD = 0;
						if (d == 3)
							tempD = 1;
						move(i, extraTile, tempD);

					}

				}

			}


			extraTile.setOrientation(rotateTileTreasures);

			int fileNameLength = extraTile.getFile().length();

			String newFileName = extraTile.getFile().substring(0, fileNameLength - 5) + extraTile.getOrientation()
			+ extraTile.getFile().substring(fileNameLength - 4);
			extraTile.setFile(newFileName);


			if (!(minTreasures.getX() == 0 && minTreasures.getY() == 0)) {
				move((int)minTreasures.getX(), extraTile, (int)minTreasures.getY());
				updateBoard();	
				defenseMoved = true;
				aiMoved = true;
			}

			int tempD = 0;				
			if (minTreasures.getY() == 0) 
				tempD = 2;
			if (minTreasures.getY() == 1) 
				tempD = 3;
			if (minTreasures.getY() == 2)
				tempD = 0;
			if (minTreasures.getY() == 3)
				tempD = 1;


			if (firstPlaceReachesTreasure() == true) {

				move((int)minTreasures.getX(), extraTile, tempD);

				defenseMoved = false;
				aiMoved = false;
			} else {

				// clear open pathways
				validX.clear();
				validY.clear();

				findPlayers();
				// find open pathways
				setOpenPoints();
				setPath(playersX[aiTurn], playersY[aiTurn]);

				aiMoved = false;

				if (aiMoved == false & defenseMoved == true) {

					for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {
						int currentValidX = validX.get(cyclePaths);
						int currentValidY = validY.get(cyclePaths);

						if (currentValidX == 0 || currentValidX == 6 && aiMoved == false) {
							if (currentValidY % 2 == 1) {

								if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
										&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {


									nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
									prevOwner = tiles[currentValidX][currentValidY].getOwner();
									// set to changed location
									tiles[currentValidX][currentValidY].setOwner(nextOwner);
									tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
									aiMoved = true;

									System.out.println("EDGE X defense");
									
								}
								
							}
							
						}
						
						if (currentValidY == 0 || currentValidY == 6 && aiMoved == false) {
							if (currentValidX % 2 == 1) {
								if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
										&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {

									nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
									prevOwner = tiles[currentValidX][currentValidY].getOwner();
									// set to changed location
									tiles[currentValidX][currentValidY].setOwner(nextOwner);
									tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
									aiMoved = true;
									System.out.println("EDGE Y defense");
								}

							}
							
						}

					}

				}


				if (aiMoved == false && defenseMoved == true) {

					for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {
						int currentValidX = validX.get(cyclePaths);
						int currentValidY = validY.get(cyclePaths);

						if (tiles[currentValidX][currentValidY].getShape().contains("T")) {

							if (!(playersX[1] == currentValidX && playersY[1] == currentValidY) && !(playersX[2] == currentValidX && playersY[2] == currentValidY)
									&& !(playersX[3] == currentValidX && playersY[3] == currentValidY) && !(playersX[4] == currentValidX && playersY[4] == currentValidY) ) {

								nextOwner = tiles[playersX[aiTurn]][playersY[aiTurn]].getOwner();
								prevOwner = tiles[currentValidX][currentValidY].getOwner();
								// set to changed location
								tiles[currentValidX][currentValidY].setOwner(nextOwner);
								tiles[playersX[aiTurn]][playersY[aiTurn]].setOwner(prevOwner);
								aiMoved = true;
								System.out.println("defense T");

							}
							
						}

					}

				}

			}

			updateBoard();
			
		} else {

			aiMoved = false;
			
		}

	}

	public boolean firstPlaceReachesTreasure () {
		
		validX.clear();
		validY.clear();

		findPlayers();
		// find open pathways
		setOpenPoints();
		setPath(playersX[highestPlayer], playersY[highestPlayer]);
		boolean tempOppGet = false;


		for (int cyclePaths = 0; cyclePaths < validX.size(); cyclePaths++) {

			int currentValidX = validX.get(cyclePaths);
			int currentValidY = validY.get(cyclePaths);

			if (tiles[currentValidX][currentValidY].getTreasure() <= 23) {

				for (int cycleDeck = 0; cycleDeck <5 ; cycleDeck++) {

					if(playerCards[highestPlayer][cycleDeck].getIcon().toString().contains(deck.CARDS[tiles[currentValidX][currentValidY].getTreasure()])) {

						tempOppGet = true;

					}

				}

			}

		}

		if (tempOppGet == true) {
			return true;
		} else {
			return false;
		}

	}


	@Override
	public void mouseReleased(MouseEvent arg0) {


	}

	@Override
	public void mouseClicked(MouseEvent e) {

		//TODO fdsa
		// end screen if score limit reached
		for (int i = 1; i < players.length; i++) {

			if (players[i].getScore() > 4) {

				new EndScreen(SetupScreen.pName[i]);

			}

		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {


	}

	@Override
	public void mouseExited(MouseEvent arg0) {


	}

}


