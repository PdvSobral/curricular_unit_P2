import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
	private static final Menu __instance = new Menu();
	private final int[] selectedOption = {-1};  // Store the selected option index

	// Private constructor to ensure singleton
	private Menu() {}

	// Get the singleton instance of Menu
	public static Menu getInstance() { return __instance; }

	// Returns the selected option index (1-based) or -1 if canceled or invalid input
	public int menu(List<String> optionsList, String menu_name, char last_zero) throws IOException {
		return this.menu(optionsList, menu_name, last_zero, 0);
	}

	public int menu(List<String> _optionsList, String menu_name, char last_zero, int debug) throws IOException {
		selectedOption[0] = -1;
		ArrayList<String> optionsList = new ArrayList<>(_optionsList);
		// Display the menu name and the options
		if (debug == Main.DEBUG) {
			optionsList.addFirst("DEBUG");
		}

		// Get the singleton instance of InterfaceWrapper
		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		interfaceWrapper.cleanWindow(); // Clear the existing content of the window

		// Create a panel to hold the buttons
		JPanel panel = new JPanel();
		panel.setLayout(null);  // Absolute Layout

		final int height_step = 30;
		int current_height = 40; // height da label  + offset buttons para a label + space buttons-label

		// Set up the window title and/or menu name
		interfaceWrapper.getFrame().setTitle(menu_name);
		JLabel menuLabel = new JLabel(menu_name, SwingConstants.CENTER);
		menuLabel.setBounds(((Main.WINDOW_WIDTH - Main.BORDER_LOSS)/2) - 100, 10, 200, 30);
		panel.add(menuLabel);


		// Create a button for each option
		int buttonIndex = 1;  // Start button index from 1 (1-based index)

		// Declare dialog variable here to be accessible in the action listener
		JDialog dialog = new JDialog();  // Create a new JDialog window

		if (debug==Main.DEBUG) {
			JButton button = new JButton(optionsList.removeFirst());
			button.setActionCommand(String.valueOf(Main.DEBUG)); // Set the action command to the option's index
			button.addActionListener(e -> {
				// Action when a button is clicked
				selectedOption[0] = Integer.parseInt(e.getActionCommand()); // Store the selected option
			});
			button.setBounds(((Main.WINDOW_WIDTH - Main.BORDER_LOSS)/2) - 100, current_height, 200, 30);
			panel.add(button);
			current_height += height_step;
		}
		for (String option : optionsList) {
			JButton button = new JButton(option);
			if (buttonIndex == optionsList.size() && last_zero == 1) button.setActionCommand("0");
			else button.setActionCommand(String.valueOf(buttonIndex));  // Set the action command to the option's index
			button.addActionListener(e -> {
				// Action when a button is clicked
				selectedOption[0] = Integer.parseInt(e.getActionCommand());  // Store the selected option
				updateWindow(panel);  // Update the window content based on the selection
			});
			button.setBounds(((Main.WINDOW_WIDTH - Main.BORDER_LOSS)/2) - 100, current_height, 200, 30);
			panel.add(button);
			current_height += height_step;
			buttonIndex++;
		}

		// Update the window with the newly created panel
		interfaceWrapper.getFrame().getContentPane().add(panel);
		interfaceWrapper.getFrame().revalidate();
		interfaceWrapper.getFrame().repaint();

		// Wait for the user to select an option
		while (selectedOption[0] == -1) {
			// Keep the dialog open until a selection is made
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Return the selected option value
		return selectedOption[0];
	}

	// Update the window content when an option is selected
	private void updateWindow(JPanel panel) {
		// Clear the panel and revalidate to refresh the display
		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		interfaceWrapper.cleanWindow();
		interfaceWrapper.getFrame().getContentPane().add(panel);
		interfaceWrapper.getFrame().revalidate();
		interfaceWrapper.getFrame().repaint();
	}
}
