

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LabyrinthTest {

	public static void main (String[] args) {

		//open start screen
		new StartScreen();

//		new EndScreen("Eddy");
		
		//sets up default names if not changes
		SetupScreen.pName[1] = "Player 1";
		SetupScreen.pName[2] = "Player 2";
		SetupScreen.pName[3] = "Player 3";
		SetupScreen.pName[4] = "Player 4";
			
	}
	
}
