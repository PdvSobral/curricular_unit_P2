public class CasaInteligente {
	final private Lampada __lampada_quarto;
	final private Lampada __lampada_sala;

	public CasaInteligente(){
		__lampada_quarto = new Lampada();
		__lampada_sala = new Lampada();
	}

	public void ligarTudo(){
		__lampada_quarto.ligar();
		__lampada_sala.ligar();
	}
	public void desligarTudo(){
		__lampada_quarto.desligar();
		__lampada_sala.desligar();
	}
}
