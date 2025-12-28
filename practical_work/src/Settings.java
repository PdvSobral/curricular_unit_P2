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

	public SettingsCore() {
		// set defaults
		// FIXME: when making the .jar, remove the practical_work/ from the paths
		mainDirectory = "./practical_work/db";
		gameSubDirectory = "games";
	}
}