import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("preview")
public class Database {
	// will work with saving/loading. Every file operation will pass here.
	// for now working with serialized binaries.
	// maybe change to JSON?? or custom binary? or custom text with offsets

	// Database.getInstance().getYear()
	private static final Database __instance = new Database();
	private Database() {}	// Constructor logic
	public static Database getInstance() {
		return __instance;
	}

	// Methods
	public void saveGame(Game game_to_save, @SuppressWarnings("unused") String file_name){
		// Game is serialized
		// TODO: Ensure the file is writable
		try (FileOutputStream fileOut = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.gameSubDirectory}/\{file_name}");
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
		String filename = STR."\{game_to_save.getGameId()}.gm";
		saveGame(game_to_save, filename);
	}
	public Game loadGame(int id) {
		String filename = STR."\{id}.gm";
		Game to_return = loadGame(filename);
		if (to_return != null && to_return.getGameId() != id) {
			System.out.println(STR."[!] Tampering with game detected! (Expected: \{id} | Returned: \{to_return.getGameId()})");
			return null;
		}
		return to_return; // Return the loaded Game object
	}
	public Game loadGame(String filename) {
		File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.gameSubDirectory}/\{filename}");
		Game loadedGame = null;

		try (FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			// Deserialize the Game object from the file
			loadedGame = (Game) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(STR."[*] Error loading game: \{e.getMessage()}");
		}

		return loadedGame; // Return the loaded Game object
	}

	public void savePlayer(Player game_to_save, String file_name){
		// Game is serialized
		// TODO: Ensure the file is writable and directories exist
		try (FileOutputStream fileOut = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.playerSubDirectory}/\{file_name}");
			 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
			// Serialize the Game object to the file
			objectOut.writeObject(game_to_save);
		} catch (IOException e) {
			System.err.println(STR."Error saving game: \{e.getMessage()}");
		}
	}
	public void savePlayer(Player player_to_save){
		// set the default filename if none is provided
		String filename = STR."\{player_to_save.getId()}.plr";
		savePlayer(player_to_save, filename);
	}
	public Player loadPlayer(String filename) {
		File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.playerSubDirectory}/\{filename}");
		Player loadedGame = null;

		try (FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			// Deserialize the Game object from the file
			loadedGame = (Player) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(STR."Error loading player: \{e.getMessage()}");
		}

		return loadedGame; // Return the loaded Game object
	}
	public Player loadPlayer(int id) {
		String filename = STR."\{id}.plr";
		Player to_return = loadPlayer(filename);
		if (to_return != null && to_return.getId() != id) {
			System.out.println(STR."[!] Tampering with player detected! (Expected: \{id} | Returned: \{to_return.getId()})");
			return null;
		}
		return to_return; // Return the loaded Game object
	}
	public ArrayList<Integer> listPlayers(boolean show_debug) {

		// Create the directory path
		File directory = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.playerSubDirectory}");

		// Filter and list files
		if (directory.exists() && directory.isDirectory()) {
			String pattern = show_debug ? "^-?\\d+$" : "^\\d+$";
			File[] files = directory.listFiles(file ->
					file.isFile() && file.getName().endsWith(".plr") && file.getName().substring(0, file.getName().lastIndexOf('.')).matches(pattern));
			// Print matched files
			if (files != null) {
				for (File file : files) {
					System.out.println(file.getName().substring(0, file.getName().lastIndexOf('.')));
				}
			}
		} else {
			System.out.println("[?] File does not exist or it is not a directory.");
			return null;
		}
		return null;
	}
	public ArrayList<Integer> listPlayers(){
		return listPlayers(false);
	}


	public boolean saveSettings(String filename){
		// TODO: Ensure the file is writeable
		// Settings is serialized
		try (FileOutputStream fileOut = new FileOutputStream(filename);
			 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
			// Serialize the Settings object to the file
			objectOut.writeObject(Settings.getInstance().core);
		} catch (IOException e) {
			System.err.println(STR."Error saving Settings: \{e.getMessage()}!");
			return false;
		}
		return true;
	}
	public boolean loadSettings(String filename) {
		File file = new File(filename);
		try (FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			// Deserialize the Game object from the file
			Settings.getInstance().core = (SettingsCore) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(STR."Error loading settings: \{e.getMessage()}!");
			return false;
		}
		return true;
	}

	// NSV (Null Seperated Values) for possibility of being loaded by another app
	// For example to trim the records for 1 per game per user except if in top 10 per game.
	// Returns MAP gameId -> List [ (playerId, score) ]
	public Map<Integer, ArrayList<Tuple<Integer, Integer>>> loadLeaderboardsByGame() {
		// Try resource assures Stream is closed even if on error
		try (InputStream inputStream = new FileInputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			Map<Integer, ArrayList<Tuple<Integer, Integer>>> gameScores = new HashMap<>();
			for (String line : (new String(inputStream.readAllBytes())).split("\n")) {
				if (!line.isEmpty()) { // Check for empty lines
					String[] values = line.split("\0");
					if (values.length == 3) { // Ensure correct number of values
						gameScores.computeIfAbsent(Integer.parseInt(values[0]), k -> new ArrayList<>()).add(new Tuple<>(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
					}
				}
			}
			return gameScores;
		} catch (IOException e) {
			System.err.println(STR."[!] Error loading leaderboards: \{e.getMessage()}");
			return null;
		}
	}
	public void saveLeaderboardsByGame(Map<Integer, List<Tuple<Integer, Integer>>> gameScores) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			for (Map.Entry<Integer, List<Tuple<Integer, Integer>>> entry : gameScores.entrySet()) {
				for (Tuple<Integer, Integer> score : entry.getValue()) {
					outputStream.write(STR."\{entry.getKey()}\0\{score.getKey()}\0\{score.getValue()}\n".getBytes(StandardCharsets.UTF_8));
				}
			}
		} catch (IOException e) {
			System.err.println(STR."[!] Error saving leaderboards: \{e.getMessage()}");
		}
	}

	// CopyPasta com algumas alterações. Prone para erors...
	// Returns MAP playerId -> List [ (gameId, score) ]
	public Map<Integer, ArrayList<Tuple<Integer, Integer>>> loadLeaderboardsByPlayer() {
		try (InputStream inputStream = new FileInputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			Map<Integer, ArrayList<Tuple<Integer, Integer>>> playerScores = new HashMap<>();
			for (String line : new String(inputStream.readAllBytes()).split("\n")) {
				if (!line.isEmpty()) {
					String[] values = line.split("\0");
					if (values.length == 3) {
						playerScores.computeIfAbsent(Integer.parseInt(values[1]), k -> new ArrayList<>()).add(new Tuple<>(Integer.parseInt(values[0]), Integer.parseInt(values[2])));
					}
				}
			}
			return playerScores;
		} catch (IOException e) {
			System.err.println(STR."[*] Error loading leaderboards: \{e.getMessage()}");
			return null;
		}
	}
	public void saveLeaderboardsByPlayer(Map<Integer, List<Tuple<Integer, Integer>>> playerScores) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			for (Map.Entry<Integer, List<Tuple<Integer, Integer>>> entry : playerScores.entrySet()) {
				for (Tuple<Integer, Integer> score : entry.getValue()) {
					outputStream.write(STR."\{score.getKey()}\0\{entry.getKey()}\0\{score.getValue()}\n".getBytes(StandardCharsets.UTF_8));
				}
			}
		} catch (IOException e) {
			System.err.println(STR."[!] Error saving leaderboards: \{e.getMessage()}");
		}
	}

	public void appendLeaderboardRecord(int gameId, int playerId, int score) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv", true)) {
			outputStream.write(STR."\{gameId}\0\{playerId}\0\{score}\n".getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.err.println(STR."[!] Error appending leaderboard record: \{e.getMessage()}");
		}
	}
}
