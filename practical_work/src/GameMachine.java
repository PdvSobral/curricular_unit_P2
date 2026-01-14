import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serial;
import java.io.Serializable; // to save in binary
import java.util.ArrayList;
import java.util.Arrays;

// Stig27 -> NOTE TO SELF, TO DELETE MACHINE OR WTV, JUST RM FILE

public class GameMachine implements Serializable {// the machines in the arcade. NO SLOTS OR GAMBLING, only skill games
    @Serial
    private static final long serialVersionUID = 1L; // For serialization version control

    private int id;
    private String __name;
	private Controls control_scheme;
	private int game_id;
	private MACHINE_STATE availability_status;
	private int available_tickets;

	private void __GameMachine(String name, Controls scheme, int game_in_machine, int id, int tickets, MACHINE_STATE state) {
		this.__name = name;
		this.control_scheme = scheme;
		this.game_id = game_in_machine;
		this.id = id;
		setAvailable_tickets(tickets);
		// Stig, não percebi esta cena que antes era to-do, passei a não
		// if state was supposed to be MAINTENANCE, OUT OF ORDER, COMING SOON, then don't set to OUT OF TICKETS despite 0 tickets
		this.availability_status = state;
	}
	public GameMachine(String name, Controls scheme, int game_in_machine, int id, MACHINE_STATE state){
		this.__GameMachine(name, scheme, game_in_machine, id, Settings.getInstance().core.defaultMachineTickets, state);
	}
	public GameMachine(String name, Controls scheme, int game_in_machine, int id, int tickets, MACHINE_STATE state){
		this.__GameMachine(name, scheme, game_in_machine, id, tickets, state);
    }
	public GameMachine(String name, Controls scheme, int game_in_machine, MACHINE_STATE state, int tickets){
		this.__GameMachine(name, scheme, game_in_machine, Settings.getInstance().core.next_player_id, tickets, state);
		Settings.getInstance().core.next_player_id++;
		Database.getInstance().saveSettings(Main.SETTINGS_FILE);
	}
    public GameMachine(String name, Controls scheme, int game_in_machine){
		this.__GameMachine(name, scheme, game_in_machine, Settings.getInstance().core.next_player_id, Settings.getInstance().core.defaultMachineTickets, Settings.getInstance().core.defaultMachineState);
		Settings.getInstance().core.next_player_id++;
		Database.getInstance().saveSettings(Main.SETTINGS_FILE);
	}
	public GameMachine(String name, Controls scheme, int game_in_machine, int id){
		this.__GameMachine(name, scheme, game_in_machine, id, Settings.getInstance().core.defaultMachineTickets, Settings.getInstance().core.defaultMachineState);
	}

	public int getId(){ return id; }
	public String getName(){ return __name; }
	public void setName(String __name){ this.__name = __name; }
	public Controls getControlScheme(){ return control_scheme; }
	public int getGame(){ return game_id; }
	public MACHINE_STATE getAvailabilityStatus(){ return availability_status; }
	public void setAvailabilityStatus(MACHINE_STATE availability_status){ this.availability_status = availability_status; }
	public int getAvailable_tickets(){ return available_tickets; }
	public void setAvailable_tickets(int tickets){
		if(tickets<0){
			throw new IllegalArgumentException("Ticket amount must be greater than 0");
		}
		this.available_tickets = tickets;
	}

	public void save(){ Database.getInstance().saveGameMachine(this); }
	public void save(String file_name){ Database.getInstance().saveGameMachine(this, file_name); }

	// Method to display game information. Overrides normal function
	@Override
	public String toString() {
		return STR."GameMachine@\{Integer.toHexString(hashCode())}{id=\{id}, name='\{__name}', game id='\{game_id}', controls='\{control_scheme}', state='\{availability_status}', tickets left='\{available_tickets}'}";
	}

	public static GameMachine createMachineGUI(){
        final int[] exit_mode = {0};
        // Declare the Game object that will be returned
        final GameMachine[] machine = new GameMachine[1];  // Using an array to modify within the lambda

        // get GUI handler instance
        InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
        // content panel
        ContentPanel main_content = interfaceWrapper.getContentSpace();

        // for button reassignment
        ControlPanel controls = interfaceWrapper.getControlSpace();

        CircularButton return_btn = controls.getButton("Return");
        CircularButton accept_btn = controls.getButton("Accept");
        CircularButton reject_btn = controls.getButton("Reject");

		ArrayList<Integer> listGames = Database.getInstance().listGames(Main.RUNNING_MODE == Main.DEBUG);
		if (listGames == null){
			InterfaceWrapper.showErrorWindow("No Games were found!\nPlease add a game before proceeding");
			return null;
		}
		ArrayList<String> listGamesWithNames = new ArrayList<>(0);
		for ( Integer id : listGames ){
			Game game_to_read = Database.getInstance().loadGame(id);
			listGamesWithNames.add(STR."\{id} -> \{game_to_read.getName()}");
		}
		listGames = null; // tell garbage collector to move it's virtual a$$ and free the memory, hopefully

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
			JLabel titleLabel = new JLabel("NEW MACHINE CREATION", SwingConstants.CENTER);
			Font old = titleLabel.getFont();
			titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
			int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
			titleLabel.setBounds(base_center - 250, 10, 500, 30);
			main_content.add(titleLabel);

			// Machine ID field
			JLabel idLabel = new JLabel("Machine ID:");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(idLabel, gbc);

			// Prepopulate with next ID in the chain
			JTextField tfID = new JTextField(String.valueOf(Settings.getInstance().core.next_machine_id));
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
					tfID.setText(String.valueOf(Settings.getInstance().core.next_machine_id)); // Set the default value
				}
			});

			// Machine Name field
			JLabel nameField = new JLabel("Name:");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(nameField, gbc);

			JTextField tfName = new JTextField();
			gbc.gridx = 1;
			main_content.add(tfName, gbc);

			// Machine game selector
			JLabel gameField = new JLabel("Game in the machine:");
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(gameField, gbc);
			// get every game from last id back, then load name from id, combine strings
			//dropdown selector

			JComboBox<String> game_box = new JComboBox(listGamesWithNames.toArray());
			game_box.setEditable(false);
			gbc.gridx = 1;
			gbc.gridy = 3;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(game_box, gbc);

			// Machine control type selector
			JLabel controlField = new JLabel("Control Type:");
			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(controlField, gbc);

			//dropdown selector
			JComboBox<Controls> control_box = new JComboBox(Controls.values());
			game_box.setEditable(false);
			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(control_box, gbc);

			// Machine state selector
			JLabel stateField = new JLabel("Status:");
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(stateField, gbc);

			//dropdown selector
			JComboBox<MACHINE_STATE> state_box = new JComboBox(Arrays.stream(MACHINE_STATE.values()).filter(
				state -> state != MACHINE_STATE.OUT_OF_TICKETS && state != MACHINE_STATE.MAINTENANCE && state != MACHINE_STATE.IN_USE
			).toArray());
			game_box.setEditable(false);
			gbc.gridx = 1;
			gbc.gridy = 5;
			gbc.anchor = GridBagConstraints.EAST;
			main_content.add(state_box, gbc);


			ActionListener _main = _ -> {
				String _name2 = tfName.getText();
				if (_name2 == null || _name2.isEmpty()) {
					InterfaceWrapper.showErrorWindow("Machine name is empty!");
					return;
				}
				String _id = tfID.getText();
				if (_id == null || _id.isEmpty() || _id.equals("0")) {
					InterfaceWrapper.showErrorWindow("Machine ID is not valid!");
					return;
				}
				MACHINE_STATE state = (MACHINE_STATE) state_box.getSelectedItem();
				Controls scheme = (Controls) control_box.getSelectedItem();
				int game_id = Integer.parseInt(game_box.getSelectedItem().toString().split(" -> ")[0]);

                if (cbManualOverride.isSelected()) machine[0] = new GameMachine(_name2, scheme, game_id, Integer.parseInt(_id), state);
                else machine[0] = new GameMachine(_name2, scheme, game_id); // default state, tickets, auto id
				System.out.println(STR."Machine Created: \{_id}, \{_name2}, \{scheme}, \{game_id}, \{state}");

				exit_mode[0] = 1;
			};
			ActionListener _scnd = _ -> {
				exit_mode[0] = 2;
			};

			// Submit and Cancel buttons
			JButton submitButton = new JButton("Create Machine");
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

		// Return the created Machine object after the user submits, if valid
		if (exit_mode[0] == 1){
			Database.getInstance().saveMachine(machine[0]);
			return machine[0];
		}
		else return null; // return null if user choose to cancel game input
	}
    public static void deleteMachineGUI() {
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

		ArrayList<Integer> listMachine = Database.getInstance().listGameMachine(Main.RUNNING_MODE == Main.DEBUG);
        if (listMachine.isEmpty()) {
            InterfaceWrapper.showErrorWindow("No Machines were found!\nPlease add one before proceeding");
            return;
        }
		ArrayList<String> listMachinesWithNames = new ArrayList<>(0);
		for ( Integer id : listMachine ){
			GameMachine game_to_read = Database.getInstance().loadGameMachine(id);
			listMachinesWithNames.add(STR."\{id} -> \{game_to_read.getName()}");
		}
		listMachine = null; // tell garbage collector to move it's virtual a$$ and free the memory, hopefully

        SwingUtilities.invokeLater(() -> {
            return_btn.removeActions();
            accept_btn.removeActions();
            reject_btn.removeActions();

            main_content.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components

            // Title label
            JLabel titleLabel = new JLabel("GAME MACHINE DELETION", SwingConstants.CENTER);
            Font old = titleLabel.getFont();
            titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
            int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
            titleLabel.setBounds(base_center - 250, 10, 500, 30);
            main_content.add(titleLabel);

            // Machine game selector
            JLabel gameField = new JLabel("Game Machine to be DELETED:");
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
            JComboBox<String> machine_box = new JComboBox(listMachinesWithNames.toArray());
            machine_box.setEditable(false);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(machine_box, gbc);

            //TODO: Error catching

            ActionListener _main = _ -> {
                int confirm = JOptionPane.showOptionDialog(null, "Machine deletion is irreversible, are you sure of what you're doing?",
                        "WARNING",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,null, JOptionPane.CANCEL_OPTION);
                if (confirm==JOptionPane.CANCEL_OPTION)
                    exit_mode[0]=0;
                   else{
                       int id = Integer.parseInt(machine_box.getSelectedItem().toString().split(" -> ")[0]);
                       System.out.println(STR."Attempting to remove machine of id: \{id}");
                       File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{id}.mch");
                    if (!file.delete()) {
					    InterfaceWrapper.showErrorWindow("Failed to remove the machine (file failed to delete)!");
					    return;
                    }
				    file = null;
                    exit_mode[0] = 1;
                    }
            };
            ActionListener _scnd = _ -> {
                exit_mode[0] = 2;
            };

            // Submit button
            JButton submitButton = new JButton("DELETE GAME MACHINE");
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
    //*/
}
