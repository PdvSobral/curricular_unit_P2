import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnnecessaryModifier")
public class Main {
	static void clearScreen() {
		System.out.print("\033[2J\033[H");
		return;
	}

	static void receivedCTRLD() {
		System.out.print("\n[!!] CTRL+D (EOF) received!. Translating to SIGINT...");
		System.exit(0);
	}

	static void pass(){ System.out.println("NOTHING HERE (yet...)"); }

	static final int DEBUG = -2;
	static final int NORMAL = 0;
	static final int RUNNING_MODE = DEBUG;

	static final List<String> MAINMENU = List.of("Management", "Checks", "Exit");
	static final List<String> MANGEGMENU = List.of("Machine Management", "Game Management", "Player Management", "Leaderboard Management", "Return");
	static final List<String> MACHINEMANGEG = List.of("Add Machine", "Remove Machine", "Return");
	static final List<String> GAMEMANGEG = List.of("Add Game", "Remove Game", "Return");
	static final List<String> PLAYERMANGEG = List.of("Add Player", "Edit Player", "Remove Player", "Reset Player Highscores", "Return");

	static final List<String> CHECKSMENU = List.of("Machine/Game Checks", "Player Checks", "Leaderboard Checks", "Return");

	public static void main(String[] args) throws IOException{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\n[!!] SIGINT received!. Shutting workers and saving...");
			// TODO: Add a save here for what is needed
			System.out.println("[!!] Exiting...");
		}));
		int __temp;
		while (true){
			__temp = Menu.getInstance().menu(MAINMENU, "MAIN MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) break;
			if (__temp == DEBUG){
				pass();
			} else if (__temp == 1){
				managementMenu();
			} else if (__temp == 2){
				checksMenu();
			} else {
				System.out.println("Option not yet implemented!");
			}
		}
		System.exit(0);
	}

	private static void managementMenu() throws IOException{
		int __temp;
		while (true){
			__temp = Menu.getInstance().menu(MANGEGMENU, "MANAGEMENT MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			if (__temp == DEBUG){
				pass();
			} else if (__temp == 2) {
				gameManeg();
			} else {
				System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void checksMenu() throws IOException{
		int __temp;
		while (true){
			__temp = Menu.getInstance().menu(CHECKSMENU, "CHECKS MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			if (__temp == DEBUG){
				System.out.println("NOTHING TO DEBUG (yet...)");
			} else {
				System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void gameManeg() throws IOException{
		int __temp;
		while (true){
			__temp = Menu.getInstance().menu(GAMEMANGEG, "GAME MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			if (__temp == DEBUG){
				Database.getInstance().setMainSaveDirectory("./practical_work/db");
				Database.getInstance().setGamesSubdirectory("games");
				Game teste = new Game(2001, "Space Odessey", new ArrayList<>(), "space", "no idea", "");
				System.out.println(teste);
				teste.save();
				System.out.println("Loading...");
				Game test = Database.getInstance().loadGame("2001_Space-Odessey.gm");
				System.out.println(test);
			} else {
				System.out.println("Option not yet implemented!");
			}
		}
	}
}
