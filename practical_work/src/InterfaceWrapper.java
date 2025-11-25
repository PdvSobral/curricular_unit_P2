import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class InterfaceWrapper {

	private JFrame frame;

	public InterfaceWrapper() {
		frame = new JFrame("Main Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLayout(new BorderLayout());
	}

	// Creates and displays the menu
	public void displayMenu(List<String> optionsList, String menu_name, char last_zero, int debug) {
		cleanWindow();

		try {
			int choice = Menu.getInstance().menu(optionsList, menu_name, last_zero, debug);
			if (choice != -1) {
				JOptionPane.showMessageDialog(frame, STR."You selected option \{choice}");
			} else {
				JOptionPane.showMessageDialog(frame, "No option was selected.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Clears the current content of the window (for reuse)
	public void cleanWindow() {
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();
	}

	// Displays the window
	public void showWindow() {
		frame.setVisible(true);
	}
}
