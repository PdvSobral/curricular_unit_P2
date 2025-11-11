import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter an integer for exercise: ");
		int temp = scanner.nextInt();
		if (temp==1){
			Lampada l = new Lampada("Mesa", 120); // fica 100
			Ventoinha v = new Ventoinha("Teto", 2); //FIXME: Dizia "medio", em vez de 2
			Termostato t = new Termostato("Parede");
			t.setTemperatura("70F"); // ~21.1°C
			RoomHub hub = new RoomHub(l, v, t);
			hub.aplicarCena("Filme", 30);
			System.out.println(hub);
		} else if (temp==2) {
			ContaPoupanca c = new ContaPoupanca("Ana", 100, 0.03);
			c.depositar(50);
			c.levantar(30);
			c.renderJuros(12);
			System.out.println(c);
		} else if (temp==3) {
			Bilhete b = new Bilhete(8.0);
			System.out.println(b.preco(10)); // criança: 4.0
			System.out.println(b.preco(true)); // estudante: 6.4
			System.out.println(b.preco(70, true)); // sénior + estudante: 4.48
			BilheteEstudante be = new BilheteEstudante(8.0);
			System.out.println(be.preco(20, true)); // desconto extra (mínimo 3.0)
		} else {
			System.out.print("No exercise with that number");
		}
	}
}
