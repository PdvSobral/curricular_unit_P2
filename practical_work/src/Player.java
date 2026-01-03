import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable; // to save in binary

public class Player implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control

	// TODO: to make basically the whole "meat" of the class
	private int id;
	private String __name;
	private int age;

	public Player(String name, int age, int id) {
		this.__name = name;
		if (!(age >= Settings.getInstance().core.minimumPlayerAge) || !(age <= Settings.getInstance().core.maxPlayerAge)) {
			this.age = 1;
			throw new IllegalArgumentException("Game year must be between 1970 and the current year!");
		}
		this.age = age;
		this.id = id;
	}

	public Player(String name, int age) {
		this.__name = name;
		if (!(age >= Settings.getInstance().core.minimumPlayerAge) || !(age <= Settings.getInstance().core.maxPlayerAge)) {
			this.age = 1;
			throw new IllegalArgumentException("Game year must be between 1970 and the current year!");
		}
		this.age = age;
		this.id = Settings.getInstance().core.next_player_id;
		Settings.getInstance().core.next_player_id++;
		Database.getInstance().saveSettings(Main.SETTINGS_FILE);
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getName() { return __name; }
	public void setName(String name) { this.__name = name; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	public void save(){ Database.getInstance().savePlayer(this); }
	public void save(String file_name){ Database.getInstance().savePlayer(this, file_name); }

	// Method to display game information. Overrides normal function
	@Override
	public String toString() {
		return STR."Player@\{Integer.toHexString(hashCode())}{id=\{id}, name='\{__name}', age='\{age}'}";
	}

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

			// Action listener to enable/disable ID editing based on checkbox
			cbManualOverride.addActionListener(e -> {
				tfID.setEditable(cbManualOverride.isSelected());
				if (!cbManualOverride.isSelected()) {
					tfID.setText(String.valueOf(Settings.getInstance().core.next_player_id)); // Set the default value
				}
			});

			// Player Name field
			JLabel nameField = new JLabel("Name:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(nameField, gbc);

			JTextField tfName = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfName, gbc);

			// Player Age field
			JLabel ageLabel = new JLabel("Age:");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(ageLabel, gbc);

			SpinnerModel yearModel = new SpinnerNumberModel( 18, Settings.getInstance().core.minimumPlayerAge, Settings.getInstance().core.maxPlayerAge, 1); // Start, Min, Max, Step
			JSpinner ageSpinner = new JSpinner(yearModel);
			// removes the comma separator in 2025 (was showing 2,025)
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ageSpinner, "#");
			ageSpinner.setEditor(editor);
			gbc.gridx = 1;
			main_content.add(ageSpinner, gbc);

			ActionListener _main = _ -> {
				// FIXME: it's not stopping on name empty... but in game yes... Why?!?
				String _name2 = nameField.getText();
				if (_name2 == null || _name2.isEmpty()) {
					InterfaceWrapper.showErrorWindow("Player name is empty!");
					return;
				}

				int age = (Integer) ageSpinner.getValue();

				String _id = tfID.getText();
				if (_id == null || _id.isEmpty() || _id.equals("0")) {
					InterfaceWrapper.showErrorWindow("Player ID is not valid!");
					return;
				}
				if (Database.getInstance().loadPlayer(Integer.parseInt(_id)) != null) {
					InterfaceWrapper.showErrorWindow("Player ID already exists! Use \"Edit Player\" or choose another ID!");
					return;
				}

				// Create a new Player object with the data   NOTE: with overwrite, it does not go up
				if (cbManualOverride.isSelected()) player[0] = new Player(_name2, age, Integer.parseInt(_id));
				else player[0] = new Player(_name2, age); // so it goes up

				System.out.println(STR."Player Created: \{_id}, \{_name2}, \{age}");

				exit_mode[0] = 1;
			};
			ActionListener _scnd = _ -> {
				exit_mode[0] = 2;
			};

			// Submit and Cancel buttons
			JButton submitButton = new JButton("Create Player");
			gbc.gridx = 1;
			gbc.gridy = 4;
			submitButton.addActionListener(_main);
			main_content.add(submitButton, gbc);

			accept_btn.addActionListener(_main);

			JButton exitButton = new JButton("Cancel");
			gbc.gridx = 0;
			exitButton.addActionListener(_scnd);
			main_content.add(exitButton, gbc);

			reject_btn.addActionListener(_scnd);
			reject_btn.addActionListener(_scnd);

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
