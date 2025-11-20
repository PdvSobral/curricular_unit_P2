import java.io.*;

@SuppressWarnings("preview")
public class Database {
	// will work with saving/loading. Every file operation will pass here.
	// for now working with serialized binaries.
	// maybe change to JSON?? or custom binary? or custom text with offsets

	// Database.getInstance().getYear()
	private static final Database __instance = new Database();
	private static String main_save_directory = ".";
	@SuppressWarnings("unused")
	private static String games_save_subdirectory = ".";

	private Database() {}	// Constructor logic

	public static Database getInstance() {
		return __instance;
	}

	// Set the main save directory
	public void setMainSaveDirectory(String directoryPath) {
		File dir = new File(directoryPath);
		if (dir.exists() && dir.isDirectory()) {
			main_save_directory = directoryPath;
		} else {
			System.err.println("Invalid directory path specified.");
		}
	}
	public void setGamesSubdirectory(String directoryPath) {
		// TODO: if not exist create. same on MainDirectory.
		// TODO: if possible use a syscall to make the GUI selector
		games_save_subdirectory = directoryPath;
	}


	// Methods
	public void saveGame(Game game_to_save, @SuppressWarnings("unused") String file_name){
		// Game is serialized
		// TODO: Ensure the file is writable
		try (FileOutputStream fileOut = new FileOutputStream(STR."\{main_save_directory}/\{games_save_subdirectory}/\{file_name}");
			 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
			// Serialize the Game object to the file
			objectOut.writeObject(game_to_save);
		} catch (IOException e) {
			System.err.println(STR."Error saving game: \{e.getMessage()}");
		}
	}
	public void saveGame(Game game_to_save){
		// set the default filename if none is provided
		String gameName = game_to_save.getName().replace(" ", "-"); // Replace spaces with dashes
		String filename = STR."\{game_to_save.getYear()}_\{gameName}.gm";
		saveGame(game_to_save, filename);
	}
	public Game loadGame(@SuppressWarnings("unused") String filename) {
		if (main_save_directory == null) {
			System.err.println("Main save directory is not set.");
			return null;
		}

		File file = new File(STR."\{main_save_directory}/\{games_save_subdirectory}/\{filename}");
		Game loadedGame = null;

		try (FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			// Deserialize the Game object from the file
			loadedGame = (Game) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(STR."Error loading game: \{e.getMessage()}");
		}

		return loadedGame; // Return the loaded Game object
	}
}
