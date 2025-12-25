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
		if (debug == Main.DEBUG) optionsList.addFirst("DEBUG");

		// Get the singleton instance of InterfaceWrapper
		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();

		// Get the content panel to hold the buttons
		ContentPanel panel = interfaceWrapper.getContentSpace();
		panel.setLayout(null);
		panel.clearPanel(); // Clear the existing content of the window

		final int height_step = 30;
		SwingUtilities.invokeLater(() -> {
			int current_height = 40; // height da label + offset buttons para a label + space buttons-label

			// Set up the menu name
			JLabel menuLabel = new JLabel(menu_name, SwingConstants.CENTER);
			Font old = menuLabel.getFont();
			menuLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
			int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
			menuLabel.setBounds(base_center - 100, 10, 200, 30);
			panel.add(menuLabel);

			// Create a button for each option
			int buttonIndex = 1;  // Start button index from 1 (1-based index)

			if (debug == Main.DEBUG) {
				JButton debugButton = new JButton(optionsList.removeFirst());
				debugButton.setActionCommand(String.valueOf(Main.DEBUG)); // Set the action command to the debug option
				debugButton.addActionListener(e -> {
					selectedOption[0] = Integer.parseInt(e.getActionCommand()); // Store the selected option
				});
				debugButton.setBounds(base_center - 100, current_height, 200, 30);
				panel.add(debugButton);
				current_height += height_step;
			}
			for (String option : optionsList) {
				JButton button = new JButton(option);
				if (buttonIndex == optionsList.size() && last_zero == 1) button.setActionCommand("0");
				else button.setActionCommand(String.valueOf(buttonIndex)); // Set the action command to the option's index
				button.addActionListener(e -> {
					// Action when a button is clicked
					selectedOption[0] = Integer.parseInt(e.getActionCommand());  // Store the selected option
				});
				button.setBounds(base_center - 100, current_height, 200, 30);
				panel.add(button);
				current_height += height_step;
				buttonIndex++;
			}
		});

		// Update the window with the updated panel (on EDT)
		SwingUtilities.invokeLater(() -> {
			panel.revalidate();
			panel.repaint();
			interfaceWrapper.getFrame().revalidate();
			interfaceWrapper.getFrame().repaint();
		});

		// NOTE: If getButton is inside EDT, it causes a deadlock. maybe later change to a callback event handler.
		// For now this works
		CircularButton return_btn = InterfaceWrapper.getInstance().getControlSpace().getButton("Return");
		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			if (last_zero == 1) return_btn.setActionCommand("0");
			else return_btn.setActionCommand(String.valueOf(optionsList.size()));
			return_btn.addActionListener(e -> {
				// Action when a button is clicked
				selectedOption[0] = Integer.parseInt(e.getActionCommand());  // Store the selected option
			});
		});

		// Wait for the user to select an option
		while (selectedOption[0] == -1) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		InterfaceWrapper.getInstance().getContentSpace().clearPanel();
		InterfaceWrapper.getInstance().getControlSpace().getButton("Return").removeActions();

		return selectedOption[0];
	}
}
