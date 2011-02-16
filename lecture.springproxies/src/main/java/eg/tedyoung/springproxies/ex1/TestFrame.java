package eg.tedyoung.springproxies.ex1;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	JButton button = new JButton("Button");
	
	JPanel panel = new JPanel();
	
	public TestFrame() {
		setLayout(new GridLayout(1, 0));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel.setBackground(Color.BLUE);
		
		add(button);
		add(panel);
		
		pack();
		setVisible(true);
	}
}
