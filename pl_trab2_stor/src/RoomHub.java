// ===== Controlador: RoomHub =====
@SuppressWarnings("preview")
record RoomHub(Lampada lampada, Ventoinha ventoinha, Termostato termostato) {

	public void aplicarCena(String nome){
		aplicarCena(nome, 50);
	} // default

	public void aplicarCena(String nome, int intensidade){
		String n = (nome == null) ? "" : nome.toLowerCase();
		switch (n){
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
				System.out.println(STR."Cena desconhecida: \{nome}");
		}
		System.out.println(STR."Cena \"\{nome}\" aplicada com intensidade \{intensidade}");
	}

	@Override
	public String toString(){
		return STR."\n\{lampada}\n\{ventoinha}\n\{termostato}";
	}
}
