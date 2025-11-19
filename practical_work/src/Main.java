import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter an integer for exercise: ");
		int temp = scanner.nextInt();
		if (temp==1){
			Database.getInstance().setMainSaveDirectory("./practical_work/db");
			Database.getInstance().setGamesSubdirectory("games");

			Game teste = new Game(2001, "Space Odessey", new ArrayList<>(), "space", "no idea", "");
			System.out.println(teste);
			teste.save();
			System.out.println("Loading...");
			Game test = Database.getInstance().loadGame("2001_Space-Odessey.gm");
			System.out.println(test);
		} else if (temp==2) {
			System.out.println("2");
		} else if (temp==3) {
			System.out.println("3"); // desconto extra (mínimo 3.0)
		} else {
			System.out.print("No exercise with that number");
		}
	}
}
