import javax.swing.*;

public class InstructionsScreen extends JFrame {

	//label showing instructions
	public JLabel instructions = new JLabel();
	
	//constructor method
	public InstructionsScreen () {
		
		//setup frame
		setResizable(false);
		setSize(1020, 1020);
		setLayout(null);
		
		//formate instructions
		instructions.setBounds(10, 10, 1000, 1000);
		instructions.setIcon(new ImageIcon(new ImageIcon("instructions.png").getImage().getScaledInstance(1000, 1000, 0)));
		add(instructions);
		
		setVisible(true);
		
		
	}
}
