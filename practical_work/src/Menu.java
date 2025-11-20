import java.io.IOException;
import java.util.List;

@SuppressWarnings("preview")
public class Menu {
	private static final Menu __instance = new Menu();

	// Private constructor to ensure singleton
	private Menu() {}

	// Get the singleton instance of Menu
	public static Menu getInstance() { return __instance; }

	// Returns the selected option index (1-based) or -1 if canceled or invalid input
	public int menu(List<String> optionsList, String menu_name, char last_zero) throws IOException{
		return this.menu(optionsList, menu_name, last_zero, 0);
	}

	public int menu(List<String> optionsList, String menu_name, char last_zero, int debug) throws IOException{
		System.out.println(STR."\{menu_name}:");
		if (debug == Main.DEBUG){
			System.out.println(" [-2] - DEBUG");
		}
		for (char i = 0; i <= optionsList.size()-2; i++) {
			System.out.println(STR." [\{i + 1}] - \{optionsList.get(i)}");
		}
		if (last_zero==1) {
			System.out.println(STR." [0] - \{optionsList.getLast()}");
		} else {
			System.out.println(STR." [\{optionsList.size()}] - \{optionsList.getLast()}");
		}

		Reader scanner = new Reader(System.in);
		scanner.setHandler(Main::receivedCTRLD);

		int selected;
		// Input loop until a valid selection is made
		while (true) {
			System.out.print("Select an option: ");
			String input = scanner.readLine();
			try {
				selected = Integer.parseInt(input);
				if (selected == Main.DEBUG && debug == Main.DEBUG) return -2;
				if (selected >= 1 - last_zero && selected <= optionsList.size() - last_zero) return selected;
				else System.out.println("Invalid option. Please choose a valid option.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}
}
