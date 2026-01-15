import java.io.*;
import java.util.*;

@SuppressWarnings("preview")
public class Database {
	// will work with saving/loading. Every file operation will pass here.
	// for now working with serialized binaries.

	private static final Database __instance = new Database();
	private Database() {}	// Constructor logic
	public static Database getInstance() { return __instance; }

    private void dirBuild(File testDir, String dirName){
        File currDir = new File("");
        if (!testDir.exists()){
            if (!testDir.mkdirs()){
                if (currDir.canWrite())
                    System.out.println(STR."\{currDir.getAbsolutePath()} exists and is Writable, but failed to create folders anyway");
                else
                    System.out.println(STR."Failed to create \{dirName}/ folder: missing write permissions");
            }
        }
    }
    public void rebuildDirs(){
        File mainDir = new File(STR."\{Settings.getInstance().core.mainDirectory}");
        File gameDir = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.gameSubDirectory}");
        File playerDir = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.playerSubDirectory}");
        File machineDir = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}");
        dirBuild(mainDir, "main");
        dirBuild(gameDir, "game");
        dirBuild(playerDir, "player");
        dirBuild(machineDir, "machine");
    }

	// Methods
	public void saveGame(Game game_to_save, @SuppressWarnings("unused") String file_name){
		// Game is serialized
		// FIXME: Ensure the file is writable, and the directory exists.
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
	public ArrayList<Integer> listGames(boolean show_debug) {
		ArrayList<Integer> to_return = new ArrayList<>();

		// Create the directory path
		File directory = new File(Settings.getInstance().core.mainDirectory, Settings.getInstance().core.gameSubDirectory);
		// Filter and list files
		if (directory.exists() && directory.isDirectory()) {
			String pattern = show_debug ? "^-?\\d+$" : "^\\d+$";
			File[] files = directory.listFiles(file ->
					file.isFile() && file.getName().endsWith(".gm") && file.getName().substring(0, file.getName().lastIndexOf('.')).matches(pattern));
			// Print matched files
			if (files != null) {
				for (File file : files) {
					to_return.add(Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf('.'))));
				}
			}
		} else {
			System.out.println("[?] File does not exist or it is not a directory.");
			return null;
		}
		return to_return;
	}
	public ArrayList<Integer> listGames(){ return listGames(false); }

	public void savePlayer(Player game_to_save, String file_name){
		// Game is serialized
		// FIXME: Ensure the file is writable, and the directory exists.
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
        ArrayList<Integer> to_return = new ArrayList<>();

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
                    to_return.add(Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf('.'))));
				}
			}
		} else {
			System.out.println("[?] File does not exist or it is not a directory.");
			return null;
		}
		return to_return;
	}
	public ArrayList<Integer> listPlayers(){ return listPlayers(false); }

	// Further Test these implementations. they were copy-pasted from Player
	// They passed DEBUG checks
	public void saveGameMachine(GameMachine game_to_save, String file_name){
		// Game is serialized
		// FIXME: Ensure the file is writable, and the directory exists.
		try (FileOutputStream fileOut = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{file_name}");
			 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
			// Serialize the Game object to the file
			objectOut.writeObject(game_to_save);
		} catch (IOException e) {
			System.err.println(STR."[*] Error saving machine: \{e.getMessage()}");
		}
	}
	public void saveGameMachine(GameMachine player_to_save){
		// set the default filename if none is provided
		String filename = STR."\{player_to_save.getId()}.mch";
		saveGameMachine(player_to_save, filename);
	}
	public GameMachine loadGameMachine(String filename) {
		File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{filename}");
		GameMachine loadedGame = null;

		try (FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
			// Deserialize the Game object from the file
			loadedGame = (GameMachine) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(STR."[*] Error loading machine: \{e.getMessage()}");
		}

		return loadedGame; // Return the loaded Game object
	}
	public GameMachine loadGameMachine(int id) {
		String filename = STR."\{id}.mch";
		GameMachine to_return = loadGameMachine(filename);
		if (to_return != null && to_return.getId() != id) {
			System.out.println(STR."[!] Tampering with machine detected! (Expected: \{id} | Returned: \{to_return.getId()})");
			return null;
		}
		return to_return; // Return the loaded Game object
	}
	public ArrayList<Integer> listGameMachine(boolean show_debug) {
		ArrayList<Integer> to_return = new ArrayList<>();

		// Create the directory path
		File directory = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}");

		// Filter and list files
		if (directory.exists() && directory.isDirectory()) {
			String pattern = show_debug ? "^-?\\d+$" : "^\\d+$";
			File[] files = directory.listFiles(file ->
					file.isFile() && file.getName().endsWith(".mch") && file.getName().substring(0, file.getName().lastIndexOf('.')).matches(pattern));
			// Print matched files
			if (files != null) {
				for (File file : files) {
					to_return.add(Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf('.'))));
				}
			}
		} else {
			System.out.println("[?] File does not exist or it is not a directory.");
			return null;
		}
		return to_return;
	}
	public ArrayList<Integer> listGameMachine(){ return listGameMachine(false); }

    public void saveMachine(GameMachine machine_to_save, @SuppressWarnings("unused") String file_name){
        // Game is serialized
        // TODO: Ensure the file is writable
        try (FileOutputStream fileOut = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{file_name}");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            // Serialize the Game object to the file
            objectOut.writeObject(machine_to_save);
        } catch (IOException e) {
            System.err.println(STR."Error saving machine: \{e.getMessage()}");
        }
    }
    public void saveMachine(GameMachine machine_to_save){
        // set the default filename if none is provided
        String gameName = machine_to_save.getName().replace(" ", "-"); // Replace spaces with dashes
        String filename = STR."\{machine_to_save.getId()}.mch";
        saveMachine(machine_to_save, filename);
    }
    public GameMachine loadMachine(int id) {
        String filename = STR."\{id}.mch";
        GameMachine to_return = loadMachine(filename);
        if (to_return != null && to_return.getId() != id) {
            System.out.println(STR."[!] Tampering with game detected! (Expected: \{id} | Returned: \{to_return.getId()})");
            return null;
        }
        return to_return; // Return the loaded Game object
    }
    public GameMachine loadMachine(String filename) {
        File file = new File(STR."\{Settings.getInstance().core.mainDirectory}/\{Settings.getInstance().core.machineSubDirectory}/\{filename}");
        GameMachine loadedMachine = null;

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            // Deserialize the Game object from the file
            loadedMachine = (GameMachine) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(STR."[*] Error loading game: \{e.getMessage()}");
        }

        return loadedMachine; // Return the loaded Game object
    }

	public boolean saveSettings(String filename){
		// FIXME: Ensure the file is writable, and the directory exists.
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
		rebuildDirs();
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
	public void saveLeaderboardsByGame(Map<Integer, ArrayList<Tuple<Integer, Integer>>> gameScores) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			for (Map.Entry<Integer, ArrayList<Tuple<Integer, Integer>>> entry : gameScores.entrySet()) {
				for (Tuple<Integer, Integer> score : entry.getValue()) {
					outputStream.write(STR."\{entry.getKey()}\0\{score.getKey()}\0\{score.getValue()}\n".getBytes("UTF-8"));
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
	public void saveLeaderboardsByPlayer(Map<Integer, ArrayList<Tuple<Integer, Integer>>> playerScores) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv")) {
			for (Map.Entry<Integer, ArrayList<Tuple<Integer, Integer>>> entry : playerScores.entrySet()) {
				for (Tuple<Integer, Integer> score : entry.getValue()) {
					outputStream.write(STR."\{score.getKey()}\0\{entry.getKey()}\0\{score.getValue()}\n".getBytes("UTF-8"));
				}
			}
		} catch (IOException e) {
			System.err.println(STR."[!] Error saving leaderboards: \{e.getMessage()}");
		}
	}

	public void appendLeaderboardRecord(int gameId, int playerId, int score) {
		try (FileOutputStream outputStream = new FileOutputStream(STR."\{Settings.getInstance().core.mainDirectory}/scores.nsv", true)) {
			outputStream.write(STR."\{gameId}\0\{playerId}\0\{score}\n".getBytes("UTF-8"));
		} catch (IOException e) {
			System.err.println(STR."[!] Error appending leaderboard record: \{e.getMessage()}");
		}
	}

	@FunctionalInterface
	public interface RecordMatcher {
		/**
		 @return true  -> REMOVE record
		         false -> KEEP recordFilesFiles
		*/
		//
		boolean match(int gameId, int playerId, int score);
	}

	// removeOnMatch((gameId, playerId, score) ->
	//    playerId == 42 && gameId == 7
	//);
	// TODO: test
	public int removeOnMatch(RecordMatcher matcher) {
		int removed = 0;
		Map<Integer, ArrayList<Tuple<Integer, Integer>>> old = loadLeaderboardsByGame();
		if (old == null) return -1;

		// Iterate over games
		Iterator<Map.Entry<Integer, ArrayList<Tuple<Integer, Integer>>>> gameIt = old.entrySet().iterator();
		while (gameIt.hasNext()) {
			Map.Entry<Integer, ArrayList<Tuple<Integer, Integer>>> gameEntry = gameIt.next();
			int gameId = gameEntry.getKey();
			ArrayList<Tuple<Integer, Integer>> scores = gameEntry.getValue();

			// Remove matching records for this game
			if (scores.removeIf(scoreTuple -> matcher.match(gameId, scoreTuple.getKey(), scoreTuple.getValue()))){
				removed++;
			}

			// Remove game entry if no scores remain
			if (scores.isEmpty()) gameIt.remove();
		}

		saveLeaderboardsByGame(old);
		return removed;
	}
}
