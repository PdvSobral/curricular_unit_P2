import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SuppressWarnings({"UnnecessaryModifier", "SwitchStatementWithTooFewBranches"})
public class Main {
	static final int     DEBUG        = -2;
	static final int     NORMAL       = 0;
	static final int     RUNNING_MODE = DEBUG;

	static void pass(){
		System.out.println("NOTHING HERE (yet...)");
		InterfaceWrapper.showErrorWindow("NOTHING HERE (yet...)", "Mystery");
	}

	static final List<String> MAINMENU		 = List.of("Management", "Checks", "Settings", "Exit");
	static final List<String> MANGEGMENU	 = List.of("Machine Management", "Game Management", "Player Management", "Leaderboard Management", "Return");
	static final List<String> MACHINEMANGEG	 = List.of("Add Machine", "Remove Machine", "Return");
	static final List<String> GAMEMANGEG	 = List.of("Add Game", "Remove Game", "Return");
	static final List<String> PLAYERMANGEG	 = List.of("Add Player", "Edit Player", "Remove Player", "Return");
	static final List<String> LEADERMANEG	 = List.of("Add New Record", "Purge Game from Leaderboards", "Purge Player from Leaderboards", "Leaderboards Reset", "Return");
	static final List<String> SUBLEADERMANEG = List.of("Proceed", "Cancel");
	static final List<String> CHECKSMENU     = List.of("Machine/Game Checks", "Player Checks", "Leaderboard Checks", "Return");

	static final int    WINDOW_WIDTH       = 700;
	static final int    WINDOW_HIGHT       = 1000;
	static final int    BORDER_WIDTH       = 40;
	static final int    BORDER_LOSS        = 10;
	static final String APPLICATION_TITTLE = "ARCADE^2 MANAGER";
	static final int    BUTTON_SIZE        = 40;
	static final int    BOTTOM_PANEL_SIZE  = 240;

	// FIXME: when making the .jar, remove the practical_work/ from the settings file
	static final String SETTINGS_FILE = "./practical_work/settings.bin";

	public static void main(String[] args) throws IOException{
		if (!Database.getInstance().loadSettings(SETTINGS_FILE)){
			Settings.getInstance().reset();
			Database.getInstance().saveSettings(SETTINGS_FILE);
		}
		InterfaceWrapper.getInstance(); // Start if not yet started
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\n[!!] SIGINT received! Shutting workers and saving...");
			// Add a save here for what is needed, like running settings or others.
			//
			System.out.println("[!!] Exiting...");
		}));
		int __temp;
		while (true){
			
			__temp = Menu.getInstance().menu(MAINMENU, "MAIN MENU", (char) 1, RUNNING_MODE);
			if (__temp == 0) break;
			switch (__temp){
				case DEBUG:
					// pass();
					System.out.println("Clearing contents and waiting for input...");
					InterfaceWrapper.getInstance().getContentSpace().clearPanel();
					System.out.println(InterfaceWrapper.getInstance().getControlSpace().getButton("Up"));
					break;
				case 1:
					managementMenu();
					break;
				case 2:
					checksMenu();
					break;
				case 3:
					settingsMenu();
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
		System.exit(0);
	}

	private static void managementMenu() throws IOException{
		int __temp;
		while (true){
			
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
			
			__temp = Menu.getInstance().menu(GAMEMANGEG, "GAME MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					System.out.println("Saving game with id -2 (Debug)");
					Game teste = new Game(1980, "Pac-Man", 1, "Maze", "Namco", "A classic arcade game where you control Pac-Man as he navigates a maze and eats pellets while avoiding ghosts.", -2);
					System.out.println(teste);
					teste.save();
					System.out.println("Loading...");
					Game test = Database.getInstance().loadGame(-2);
					System.out.println(test);
					System.out.println("Deb off");
					Database.getInstance().listGames();
					System.out.println("Deb on");
					Database.getInstance().listGames(true);
					break;
				case 1:
					Game new_game = Game.createGameGUI();
					if (new_game != null) {
						// maybe add an overwrite detection and user confirmation.
						new_game.save();
						System.out.println("[*] New game added and saved successfully!");
					} else System.out.println("[*] User canceled operation.");
					break;
                case 2:
                     Game.deleteGameGUI();
                     break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void playerManeg() throws IOException{
		int __temp;
		while (true){
			
			__temp = Menu.getInstance().menu(PLAYERMANGEG, "PLAYER MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					System.out.println("Saving player with id -2 (Debug)");
					Player teste = new Player("John Doe", 25, -2);
					System.out.println(teste);
					teste.save();
					System.out.println("Loading...");
					Player test = Database.getInstance().loadPlayer(-2);
					System.out.println(test);
					System.out.println("Deb off");
					System.out.println(Database.getInstance().listPlayers());
					System.out.println("Deb on");
					System.out.println(Database.getInstance().listPlayers(true));
					break;
				case 1:
					Player new_player = Player.createPlayerGUI();
					if (new_player != null) {
						// add an overwrite detection and user confirmation.
						new_player.save();
						System.out.println("[*] New player added and saved successfully!");
					} else System.out.println("[*] User canceled operation.");
					break;
                case 2:
                    Player edit_player = Player.editPlayerGUI();
                    if (edit_player != null) {
                        edit_player.save();
                        System.out.println("[*] New player data saved successfully!");
                    } else System.out.println("[*] User canceled operation.");
                case 3:
                    Player.deletePlayerGUI();
                    break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void machineManeg() throws IOException{
		int __temp;
		while (true){
			
			__temp = Menu.getInstance().menu(MACHINEMANGEG, "MACHINE MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			switch (__temp){
				case DEBUG:
					// pass();
					System.out.println("Saving machine with id -2 (Debug)");
					GameMachine teste = new GameMachine("Street of Craziness", Controls.EIGHT_BUTTON, -2, -2);
					System.out.println(teste);
					teste.save();
					System.out.println("Loading...");
					GameMachine test = Database.getInstance().loadGameMachine(-2);
					System.out.println(test);
					System.out.println("Deb off");
					System.out.println(Database.getInstance().listGameMachine());
					System.out.println("Deb on");
					System.out.println(Database.getInstance().listGameMachine(true));
					break;
                case 1:
					// FIXME: When GUI finished, uncomment the code
					GameMachine new_machine = GameMachine.createMachineGUI();
					if (new_machine != null) {
						// add an overwrite detection and user confirmation.
						new_machine.save();
						System.out.println("[*] New machine added and saved successfully!");
					} else System.out.println("[*] User canceled operation.");
                    break;
                case 2:
					// FIXME
                    GameMachine.deleteMachineGUI();
                    break;

				default: System.out.println("Option not yet implemented!");
			}
		}
	}

	private static void leadersManeg() throws IOException{
		int __temp;
		while (true){
			__temp = Menu.getInstance().menu(LEADERMANEG, "LEADERBOARDS MANAGEMENT", (char) 1, RUNNING_MODE);
			if (__temp == 0) return;
			//TODO: PdvSobral - make Player purge and other
			switch (__temp) {
				case DEBUG:
					pass();
					break;
				case 4:
					InterfaceWrapper.showErrorWindow("You are about to delete all records from the system.\nThis is your last chance to turn back.");
					__temp = Menu.getInstance().menu(SUBLEADERMANEG, "LEADERBOARDS RESET", (char) 1, NORMAL);
					switch (__temp) {
						case 0: break;
						case 1:
							try {
								File file = new File(Settings.getInstance().core.mainDirectory, Settings.getInstance().core.scoresFileName);
								try (FileOutputStream _ = new FileOutputStream(file)) {}
								System.out.println("[*] File cleared successfully!");
							} catch (IOException e) {
								System.err.println(STR."[!] Error clearing file: \{e.getMessage()}");
							}
							break;
						default: System.out.println("Option not yet implemented!");
					}
					break;
				default: System.out.println("Option not yet implemented!");
			}
		}
	}
	private static void settingsMenu() throws IOException{
		pass();
		// TODO: make GUI
	}
}
