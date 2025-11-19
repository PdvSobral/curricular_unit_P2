public class Menu {
	private static final Menu __instance = new Menu();
	// will contain the strings to print
	// if last option = 0, input checks, etc...
	private Menu() {}

	public static Menu getInstance() {
		return __instance;
	}
}
