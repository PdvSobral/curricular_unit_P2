import java.util.ArrayList;

public class RoomHub {
	private Lampada __lampada;
	private Ventoinha __ventoinha;
	private Termostato __termostato;
	private ArrayList<Scene> __scenes;

	private class Scene {
		public String nome;
		public int intensidade;  // For Lampada
		public int velocidade;   // For Ventoinha
		public double temperatura; // For Termostato

		public Scene(String nome, int intensidade, int velocidade, double temperatura) {
			this.nome = nome;
			this.intensidade = intensidade;
			this.velocidade = velocidade;
			this.temperatura = temperatura;
		}
	}

	public RoomHub(Lampada lampada, Ventoinha ventoinha, Termostato termostato){
		this.__lampada = lampada;
		this.__ventoinha = ventoinha;
		this.__termostato = termostato;
		this.__scenes = new ArrayList<>();
	}

	// public void aplicarCena(String nome);
	public void aplicarCena(String nome, int intensidade){
		for (Scene scene : this.__scenes) {
			if (scene.nome.equals(nome)) {
				this.__lampada.ajustarIntensidade(scene.intensidade); // Set intensity for Lampada
				this.__lampada.ligar();
				System.out.print("[Lâmpada: intensidade=");
				System.out.print(intensidade);
				System.out.print("% | ligada=");
				System.out.print(this.__lampada.isLigado());
				System.out.println("]");
				this.__ventoinha.setVelocidade(scene.velocidade); // Set speed for Ventoinha
				this.__ventoinha.ligar();
				System.out.print("[Ventoínha: velocidade=");
				System.out.print(scene.velocidade);
				System.out.print(" | ligada=");
				System.out.print(this.__ventoinha.isLigado());
				System.out.println("]");
				this.__termostato.setTemperatura((float) scene.temperatura); // Set temperature for Termostato
				this.__termostato.ligar();
				System.out.print("[Termostato: temperatura=");
				System.out.print(scene.temperatura);
				System.out.print("ºC | ligada=");
				System.out.print(this.__termostato.isLigado());
				System.out.println("]");
				System.out.println("Cena \"" + scene.nome + "\" aplicada com intensidade " + scene.intensidade);
				return;
			}
		}
		System.out.println("Cena \"" + nome + "\" não encontrada.");

	}

	public void addScene(String nome, int intensidade, int velocidade, double temperatura) {
		Scene newScene = new Scene(nome, intensidade, velocidade, temperatura);
		this.__scenes.add(newScene);
		System.out.println("Cena \"" + nome + "\" adicionada.");
	}

	/*
	[Termostato: temperatura=21.0°C | ligada=true]
	*/
}
