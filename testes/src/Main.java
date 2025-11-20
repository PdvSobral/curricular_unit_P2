import java.util.Scanner;

@SuppressWarnings("UnnecessaryModifier")
public class Main {

    public static void main(String[] args) {
		final Scanner terminal_input = new Scanner(System.in);

		@SuppressWarnings("WriteOnlyObject") teste Lol = new teste();
		Lol.setName("BANANA");
		Lol.Number(12);
		Lol.setNumber(33641);

		System.out.println("Hello and welcome!");
		System.out.print("Enter a number: ");
		int userInput = terminal_input.nextInt();

        System.out.println(userInput);
	}
}
