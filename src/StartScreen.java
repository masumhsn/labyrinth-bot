import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

public class StartScreen extends JFrame implements ActionListener, MouseMotionListener{

	//constants for up down left right
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	//creates array of characters and corresponding labels for maze
	public static char maze [][] = new char [50][50];
	public static JLabel[][] mazeLabel = new JLabel[50][50];

	//create button to start program
	JButton startProgram = new JButton("Play");

	//create button to open instructions
	JButton instructionsButton = new JButton ("Instructions");

	//create setup button
	JButton setupButton = new JButton ("Setup");

	//create fonts
	Font buttonFont = new Font("Serif", Font.BOLD, 30);
	Font largeButtonFont = new Font ("serif", Font.BOLD, 35);
	Font startPageFont = new Font("Serif", Font.BOLD, 85);

	//create label for title
	JLabel title = new JLabel ("The Amazing Labyrinth");

	Timer timer = new Timer(10, this);
	//create and initialize variable for r of rgb value
	private int r = 100;

	//create and initialize variable for g of rgb value
	private int g = 150;

	//create and initialize variable of b of rgb value
	private int b = 199;

	//create and initialize variable for r of rgb value increasing/decreasing
	private int colorDirection = 1;

	//create and initialize variable for g of rgb value increasing/decreasing
	private int gcolorDirection = 1;

	//create and initialize variable for b of rgb value increasing/decreasing
	private int bcolorDirection = 1;

	//constructor method
	public StartScreen () {

		//set frame layour
		setResizable(false);
		setSize(1020, 1020);
		setLayout(null);
		
		//generate maze
		setMaze();
		//set labels for maze
		setLabels();

		//formate title
		title.setBounds(45, 100, 960, 300);
		title.setFont(startPageFont);
		title.setForeground(new Color(255, 150, 150));
		add(title);

		//format start button
		startProgram.setBounds(260, 400, 500, 70);
		startProgram.setFont(buttonFont);
		startProgram.setBackground(Color.WHITE);
		startProgram.setForeground(new Color(255, 150, 150));

		//format instructions button
		instructionsButton.setBounds(260, 550, 500, 70);
		instructionsButton.setFont(buttonFont);
		instructionsButton.setBackground(Color.WHITE);
		instructionsButton.setForeground(new Color(255, 150, 150));

		//format instructions button
		setupButton.setBounds(260, 705, 500, 70);
		setupButton.setFont(buttonFont);
		setupButton.setBackground(Color.WHITE);
		setupButton.setForeground(new Color(255, 150, 150));

		//add items to frame
		add(instructionsButton);
		add(startProgram);
		add(setupButton);

		//allow program to listen to when button is clicked
		startProgram.addActionListener(this);
		instructionsButton.addActionListener(this);
		setupButton.addActionListener(this);

		//listen to mouse clicks
		addMouseMotionListener(this);

		//close program upon exiting window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//start timer
		timer.start();

		//make frame visible
		setVisible(true);

	}

	//set labels for the maze
	public void setLabels() {

		//cycle through maze array
		for (int x = 0; x < mazeLabel.length; x++) {

			for (int y = 0; y < mazeLabel[0].length; y++) {

				//set appropriate label
				if (maze[x][y] == '#') {
					mazeLabel[x][y] = new JLabel();

					mazeLabel[x][y].setBounds(20 * x, 20 * y, 20, 20);
					add(mazeLabel[x][y]);
					mazeLabel[x][y].setOpaque(true);

				} 
				
			}

		}

	}

	//setup maze
	public void setMaze() {
		
		//ensures process occurs once
		boolean repeat = false;

		do {

			int counter = 0;
			repeat = false;
			
			//generate maze
			fillMaze();
			generate(1,1);

			//fill maze with . if space
			for (int x = 0; x < maze.length; x++) {

				for (int y = 0; y < maze[0].length; y++) {

					if (maze[x][y] == '.') {

						counter++;

					}

				}

			}

			//repeat until sufficient maze created
			if (counter < 1200) {

				repeat = true;

				for (int i = 0; i < maze.length; i ++) {

					Arrays.fill(maze[i], '#');

				}

			}

		} while (repeat == true);


		//cycle through maze
		for (int x = 0; x < mazeLabel.length; x++) {

			for (int y = 0; y < mazeLabel[0].length; y++) {


				//set up open and closed spots
				if (x == mazeLabel.length - 1 || y == mazeLabel.length - 1) {


					maze[x][y] = '#';

				}

				if (y > 10 && y < 15 && x > 1 && x < maze.length - 3) {

					maze[x][y] = '.';

				}

				if (x > 10 && x < 40) {

					if (y > 17 && y < 25) {
						
						maze[x][y] = '.';

					}

					if (y > 25 && y < 33) {
						
						maze[x][y] = '.';

					}
					if (y > 33 && y < 40) {
						
						maze[x][y] = '.';
					}

					if (y == 25 || y == 33 || y == 17 || y == 40) {
						
						maze[x][y] = '#';
						
					}

				}

			}
			
		}

	}


	public static void fillMaze() {

		//fill maze temporarily with all walls
		for (int i = 0; i < maze.length; i++) {

			for (int j = 0; j < maze.length; j++) {

				maze[i][j] = '#';

			}

		}

	}

	//generate maze recursively
	public static void generate (int row, int col) {

		Scanner input = new Scanner(System.in);

		for (int count = 0; count < 4; count++) {

			//pick random direction
			int direction = (int) (Math.random() * 4) ;

			//if up
			if (direction == UP) {

				if(checkValid (row - 2, col)) {

					maze[row - 2][col] = '.';
					maze[row - 1][col] = '.';

					generate(row - 2, col);

				}

				//if down
			} else if (direction == DOWN) {

				if (checkValid(row + 2, col)) {

					maze[row + 2][col] = '.';
					maze[row + 1][col] = '.';

					generate(row + 2, col);

				}

				//if left
			} else if (direction == LEFT) {

				if (checkValid(row, col - 2)) {

					maze[row][col - 2] = '.';
					maze[row][col - 1] = '.';

					generate(row, col - 2);

				}

				//if right
			} else if (direction == RIGHT) {

				if (checkValid(row, col + 2)) {

					maze[row][col + 2] = '.';
					maze[row][col + 1] = '.';

					generate(row, col + 2);

				}

			}
			
		}

	}

	//checks if direction move is valid
	public static boolean checkValid (int row, int col) {

		if (row > 0 && row < maze.length && col > 0 && col < maze.length && maze[row][col] == '#') {
			return true;
		} else {
			return false;
		}

	}



	//when timer ticks or button clicked
	@Override
	public void actionPerformed(ActionEvent e) {


		//change background color
		r += colorDirection;
		g += gcolorDirection;
		b += bcolorDirection;

		//cycle through walls
		for (int x = 0; x < mazeLabel.length; x++) {

			for (int y = 0; y < mazeLabel[0].length; y++) {
				if (maze[x][y] == '#') {

					//set color
					mazeLabel[x][y].setBackground(new Color (r, g, b));
					mazeLabel[x][y].setForeground(new Color (r, g, b));
				}
			}
		}


		if (r== 200) {
			
			colorDirection = -3;
			
		}else if (r == 101) {
			
			colorDirection = 3;
			
		}

		if (g== 200) {
			
			gcolorDirection = -3;
			
		}else if (g == 101) {
			
			gcolorDirection = 3;
			
		}

		if (b== 200) {
			
			bcolorDirection = -3;
			
		}else if (b == 101) {
			
			bcolorDirection = 3;
			
		}




		//if the start button is clicked
		if (e.getSource() == startProgram) {

			new LabyrinthGUI();
			setVisible(false);
			
			//if the instructions button is clicked
		}else if (e.getSource() == instructionsButton) {

			new InstructionsScreen();

		} else if (e.getSource () == setupButton) {
			
			new SetupScreen();
			
		}

	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseMoved(MouseEvent mouse) {

		//if mouse hovers over start button
		if (mouse.getX() > 235 && mouse.getX() < 785 && mouse.getY() > 400 && mouse.getY() < 540) {

			//increase the size of the button as emphasis
			startProgram.setBounds(235, 375, 550, 120);
			startProgram.setFont(largeButtonFont);

		} else {

			//otherwise, return to normal size
			startProgram.setBounds(260, 400, 500, 70);
			startProgram.setFont(buttonFont);

		}

		//if mouse hovers over instructions button
		if (mouse.getX() > 235 && mouse.getX() < 785 && mouse.getY() > 550 && mouse.getY() < 690) {

			//increase the size of the button as emphasis
			instructionsButton.setBounds(235, 525, 550, 120);
			instructionsButton.setFont(largeButtonFont);

		} else {

			//otherwise, return to normal size
			instructionsButton.setBounds(260, 550, 500, 70);
			instructionsButton.setFont(buttonFont);

		}


		//if mouse hovers over setup button
		if (mouse.getX() > 235 && mouse.getX() < 785 && mouse.getY() > 705 && mouse.getY() < 845) {

			//increase the size of the button as emphasis
			setupButton.setBounds(235, 680, 550, 120);
			setupButton.setFont(largeButtonFont);

		} else {

			//otherwise, return to normal size
			setupButton.setBounds(260, 705, 500, 70);
			setupButton.setFont(buttonFont);

		}

	}

}
