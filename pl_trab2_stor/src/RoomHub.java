// ===== Controlador: RoomHub =====
class RoomHub {
	private final Lampada lampada;
	private final Ventoinha ventoinha;
	private final Termostato termostato;

	public RoomHub(Lampada l, Ventoinha v, Termostato t) {
		this.lampada = l;
		this.ventoinha = v;
		this.termostato = t;
	}

	public void aplicarCena(String nome) { aplicarCena(nome, 50); } // default
	public void aplicarCena(String nome, int intensidade) {
		String n = (nome == null) ? "" : nome.toLowerCase();
		switch (n) {
			case "filme":
				lampada.ajustarIntensidade(intensidade); // tipicamente 30
				ventoinha.setVelocidade(1);
				termostato.setTemperatura(21.0);
				break;
			case "foco":
				lampada.ajustarIntensidade(Math.max(60, intensidade));
				ventoinha.setVelocidade(2);
				termostato.setTemperatura(22.0);
				break;
			case "off":
			case "desligar":
				lampada.ajustarIntensidade(0);
				ventoinha.setVelocidade(0);
				termostato.desligar();
				break;
			default:
				System.out.println("Cena desconhecida: " + nome);
		}
		System.out.println("Cena \"" + nome + "\" aplicada com intensidade " + intensidade);
	}

	@Override public String toString() {
		return lampada + "\n" + ventoinha + "\n" + termostato;
	}
}