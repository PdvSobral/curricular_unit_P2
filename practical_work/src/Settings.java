import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serial;
import java.io.Serializable; // to save in binary;
import java.util.ArrayList;
import java.util.Arrays;

public class Settings{
	private static Settings instance = new Settings();
	// Public method to get the singleton instance
	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public SettingsCore core;

	// Private constructor to prevent instantiation from other classes
	private Settings() {
		core = new SettingsCore();
	}

	public void reset() {
		core = new SettingsCore();
	}

//FIXME: Gridbag doesn't want to properly display all entries starting from y=6
//FIXME: Needs to recreate folder structure if directories are changed
    public static void editSettingsGUI(){
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
            return;
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
            JLabel titleLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
            Font old = titleLabel.getFont();
            titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            main_content.add(titleLabel);

            // mainDirectory
            JLabel mainDirLabel = new JLabel("Main database directory:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(mainDirLabel, gbc);

            // Prepopulate with current value
            JTextField tfmainDir = new JTextField(Settings.getInstance().core.mainDirectory);
            gbc.gridx = 1;
            main_content.add(tfmainDir, gbc);

            // gameDirectory
            JLabel gameDirLabel = new JLabel("Game directory inside the Main directory:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(gameDirLabel, gbc);

            // Prepopulate with current value
            JTextField tfgameDir = new JTextField(Settings.getInstance().core.gameSubDirectory);
            gbc.gridx = 1;
            main_content.add(tfgameDir, gbc);

            // machineDirectory
            JLabel machineDirLabel = new JLabel("Game Machine directory inside the Main directory:");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(machineDirLabel, gbc);

            // Prepopulate with current value
            JTextField tfmachineDir = new JTextField(Settings.getInstance().core.machineSubDirectory);
            gbc.gridx = 1;
            main_content.add(tfmachineDir, gbc);

            // playerDirectory
            JLabel playerDirLabel = new JLabel("Player directory inside the Main directory:");
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(playerDirLabel, gbc);

            // Prepopulate with current value
            JTextField tfplayerDir = new JTextField(Settings.getInstance().core.playerSubDirectory);
            gbc.gridx = 1;
            main_content.add(tfplayerDir, gbc);

            // score file name
            JLabel scoreNameLabel = new JLabel("Name for leaderboard file:");
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(scoreNameLabel, gbc);

            // Prepopulate with current value
            JTextField tfScoreFileName = new JTextField(Settings.getInstance().core.scoresFileName);
            gbc.gridx = 1;
            main_content.add(tfScoreFileName, gbc);

            // label and spinner for min age
            JLabel minAgeLabel = new JLabel("Minimum Player Age:");
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(minAgeLabel, gbc);
            SpinnerModel minAgeModel = new SpinnerNumberModel( Settings.getInstance().core.minimumPlayerAge, 0, Settings.getInstance().core.maxPlayerAge, 1); // Start, Min, Max, Step
            JSpinner minAgeSpinner = new JSpinner(minAgeModel);
            gbc.gridx = 1;
            gbc.gridy = 6;
            main_content.add(minAgeSpinner, gbc);

            // label and spinner for max age
            JLabel maxAgeLabel = new JLabel("Maximum Player Age:");
            gbc.gridx = 0;
            gbc.gridy = 7;
            main_content.add(maxAgeLabel, gbc);
            SpinnerModel maxAgeModel = new SpinnerNumberModel( Settings.getInstance().core.maxPlayerAge, Settings.getInstance().core.minimumPlayerAge, 140, 1); // Start, Min, Max, Step
            JSpinner maxAgeSpinner = new JSpinner(maxAgeModel);
            gbc.gridx = 1;
            gbc.gridy = 7;
            main_content.add(maxAgeSpinner, gbc);

            // label and spinner for default tickets
            JLabel defaultTicketsLabel = new JLabel("Default Ticket Count:");
            gbc.gridx = 0;
            gbc.gridy = 8;
            main_content.add(defaultTicketsLabel, gbc);
            SpinnerModel defaultTicketsModel = new SpinnerNumberModel( Settings.getInstance().core.defaultMachineTickets, 0, 9999999, 1); // Start, Min, Max, Step
            JSpinner defaultTicketsSpinner = new JSpinner(defaultTicketsModel);
            gbc.gridx = 1;
            gbc.gridy = 8;
            main_content.add(defaultTicketsSpinner, gbc);

            // Machine state selector
            JLabel defaultStateField = new JLabel("Default Machine Status:");
            gbc.gridx = 0;
            gbc.gridy = 9;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(defaultStateField, gbc);

            //dropdown selector
            JComboBox<MACHINE_STATE> state_box = new JComboBox(Arrays.stream(MACHINE_STATE.values()).filter(
                    state -> state != MACHINE_STATE.IN_USE
            ).toArray());
            state_box.setEditable(false);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            main_content.add(state_box, gbc);


            ActionListener _main = _ -> {
                File oldsetting = new File(Main.SETTINGS_FILE);
                oldsetting.delete();
                System.out.println("Deleting old settings...");
                Settings.getInstance().core.mainDirectory           = tfmainDir.getText();
                Settings.getInstance().core.scoresFileName          = tfScoreFileName.getText();
                Settings.getInstance().core.gameSubDirectory        = tfgameDir.getText();
                Settings.getInstance().core.machineSubDirectory     = tfmachineDir.getText();
                Settings.getInstance().core.playerSubDirectory      = tfplayerDir.getText();
                Settings.getInstance().core.maxPlayerAge            = (int) maxAgeSpinner.getValue();
                Settings.getInstance().core.minimumPlayerAge        = (int) minAgeSpinner.getValue();
                Settings.getInstance().core.defaultMachineTickets   = (int) defaultTicketsSpinner.getValue();
                Settings.getInstance().core.defaultMachineState     = (MACHINE_STATE) state_box.getSelectedItem();
                System.out.println(STR."Settings Updated: Main Dir:\{tfmainDir.getText()}, PDir:\{tfplayerDir.getText()}, GDir:\{tfgameDir.getText()}, GMDir:\{tfmachineDir.getText()}\nMaxAge:\{maxAgeSpinner.getValue()}, MinAge:\{minAgeSpinner.getValue()}\nDefaultTickets:\{defaultTicketsSpinner.getValue()}, DefaultState:\{state_box.getSelectedItem()}");
                Database.getInstance().saveSettings(Main.SETTINGS_FILE);
                Database.getInstance().loadSettings(Main.SETTINGS_FILE);
                exit_mode[0] = 1;
            };
            ActionListener _scnd = _ -> {
                exit_mode[0] = 2;
            };

            // Submit and Cancel buttons
            JButton submitButton = new JButton("Save new Settings");
            gbc.gridx = 1;
            gbc.gridy = 10;
            submitButton.addActionListener(_main);
            main_content.add(submitButton, gbc);

            accept_btn.addActionListener(_main);

            JButton exitButton = new JButton("Cancel");
            gbc.gridx = 0;
            gbc.gridy = 10;
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
            //apply settings here
            return;
        }
        else return; // return null if user choose to cancel game input
    }
}

class SettingsCore implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control

	public     String    mainDirectory;
	public     String    scoresFileName;
	public     String    gameSubDirectory;
	public     String    playerSubDirectory;
	public     String    machineSubDirectory;
	public      int      maxPlayerAge;
	public      int      next_game_id;
	public      int      next_player_id;
	public      int      next_machine_id;
	public      int      minimumPlayerAge;
	public      int      defaultMachineTickets;
	public MACHINE_STATE defaultMachineState;

	public SettingsCore() {
		// set defaults
		// FIXME: when making the .jar, remove the practical_work/ from the paths
		// NOTE: After every change in here (adding new attributes or just changing order/type, delete old .bin for new module to be applied.
		mainDirectory = "./practical_work/db";
		gameSubDirectory = "games";
		playerSubDirectory = "players";
		machineSubDirectory = "machines";
		next_machine_id = 1; // Artificial SERIAL for machine ids
		next_player_id = 1;  // Artificial SERIAL for player  ids
		next_game_id = 1;    // Artificial SERIAL for game    ids
		minimumPlayerAge = 8;
		maxPlayerAge = 140;
		scoresFileName = "scores.nsv";
		defaultMachineTickets = 200;
		defaultMachineState = MACHINE_STATE.COMING_SOON;
	}
}