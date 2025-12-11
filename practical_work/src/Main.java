import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnnecessaryModifier", "SwitchStatementWithTooFewBranches"})
public class Main {
	static final int DEBUG = -2;
	static final int NORMAL = 0;
	static final int RUNNING_MODE = DEBUG;
	static final boolean CLEAR_SCREEN = false;

	static void clearScreen() {
		if (CLEAR_SCREEN) System.out.print("\033[2J\033[H");
		return;
	}
	static void receivedCTRLD() {
		System.out.print("\n[!!] CTRL+D (EOF) received!. Translating to SIGINT...");
		System.exit(0);
	}

	static void pass(){ System.out.println("NOTHING HERE (yet...)"); }

	static final List<String> MAINMENU		 = List.of("Management", "Checks", "Exit");
	static final List<String> MANGEGMENU	 = List.of("Machine Management", "Game Management", "Player Management", "Leaderboard Management", "Return");
	static final List<String> MACHINEMANGEG	 = List.of("Add Machine", "Remove Machine", "Return");
	static final List<String> GAMEMANGEG	 = List.of("Add Game", "Remove Game", "Return");
	static final List<String> PLAYERMANGEG	 = List.of("Add Player", "Edit Player", "Remove Player", "Reset Player Highscores", "Return");
	static final List<String> LEADERMANEG	 = List.of("Reset Game Leaderboards", "Purge Player from Leaderboards", "Leaderboards Reset", "Return");
	static final List<String> SUBLEADERMANEG = List.of("Player Records Reset", "Game Records Reset", "FULL RESET", "Cancel");

	static final List<String> CHECKSMENU = List.of("Machine/Game Checks", "Player Checks", "Leaderboard Checks", "Return");

	static final int WINDOW_WIDTH = 700;
	static final int WINDOW_HIGHT = 1000;
	static final int BORDER_WIDTH = 40;
	static final int BORDER_LOSS = 10;
	static final String APPLICATION_TITTLE = "ARCADE^2 MANAGER";
	static final int BUTTON_SIZE = 40;
	static final int BOTTOM_PANEL_SIZE = 240;

	public static void main(String[] args) throws IOException{
		InterfaceWrapper.getInstance(); // Start if not yet started
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\n[!!] SIGINT received! Shutting workers and saving...");
			// TODO: Add a save here for what is needed
			System.out.println("[!!] Exiting...");
		}));
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(MAINMENU, "MAIN MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) break;
			switch (__temp){
				case DEBUG:
					// pass();
					System.out.println("Clearing contents and waiting for input...");
					InterfaceWrapper.getInstance().getContentSpace().clearPanel();
					System.out.println("Press enter to continue...");
					new Reader(System.in).readLine();
					break;
				case 1:
					managementMenu();
					break;
				case 2:
					checksMenu();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
		System.exit(0);
	}

	private static void managementMenu() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(MANGEGMENU, "MANAGEMENT MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					pass();
					break;
				case 1:
					machineManeg();
					break;
				case 2:
					gameManeg();
					break;
				case 3:
					playerManeg();
					break;
				case 4:
					leadersManeg();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void checksMenu() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(CHECKSMENU, "CHECKS MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					pass();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void gameManeg() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(GAMEMANGEG, "GAME MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					Database.getInstance().setMainSaveDirectory("./practical_work/db");
					Database.getInstance().setGamesSubdirectory("games");
					Game teste = new Game(2001, "Space Odessey", new ArrayList<>(), "space", "no idea", "");
					System.out.println(teste);
					teste.save();
					System.out.println("Loading...");
					Game test = Database.getInstance().loadGame("2001_Space-Odessey.gm");
					System.out.println(test);
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void playerManeg() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(PLAYERMANGEG, "PLAYER MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					pass();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void machineManeg() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(MACHINEMANGEG, "MACHINE MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					pass();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void leadersManeg() throws IOException{
		int __temp;
		while (true){
			clearScreen();
			__temp = Menu.getInstance().menu(LEADERMANEG, "LEADERBOARDS MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp) {
				case DEBUG:
					pass();
					break;
				case 3:
					clearScreen();
					__temp = Menu.getInstance().menu(SUBLEADERMANEG, "LEADERBOARDS RESET", (char) 1, RUNNING_MODE);
					switch (__temp) {
						case 0: break;
						default: System.out.println("Option not yet implemented!");
					}
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}
}
