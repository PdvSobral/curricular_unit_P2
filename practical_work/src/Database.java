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
		// TODO: if possible use a syscall to make the GUI selector
		File dir = new File(directoryPath);
		try {
			if (!dir.isDirectory()) {
				System.out.println(STR."[!] File \"\{directoryPath}\" already exists. Unable to create directory!");
				return;
			}
			// If it does not exist, attempt to create the directory (and parent dirs also as needed)
			if (!dir.exists()) {
				if (dir.mkdirs()) System.out.println(STR."[*] Directory creation of \"\{directoryPath}\" sucsesful!");
				else System.err.println(STR."[!] Directory creation of \"\{directoryPath}\" unsucsesful!");
			}
			else System.out.println(STR."[*] Directory \"\{directoryPath}\" already exists!");
			main_save_directory = directoryPath;
		}
		catch (SecurityException e) {
			System.err.println(STR."Permission denied: \{e.getMessage()}");
		} catch (Exception e) {
			System.err.println(STR."An error occurred: \{e.getMessage()}");
		}
	}

	public void setGamesSubdirectory(String directoryPath) {
		// TODO: if possible use a syscall to make the GUI selector
		// TODO: if not GUI, then will have to change this to be very aparent the maindir in prints
		// However, for now, let's keep this, and attemp to use GUI to ask.
		File dir = new File(STR."\{main_save_directory}\{directoryPath}");
		try {
			if (!dir.isDirectory()) {
				System.out.println(STR."[!] File \"\{directoryPath}\" already exists. Unable to create directory!");
				return;
			}
			// If it does not exist, attempt to create the directory (and parent dirs also as needed)
			if (!dir.exists()) {
				if (dir.mkdirs()) System.out.println(STR."[*] Directory creation of \"\{directoryPath}\" sucsesful!");
				else System.err.println(STR."[!] Directory creation of \"\{directoryPath}\" unsucsesful!");
			}
			else System.out.println(STR."[*] Directory \"\{directoryPath}\" already exists!");
			games_save_subdirectory = directoryPath;
		}
		catch (SecurityException e) {
			System.err.println(STR."Permission denied: \{e.getMessage()}");
		} catch (Exception e) {
			System.err.println(STR."An error occurred: \{e.getMessage()}");
		}
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
