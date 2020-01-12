import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Player extends JLabel {

	//create fields
	private int[] hand = new int[5];
	private String fileName;
	private int score;

	//constructor method
	public Player(int[] hand, String fileName, int score) {

		this.hand = hand;
		this.score = score;
		this.setFileName(fileName);

	}

	//getters and setters
	public int[] getHand() {
		return hand;
	}

	public void setHand(int[] hand) {
		this.hand = hand;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {

		this.fileName = fileName;
		setIcon(new ImageIcon(new ImageIcon(fileName).getImage().getScaledInstance(50, 50, 0)));

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	//toString
	@Override
	public String toString() {

		return "Player [hand=" + Arrays.toString(hand) + ", fileName=" + fileName + ", score=" + score + "]";

	}
}



