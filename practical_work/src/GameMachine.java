import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable; // to save in binary

//NOTE TO SELF, TO DELETE MACHINE OR WTV, JUST RM FILE

public class GameMachine implements Serializable {// the machines in the arcade. NO SLOTS OR GAMBLING, only skill games
    @Serial
    private static final long serialVersionUID = 1L; // For serialization version control

    // TODO: to make basically the whole "meat" of the class
    private int id;
    private String __name;

	Controls control_scheme;
	Game game_in_machine;
	MACHINE_STATE availabilityStatus;
	int available_tickets;

    public GameMachine(String name, Controls scheme, Game game_in_machine, int id, int tickets, MACHINE_STATE state){
        this.__name = name;
        this.control_scheme = scheme;
        this.game_in_machine = game_in_machine;
        this.id = id;
        if(tickets<0){
            this.available_tickets = 0;
            state = MACHINE_STATE.OUT_OF_TICKETS;
            throw new IllegalArgumentException("Ticket amount must be greater than 0");
        }
        this.available_tickets = tickets;
        //TODO: check if state is one of the values in enum
        //TODO: if state was supposed to be MAINTENANCE, OUT OF ORDER, COMING SOON, then don't set to OUT OF TICKETS despite 0 tickets
        this.availabilityStatus = state;
    }

    public GameMachine(String name, Controls scheme, Game game_in_machine){
        this.__name = name;
        this.control_scheme = scheme;
        this.game_in_machine = game_in_machine;
        this.id = Settings.getInstance().core.next_machine_id;
        Settings.getInstance().core.next_machine_id++;
        Database.getInstance().saveSettings(Main.SETTINGS_FILE);
        this.available_tickets = 0;
        this.availabilityStatus = MACHINE_STATE.OUT_OF_ORDER;
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

            // Player ID field
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

            // Machine state selector
            JComboBox<MACHINE_STATE> state = new JComboBox(MACHINE_STATE.values());
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(state, gbc);



            ActionListener _main = _ -> {
                // FIXME: it's not stopping on name empty... but in game yes... Why?!?
                String _name2 = nameField.getText();
                if (_name2 == null || _name2.isEmpty()) {
                    InterfaceWrapper.showErrorWindow("Machine name is empty!");
                    return;
                }

                String _id = tfID.getText();
                if (_id == null || _id.isEmpty() || _id.equals("0")) {
                    InterfaceWrapper.showErrorWindow("Machine ID is not valid!");
                    return;
                }
                if (Database.getInstance().loadPlayer(Integer.parseInt(_id)) != null) {
                    InterfaceWrapper.showErrorWindow("Machine ID already exists! Choose another ID!");
                    return;
                }

                // Create a new machine object with the data   NOTE: with overwrite, it does not go up
               // if (cbManualOverride.isSelected()) machine[0] = new GameMachine(_name2, scheme, machine_game, Integer.parseInt(_id), Integer.parseInt(tickets), state);
             //   else machine[0] = new GameMachine(_name2, scheme, machine_game); // default state, tickets, auto id

             //   System.out.println(STR."Machine Created: \{_id}, \{_name2}, \{scheme}, \{machine_game}, \{tickets}, \{state}");

                exit_mode[0] = 1;
            };
            ActionListener _scnd = _ -> {
                exit_mode[0] = 2;
            };

            // Submit and Cancel buttons
            JButton submitButton = new JButton("Create Machine");
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
        if (exit_mode[0] == 1) return machine[0];
        else return null; // return null if user choose to cancel game input
    }
}
