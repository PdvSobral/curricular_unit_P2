import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serial;
import java.util.List;

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
			System.out.println(STR." [-2] - DEBUG");
		}
		for (char i = 0; i <= optionsList.size()-2; i++) {
			System.out.println(STR." [\{i + 1}] - \{optionsList.get(i)}");
		}
		if (last_zero==1) {
			System.out.println(STR." [0] - \{optionsList.get(optionsList.size()-1)}");
		} else {
			System.out.println(STR." [\{optionsList.size()}] - \{optionsList.get(optionsList.size()-1)}");
		}

		BufferedReader scanner = new Reader(System.in);
		int selected;
		// Input loop until a valid selection is made
		while (true) {
			System.out.print("Select an option: ");
			String input = scanner.readLine();
			try {
				if (input == null) Main.receivedCTRLD();
				else {
					selected = Integer.parseInt(input);
					if (selected == Main.DEBUG && debug == Main.DEBUG) return -2;
					if (selected >= 1 - last_zero && selected <= optionsList.size() - last_zero) return selected;
					else System.out.println("Invalid option. Please choose a valid option.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}
}
