import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;



public class SetupScreen extends JFrame implements WindowListener, ActionListener{

	//variable for number of players
	static int numPlayers = 4;	

	//variable for player name
	public static String[] pName = new String[5];

	//creates 4 text fields for player names
	JTextField p1Input = new JTextField("Player 1", 100);
	JTextField p2Input = new JTextField("Player 2",100);
	JTextField p3Input = new JTextField("Player 3", 100);
	JTextField p4Input = new JTextField("Player 4", 100);

	//title labels
	JLabel title = new JLabel("Select your names");
	JLabel numPs = new JLabel("Select the number");
	JLabel numPst = new JLabel ("of players");

	//array of player images
	JLabel [] pImage = new JLabel[5];

	//button for increase number of players
	JButton pUp = new JButton("+");

	//button for decrease number of players
	JButton pDown = new JButton("-");

	//label showing number of players
	JLabel numPlayersLabel = new JLabel("4");

	//create fonts
	Font inputFont = new Font("Serif", Font.BOLD, 30);
	Font bigFont = new Font("Serif", Font.BOLD, 65);

	//constructor method
	public SetupScreen () {

		//set frame layout
		setResizable(false);
		setSize(1020, 1020);
		setLayout(null);
		addWindowListener(this);

		//formate title
		title.setBounds(300, 0, 960, 90);
		title.setFont(bigFont);
		title.setForeground(new Color(255, 150, 150));
		add(title);

		//format number of players label
		numPs.setBounds(100, 700, 960, 90);
		numPs.setFont(bigFont);
		numPs.setForeground(new Color(255, 150, 150));
		add(numPs);

		//formate number of players lable
		numPst.setBounds(100, 800, 960, 90);
		numPst.setFont(bigFont);
		numPst.setForeground(new Color(255, 150, 150));
		add(numPst);

		//format increase button
		pUp.setBounds(900, 750, 80, 80);
		pUp.setFont(bigFont);
		pUp.setBackground(Color.WHITE);
		pUp.setForeground(new Color(255, 150, 150));
		add(pUp);

		//formate decrease button
		pDown.setBounds(700, 750, 80, 80);
		pDown.setFont(bigFont);
		pDown.setBackground(Color.WHITE);
		pDown.setForeground(new Color(255, 150, 150));
		add(pDown);

		//make program listen to buttons
		pUp.addActionListener(this);
		pDown.addActionListener(this);

		//format label showing number of players
		numPlayersLabel.setBounds(820, 750, 120, 80);
		numPlayersLabel.setFont(bigFont);
		numPlayersLabel.setBackground(Color.WHITE);
		numPlayersLabel.setForeground(new Color(255, 150, 150));
		add(numPlayersLabel);

		//format player 1 input
		p1Input.setBounds(300, 150, 500, 70);
		add(p1Input);
		p1Input.setFont(inputFont);
		p1Input.setForeground(Color.RED);

		//format player 2 input
		p2Input.setBounds(300, 300, 500, 70);
		add(p2Input);
		p2Input.setFont(inputFont);
		p2Input.setForeground(Color.YELLOW);

		//format player 3 input
		p3Input.setBounds(300, 450, 500, 70);
		add(p3Input);
		p3Input.setFont(inputFont);
		p3Input.setForeground(Color.BLUE);

		//format player 4 input
		p4Input.setBounds(300, 600, 500, 70);
		add(p4Input);
		p4Input.setFont(inputFont);
		p4Input.setForeground(Color.GREEN);


		//cycle through labels
		for (int i = 1; i < 5; i++) {

			//format labels
			pImage[i] = new JLabel();
			pImage[i].setIcon(new ImageIcon(new ImageIcon("Images/player" + i + ".png").getImage().getScaledInstance(250, 250, 0)));
			pImage[i].setBounds(80,  150 * i  - 90, 250, 250 );
			add(pImage[i]);
		}

		//make labels visible
		setVisible(true);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {


	}

	//if window closes
	@Override
	public void windowClosing(WindowEvent e) {

		//set player names when window closes
		pName[1] = p1Input.getText();
		pName[2] = p2Input.getText();
		pName[3] = p3Input.getText();
		pName[4] = p4Input.getText();

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//if increase button clicked
		if (e.getSource() == pUp) {
			
			//and max limit isn't reached
			if (numPlayers < 4) {
				
				//increase players
				numPlayers++;
				
			}

		}

		//if decrease button clicked
		if (e.getSource() == pDown) {

			//and minimum limit isn't reached
			if (numPlayers > 2) {
				
				//decrease players
				numPlayers--;
				
			}
			
		}

		//if 3 players
		if (numPlayers == 3) {

			//hide fourth player
			p4Input.setVisible(false);
			pImage[4].setVisible(false);
			p3Input.setVisible(true);
			pImage[3].setVisible(true);

			//if 2 players
		} else if (numPlayers == 2) {

			// hide players 3 and 4
			p4Input.setVisible(false);
			pImage[4].setVisible(false);
			p3Input.setVisible(false);
			pImage[3].setVisible(false);

			//if 4 players
		} else if (numPlayers == 4){

			//show all players
			p4Input.setVisible(true);
			pImage[4].setVisible(true);
			p3Input.setVisible(true);
			pImage[3].setVisible(true);


		}

		//update label
		numPlayersLabel.setText(String.valueOf(numPlayers));

	}


}
