import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable; // to save in binary

public class Player implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control

	// TODO: to make basically the whole "meat" of the class
	private int id;
	private String name;
	private int age;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	// FIXME: prototype
	public static Player createPlayerGUI() {
		final int[] exit_mode = {0};
		// Declare the Game object that will be returned
		final Player[] player = new Player[1];  // Using an array to modify within the lambda

		// get GUI handler instance
		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		// content panel
		ContentPanel main_content = interfaceWrapper.getContentSpace();

		// for button reassignment
		ControlPanel controls = interfaceWrapper.getControlSpace();

		CircularButton return_btn = controls.getButton("Return");
		CircularButton accept_btn = controls.getButton("Accept");
		CircularButton reject_btn = controls.getButton("Reject");

		// reset buttons, just in case
		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			accept_btn.removeActions();
			reject_btn.removeActions();

			// Set up GridBagLayout for the panel
			main_content.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components

			// Player ID field
			JLabel idLabel = new JLabel("Player ID:");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(idLabel, gbc);

			// Prepopulate with next ID in the chain
			JTextField tfID = new JTextField(String.valueOf(Settings.getInstance().core.next_player_id));
			tfID.setEditable(false);  // Initially non-editable
			gbc.gridx = 1;
			main_content.add(tfID, gbc);

			JCheckBox cbManualOverride = new JCheckBox("Manual Override ID");
			gbc.gridx = 2;
			main_content.add(cbManualOverride, gbc);

			// Player Name field
			JLabel nameLabel = new JLabel("Name:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(nameLabel, gbc);

			JTextField tfName = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfName, gbc);

			// Player Age field
			JLabel ageLabel = new JLabel("Age:");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(ageLabel, gbc);

			JTextField tfAge = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfAge, gbc);

			// Player Score field
			JLabel scoreLabel = new JLabel("Score:");
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(scoreLabel, gbc);

			JTextField tfScore = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfScore, gbc);

			// Action listener to enable/disable ID editing based on checkbox
			cbManualOverride.addActionListener(e -> tfID.setEditable(cbManualOverride.isSelected()));

			// Submit and Cancel buttons
			JButton submitButton = new JButton("Submit");
			gbc.gridx = 1;
			gbc.gridy = 4;
			main_content.add(submitButton, gbc);

			JButton cancelButton = new JButton("Cancel");
			gbc.gridx = 0;
			main_content.add(cancelButton, gbc);

			// Action listener for submit button
			submitButton.addActionListener(e -> {
				String playerID = tfID.getText();
				if (!cbManualOverride.isSelected() && playerID.equals("12345")) {
					JOptionPane.showMessageDialog(null, "Player ID cannot be default unless manually overridden.");
					return;
				}

				String playerName = tfName.getText();
				String playerAgeStr = tfAge.getText();
				String playerScoreStr = tfScore.getText();

				if (playerName.isEmpty() || playerAgeStr.isEmpty() || playerScoreStr.isEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields must be filled!");
					return;
				}

				int playerAge = Integer.parseInt(playerAgeStr);
				int playerScore = Integer.parseInt(playerScoreStr);

				// Create the Player object (you can use this data as needed)
				System.out.println("Player Created: ID=" + playerID + ", Name=" + playerName + ", Age=" + playerAge + ", Score=" + playerScore);

				// Close or perform any action on success
				JOptionPane.showMessageDialog(null, "Player Data Submitted!");
			});

			// Action listener for cancel button
			cancelButton.addActionListener(e -> {
				// Handle cancellation (like clearing the form or closing the window)
				JOptionPane.showMessageDialog(null, "Player Creation Cancelled!");
			});

			main_content.revalidate();
			main_content.repaint();
		});

		while (exit_mode[0] == 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			accept_btn.removeActions();
			reject_btn.removeActions();
		});

		// Return the created Game object after the user submits, if valid
		if (exit_mode[0] == 1) return player[0];
		else return null; // return null if user choose to cancel game input
	}
}
