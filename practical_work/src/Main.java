import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	static void receivedCTRLD() {
		System.out.print("\n[!!] CTRL+D (EOF) received!. Translating to SIGINT...");
		System.exit(0);
	}

	static final int DEBUG = -2;

	static List<String> MAINMENU = List.of("Option 1", "Option 2", "Option 3", "Exit");

	public static void main(String[] args) throws IOException{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\n[!!] SIGINT received!. Shutting workers and saving...");
			// TODO: Add a save here for what is needed
			System.out.println("[!!] Exiting...");
		}));
		int temp;
		while (true){
			temp = Menu.getInstance().menu(MAINMENU, "MAIN MENU", (char) 1, DEBUG);
			if (temp == 0) break;
			if (temp == DEBUG){
				Database.getInstance().setMainSaveDirectory("./practical_work/db");
				Database.getInstance().setGamesSubdirectory("games");
				Game teste = new Game(2001, "Space Odessey", new ArrayList<>(), "space", "no idea", "");
				System.out.println(teste);
				teste.save();
				System.out.println("Loading...");
				Game test = Database.getInstance().loadGame("2001_Space-Odessey.gm");
				System.out.println(test);
			} else if (temp == 2){
				System.out.println("2");
			} else if (temp == 3){
				System.out.println("3");
			} else {
				System.out.print("Option not yet implemented!");
			}
		}
		System.out.println("Exiting...");
	}
}
