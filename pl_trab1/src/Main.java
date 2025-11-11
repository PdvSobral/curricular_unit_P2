// import java.util.Scanner;
// final Scanner terminal_input = new Scanner(System.in);
// int userInput = terminal_input.nextInt();

public class Main {
    public static void main(String[] args) {
		System.out.println("[*] Starting...");
		System.out.println("[*] Initializing objects...");
		Lampada lampada1 = new Lampada(true);
		Lampada lampada2 = new Lampada(false);
		CasaInteligente casa_inteli = new CasaInteligente();
		System.out.println("[*] \tDone!");
		System.out.println("[*] Setting intensities...");
		lampada1.setBrilho(80);
		lampada2.setBrilho(60);
		System.out.println("[*] \tDone!");
		System.out.println("[*] Trying overflow on Lamp2...");
		lampada2.setBrilho(200);
		System.out.println("[*] \tDone!");
		System.out.println("[*] Printing states...");
		System.out.print("[*] \tLamp1: ");
		System.out.println(lampada1.getEstado());
		System.out.print("[*] \tLamp2: ");
		System.out.println(lampada2.getEstado());
		System.out.println("[*] \t\tDone!");
		System.out.println("[*] Turning casa on...");
		casa_inteli.ligarTudo();
		System.out.println("[*] \tDone!");
		System.out.println("[*] Turning casa off...");
		casa_inteli.desligarTudo();
		System.out.println("[*] \tDone!");
		System.out.println("[*] Exiting...");
		return;
    }
}
