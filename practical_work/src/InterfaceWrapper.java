import javax.swing.*;
import java.awt.*;

public class InterfaceWrapper {

	private static InterfaceWrapper instance = new InterfaceWrapper();
	private final JFrame frame;

	// Private constructor to prevent instantiation from other classes
	private InterfaceWrapper() {
		frame = new JFrame("Main Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);  // null centers it on the screen
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.setResizable(false);
	}

	// Public method to get the singleton instance
	public static InterfaceWrapper getInstance() {
		if (instance == null) {
			instance = new InterfaceWrapper();
		}
		return instance;
	}

	// Clears the current content of the window (for reuse)
	public void cleanWindow() {
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();
	}

	// Getter for the JFrame, in case you need to access the main window directly
	public JFrame getFrame() {
		return frame;
	}
}
