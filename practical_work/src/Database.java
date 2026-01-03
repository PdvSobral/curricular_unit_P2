import java.io.*;
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
		return loadGame(filename); // Return the loaded Game object
	}
	public Game loadGame(String filename) {
		File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.gameSubDirectory}/\{filename}");
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

	public void savePlayer(Player game_to_save, @SuppressWarnings("unused") String file_name){
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
	public Player loadPlayer(@SuppressWarnings("unused") int id) {
		String filename = STR."\{id}.plr";
		return loadPlayer(filename); // Return the loaded Game object
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
	public Map loadLeaderboardsByGame() throws IOException{
		// Returns MAP gameId -> List [ (playerId, score) ]
		InputStream inputStream = new FileInputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv");
		byte[] data = inputStream.readAllBytes();
		String nsvContent = new String(data);

		// Tuple is a custom class, already implemented
		Map<Integer, List<Tuple<Integer, Integer>>> gameScores = new HashMap<>();
		for (String line : nsvContent.split("\n")) {
			String[] values = line.split("\0");
			gameScores.computeIfAbsent(Integer.parseInt(values[0]), k -> new ArrayList<>()).add(new Tuple<>(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
		}
		return gameScores;
	}

	// FIXME: only prototype
	public Map loadLeaderboardsByPlayer() throws IOException{
		InputStream inputStream = new FileInputStream("scores.nsv");
		String nsvContent = new String(inputStream.readAllBytes());
		inputStream.close();

		Map<Integer, List<Tuple<Integer, Integer>>> playerScores = new HashMap<>();
		for (String line : nsvContent.split("\n")) {
			String[] values = line.split("\0");
			Integer gameId = Integer.parseInt(values[0]);
			Integer playerId = Integer.parseInt(values[1]);
			int score = Integer.parseInt(values[2]);
			playerScores.computeIfAbsent(playerId, k -> new ArrayList<>()).add(new Tuple<>(gameId, score));
		}

		return playerScores;
	}
}
