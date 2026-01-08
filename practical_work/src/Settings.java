import java.io.Serial;
import java.io.Serializable; // to save in binary

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
}

class SettingsCore implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control

	public String mainDirectory;
	public String gameSubDirectory;
	public String playerSubDirectory;
	public String machineSubDirectory;
	public int next_player_id;
	public int next_game_id;
	public int next_machine_id;
	public int minimumPlayerAge;
	public int maxPlayerAge;
	public String scoresFileName;

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
	}
}