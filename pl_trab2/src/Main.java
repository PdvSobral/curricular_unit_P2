import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter an integer for exercise: ");
		int temp = scanner.nextInt();
		if (temp==1){
			Lampada l = new Lampada("Mesa", 120); // intensidade será 100 (limitada)
			Ventoinha v = new Ventoinha("Teto", 2);
			Termostato t = new Termostato("Parede");
			t.setTemperatura("70F"); // converte para cerca de 21°C
			RoomHub hub = new RoomHub(l, v, t);
			hub.addScene("Filme", 30, 2, 21.1);
			hub.aplicarCena("Filme", 30);
		} else if (temp==2) {
			ContaPoupanca c = new ContaPoupanca("Ana", 100, 0.03);
			c.depositar(50);
			System.out.println("Depósito de 50.0€ efetuado com sucesso.");
			c.levantar(30);
			System.out.println("Levantamento de 30.0€ efetuado com sucesso.");
			c.renderJuros(12);
			System.out.println("Juros aplicados com taxa 3.0%.");
			System.out.print("Saldo atual: ");
			System.out.print(c.getSaldo());
			System.out.println("€");
		} else if (temp==3) {
			Bilhete b = new Bilhete(8.0);
			System.out.println(b.preco(10)); // criança
			System.out.println(b.preco(true)); // estudante
			System.out.println(b.preco(70, true)); // sénior + estudante
			BilheteEstudante be = new BilheteEstudante(8.0);
			System.out.println(be.preco(20, true));
		} else {
			System.out.print("No exercise with that number");
		}
	}
}
