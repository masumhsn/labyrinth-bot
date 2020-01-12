
import java.util.Arrays;
import java.util.Random;

public class Deck {
	
	//create fields for deck and each hand
	private int[] deck = new int[24];
	private int[] hand1 = new int[5];
	private int[] hand2 = new int[5];
	private int[] hand3 = new int[5];
	private int[] hand4 = new int[5];

	public final static String[] CARDS = { "BagOfGoldCoins", "Bat", "BookWithClasp", "Dragon", "GhostInBottle",
			"GhostWaving", "GoldCrown", "GoldMenorah", "GoldRing", "Helmet", "Jewel", "LadyPig", "Lizard", "Moth",
			"Owl", "Rat", "Scarab", "SetOfKeys", "Skull", "Sorceress", "SpiderOnWeb", "Sword", "TreasureChest",
			"TreasureMap" };

	//constructor method
	public Deck() {

		for (int i = 0; i < deck.length; i++) {
			deck[i] = i;
		}

		shuffle();
		setHands();
	}

	//getters and setters
	public int[] getDeck() {
		return deck;
	}

	public void setDeck(int[] deck) {
		this.deck = deck;
	}

	private void shuffle() {
		Random rand = new Random();
		int r, hold;

		for (int i = 0; i < deck.length; i++) {
			r = rand.nextInt(24);
			hold = deck[i];
			deck[i] = deck[r];
			deck[r] = hold;
		}
	}

	private void setHands() {

		hand1 = Arrays.copyOfRange(deck, 0, 5);
		hand2 = Arrays.copyOfRange(deck, 5, 10);
		hand3 = Arrays.copyOfRange(deck, 10, 15);
		hand4 = Arrays.copyOfRange(deck, 15, 20);

	}

	public int[] getHand(int playerNum) {
		if (playerNum == 1)
			return hand1;
		else if (playerNum == 2)
			return hand2;
		else if (playerNum == 3)
			return hand3;
		else if (playerNum == 4)
			return hand4;
		else {
			System.out.println("Invalid Number");
			return null;
		}
	}


	//toString method
	@Override
	public String toString() {
		return "Deck [deck=" + Arrays.toString(deck) + ", hand1=" + Arrays.toString(hand1) + ", hand2="
				+ Arrays.toString(hand2) + ", hand3=" + Arrays.toString(hand3) + ", hand4=" + Arrays.toString(hand4)
				+ "]";
	}

}

