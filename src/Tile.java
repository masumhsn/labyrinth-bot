import java.util.*;

import javax.swing.*;

public class Tile extends JLabel {

	//create fields
	private String shape;
	private boolean moveable;
	private int treasure;
	private int orientation;
	private String owner;
	private String file;

	//constructor method
	public Tile(String shape, boolean moveable, int treasure, int orientation, String owner, String file) {

		super();
		this.shape = shape;
		this.moveable = moveable;
		this.treasure = treasure;
		this.orientation = orientation;
		this.owner = owner;
		setFile(file);

	}

	
	//getters and setters for field
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public int getTreasure() {
		return treasure;
	}

	public void setTreasure(int treasure) {
		this.treasure = treasure;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String fileName) {

		this.file = fileName;
		setIcon(new ImageIcon(new ImageIcon(fileName).getImage().getScaledInstance(70, 70, 0)));

	}

	//toString method
	@Override
	public String toString() {
		return "Tile [shape=" + shape + ", moveable=" + moveable + ", treasure=" + treasure + ", orientation="
				+ orientation + ", owner=" + owner + ", file=" + file + "]";
	}

}



