import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.Serial;
import java.io.Serializable; // to save in binary
import java.util.ArrayList;

public class Player implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control

	private int id;
	private String __name;
	private int age;

	public Player(String name, int age, int id) {
		this.__name = name;
		if (!(age >= Settings.getInstance().core.minimumPlayerAge) || !(age <= Settings.getInstance().core.maxPlayerAge)) {
			this.age = 1;
			throw new IllegalArgumentException(STR."Player age must be between \{Settings.getInstance().core.minimumPlayerAge} and \{Settings.getInstance().core.maxPlayerAge}!");
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

            //Title Label
            JLabel titleLabel = new JLabel("NEW PLAYER CREATION", SwingConstants.CENTER);
            Font old = titleLabel.getFont();
            titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
            int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
            titleLabel.setBounds(base_center - 250, 10, 500, 30);
            main_content.add(titleLabel);

			// Player ID field
			JLabel idLabel = new JLabel("Player ID:");
			gbc.gridx = 0;
			gbc.gridy = 1;
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
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(nameField, gbc);

			JTextField tfName = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfName, gbc);

			// Player Age field
			JLabel ageLabel = new JLabel("Age:");
			gbc.gridx = 0;
			gbc.gridy = 3;
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
				String _name2 = tfName.getText();
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
    public static Player editPlayerGUI() {
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

        ArrayList<Integer> temp = Database.getInstance().listPlayers(Main.RUNNING_MODE == Main.DEBUG);
        Integer[] temp2 = new Integer[temp.size()];
        temp.toArray(temp2);
        if (temp.isEmpty()) {
            InterfaceWrapper.showErrorWindow("No Player to Edit found! Please register a Player and try again!");
            return null;
        }

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

            // Title label
            JLabel titleLabel = new JLabel("EDIT PLAYER", SwingConstants.CENTER);
            Font old = titleLabel.getFont();
            titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            main_content.add(titleLabel);

            // Player ID field
            JLabel idLabel = new JLabel("Player ID:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(idLabel, gbc);

            // Prepopulate with next ID in the chain
            JComboBox<Integer> CPID = new JComboBox<>(temp2);
            CPID.setSelectedItem(null);
            CPID.setEditable(false);  // Initially non-editable
            gbc.gridx = 1;
            main_content.add(CPID, gbc);

            // Player Name field
            JLabel nameField = new JLabel("Name:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(nameField, gbc);

            JTextField tfName = new JTextField();
            gbc.gridx = 1;
            main_content.add(tfName, gbc);

            // Player Age field
            JLabel ageLabel = new JLabel("Age:");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(ageLabel, gbc);

            SpinnerModel yearModel = new SpinnerNumberModel( 18, Settings.getInstance().core.minimumPlayerAge, Settings.getInstance().core.maxPlayerAge, 1); // Start, Min, Max, Step
            JSpinner ageSpinner = new JSpinner(yearModel);
            // removes the comma separator in 2025 (was showing 2,025)
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(ageSpinner, "#");
            ageSpinner.setEditor(editor);
            gbc.gridx = 1;
            main_content.add(ageSpinner, gbc);

            // make the update function.
            CPID.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (itemEvent.getStateChange() == ItemEvent.SELECTED){
                        Player temp_pl = Database.getInstance().loadPlayer((Integer) itemEvent.getItem());
                        tfName.setText(temp_pl.getName());
                        ageSpinner.setValue(temp_pl.getAge());
                        main_content.revalidate();
                        main_content.repaint();
                    }
                }
            });

            ActionListener _main = _ -> {
                String _name2 = tfName.getText();
                if (_name2 == null || _name2.isEmpty()) {
                    InterfaceWrapper.showErrorWindow("Player name is empty!");
                    return;
                }

                int age = (Integer) ageSpinner.getValue();

                String _id = CPID.getSelectedItem().toString();
                if (Database.getInstance().loadPlayer(Integer.parseInt(_id)) == null) {
                    InterfaceWrapper.showErrorWindow("Player ID does not exist! Use \"Add Player\" or choose another ID!");
                    return;
                }

                player[0] = new Player(_name2, age, Integer.parseInt(_id));
                System.out.println(STR."Player Edited: \{_id}, \{_name2}, \{age}");

                exit_mode[0] = 1;
            };
            ActionListener _scnd = _ -> {
                exit_mode[0] = 2;
            };

            // Submit and Cancel buttons
            JButton submitButton = new JButton("Apply Changes");
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
    public static void deletePlayerGUI() {
        final int[] exit_mode = {0};
        // get GUI handler instance
        InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
        // content panel
        ContentPanel main_content = interfaceWrapper.getContentSpace();
        main_content.setLayout(null); // Using absolute positioning
        // for button reassignment
        ControlPanel controls = interfaceWrapper.getControlSpace();

        CircularButton return_btn = controls.getButton("Return");
        CircularButton accept_btn = controls.getButton("Accept");
        CircularButton reject_btn = controls.getButton("Reject");

        ArrayList<Integer> listPlayers = Database.getInstance().listPlayers(Main.RUNNING_MODE == Main.DEBUG);
		if (listPlayers.isEmpty()){
                InterfaceWrapper.showErrorWindow("No Players were found!\nPlease add one before proceeding");
                return;
            }
        ArrayList<String> listPlayerIDthNames = new ArrayList<>(0);
        for ( Integer id : listPlayers ){
            Player player_to_read = Database.getInstance().loadPlayer(id);
            listPlayerIDthNames.add(STR."\{id} -> \{player_to_read.getName()}");
        }
        listPlayers = null; // tell garbage collector to move it's virtual a$$ and free the memory, hopefully

        SwingUtilities.invokeLater(() -> {
            return_btn.removeActions();
            accept_btn.removeActions();
            reject_btn.removeActions();

            main_content.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components

            // Title label
            JLabel titleLabel = new JLabel("PLAYER DELETION", SwingConstants.CENTER);
            Font old = titleLabel.getFont();
            titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
            int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
            titleLabel.setBounds(base_center - 250, 10, 500, 30);
            main_content.add(titleLabel);

            // Player selector
            JLabel gameField = new JLabel("Player ID:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(gameField, gbc);

            // get every game from last id back, then load name from id
            ArrayList<String> listMachines = new ArrayList<>(0);
            for (int i = 1; i<Settings.getInstance().core.next_machine_id; i++){
                GameMachine machine_to_read = Database.getInstance().loadMachine(i);
                listMachines.add(machine_to_read.getName());
                }
            System.out.println(listMachines);
            //dropdown selector
            JComboBox<String> player_box = new JComboBox(listPlayerIDthNames.toArray());
            player_box.setEditable(false);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(player_box, gbc);

			//TODO: Error catching, scary confirmation window

			ActionListener _main = _ -> {
				int id = Integer.parseInt(player_box.getSelectedItem().toString().split(" -> ")[0]);
				System.out.println(STR."Attempting to remove player with id: \{id}");
				File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.playerSubDirectory}/\{id}.plr");
				if (!file.delete()) {
					InterfaceWrapper.showErrorWindow("Failed to DELETE the player (file failed to delete)!");
					return;
				}
				file = null;
				exit_mode[0] = 1;
			};
			ActionListener _scnd = _ -> {
				exit_mode[0] = 2;
			};

			// Submit button
			JButton submitButton = new JButton("DELETE PLAYER DATA");
			gbc.gridx = 1;
			gbc.gridy = 8;
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

		// Return to caller
		System.out.println("Returning...");
		return;
	}
}
