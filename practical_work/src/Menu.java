import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Menu {
	private static final Menu __instance = new Menu();

	// Private constructor to ensure singleton
	private Menu() {}

	// Get the singleton instance of Menu
	public static Menu getInstance() {
		return __instance;
	}

	// Returns the selected option index (1-based) or -1 if canceled or invalid input
	public int menu(List<String> optionsList, String menu_name, char last_zero) {
		System.out.println(STR."\{menu_name}:");
		for (char i = 0; i <= optionsList.size()-2; i++) {
			System.out.println(STR." [\{i + 1}] - \{optionsList.get(i)}");
		}
		if (last_zero==1) {
			System.out.println(STR." [0] - \{optionsList.get(optionsList.size()-1)}");
		} else {
			System.out.println(STR." [\{optionsList.size()}] - \{optionsList.get(optionsList.size()-1)}");
		}

		Scanner scanner = new Scanner(System.in);
		// Input loop until a valid selection is made
		while (true) {
			System.out.print("Select an option: ");
			String input = scanner.nextLine();
			try {
				int selected = Integer.parseInt(input);
				if (selected >= 1 - last_zero && selected <= optionsList.size() - last_zero) {
					return selected;
				} else {
					System.out.println("Invalid option. Please choose a valid option.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}
}

try{
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Type something (Ctrl+D to disable EOF, Ctrl+C to quit):");
	String line;
	while(true){
		line =reader.
		readLine();
		// Check if Ctrl+D is pressed (EOF) but block it
		if(line ==null){
			if(!allowCtrlD){
			System.out.println("Ctrl+D is disabled. Please enter valid input.");
			continue; // Skip EOF handling
		}else{
			break;  // Exit gracefully if Ctrl+D is allowed (EOF)
		}
	}
	System.out.println("You entered: "+line);
	}
}