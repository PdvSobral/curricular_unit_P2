import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serial;
import java.io.Serializable; // to save in binary
import java.util.ArrayList;

// TODO: save machine to file
// TODO: no checks on inputs in GUI
//NOTE TO SELF, TO DELETE MACHINE OR WTV, JUST RM FILE

public class GameMachine implements Serializable {// the machines in the arcade. NO SLOTS OR GAMBLING, only skill games
    @Serial
    private static final long serialVersionUID = 1L; // For serialization version control


    private int id;
    private String __name;

	private Controls control_scheme;
	private Game game_in_machine;
	private MACHINE_STATE availabilityStatus;
	int available_tickets;

    public int getId() {return id;}
    public String getName() {return __name;}
    public Game getGame_in_machine() {return game_in_machine;}
    public Controls getControl_scheme() {return control_scheme;}
    public int getAvailable_tickets() {return available_tickets;}
    public MACHINE_STATE getAvailabilityStatus() {return availabilityStatus;}

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

    public GameMachine(String name, Controls scheme, Game game_in_machine, int tickets, MACHINE_STATE state){
        this.__name = name;
        this.control_scheme = scheme;
        this.game_in_machine = game_in_machine;
        this.id = Settings.getInstance().core.next_machine_id;
        Settings.getInstance().core.next_machine_id++;
        Database.getInstance().saveSettings(Main.SETTINGS_FILE);
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

            ArrayList<String> listGames = new ArrayList<>(0);
            for (int i = 1; i<Settings.getInstance().core.next_game_id; i++){
                Game game_to_read = Database.getInstance().loadGame(i);
                listGames.add(game_to_read.getName());
            }
            System.out.println(listGames);

            JComboBox<String> game_box = new JComboBox(listGames.toArray());
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
            JComboBox<MACHINE_STATE> state_box = new JComboBox(MACHINE_STATE.values());
            game_box.setEditable(false);
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(state_box, gbc);

            // Tickets field
            JLabel ticketsLabel = new JLabel("Tickets in the Machine:");
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(ticketsLabel, gbc);

            JTextField ticketsField = new JTextField("0");
            gbc.gridx = 1;
            main_content.add(ticketsField, gbc);

            ActionListener _main = _ -> {
                // FIXME: it's not stopping on name empty... but in game yes... Why?!?
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

                String _tickets = ticketsField.getText();
                MACHINE_STATE state = (MACHINE_STATE) state_box.getSelectedItem();
                Controls scheme = (Controls) control_box.getSelectedItem();
                Game machine_game = Database.getInstance().loadGame(game_box.getSelectedIndex()+1); //+1 because index for ID starts from 1, in the combo box starts from 0
                // Create a new machine object with the data   NOTE: with overwrite, it does not go up
                if (cbManualOverride.isSelected()) machine[0] = new GameMachine(_name2, scheme, machine_game, Integer.parseInt(_id), Integer.parseInt(_tickets), state);
                else machine[0] = new GameMachine(_name2, scheme, machine_game, Integer.parseInt(_tickets), state); // default state, tickets, auto id

                System.out.println(STR."Machine Created: \{_id}, \{_name2}, \{control_box.getSelectedItem()}, \{game_box.getSelectedItem()}, \{_tickets}, \{state_box.getSelectedItem()}");

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

    public static GameMachine deleteMachineGUI() {
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
            JComboBox<String> machine_box = new JComboBox(listMachines.toArray());
            machine_box.setEditable(false);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(machine_box, gbc);

            //TODO: Error catching, scary confirmation window

            ActionListener _main = _ -> {
                int id = machine_box.getSelectedIndex()+1;
                String filename = STR."\{id}.mch";
                GameMachine target_machine = Database.getInstance().loadMachine(id); //+1 because index for ID starts from 1, in the combo box starts from 0
                File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{filename}");
                file.delete();

                exit_mode[0] = 1;
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

        // reset buttons, just in case
        SwingUtilities.invokeLater(() -> {
            return_btn.removeActions();
            accept_btn.removeActions();
            reject_btn.removeActions();
        });

        // Return the created Game object after the user submits, if valid
        if (exit_mode[0] == 1) return null;
        else return null; // return null if user choose to cancel game input
    }
}
