import java.util.ArrayList;

@SuppressWarnings("preview")
public class RoomHub {
	private final Lampada __lampada;
	private final Ventoinha __ventoinha;
	private final Termostato __termostato;
	private final ArrayList<Scene> __scenes;

	/**
	 * @param intensidade For Lampada
	 * @param velocidade  For Ventoinha
	 * @param temperatura For Termostato
	 */
	private record Scene(String nome, int intensidade, int velocidade, double temperatura) {
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
				System.out.println(STR."Cena \"\{scene.nome}\" aplicada com intensidade \{scene.intensidade}");
				return;
			}
		}
		System.out.println(STR."Cena \"\{nome}\" não encontrada.");

	}

	public void addScene(String nome, int intensidade, int velocidade, double temperatura) {
		Scene newScene = new Scene(nome, intensidade, velocidade, temperatura);
		this.__scenes.add(newScene);
		System.out.println(STR."Cena \"\{nome}\" adicionada.");
	}

	/*
	[Termostato: temperatura=21.0°C | ligada=true]
	*/
}
