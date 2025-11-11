// ===== Subclasse: Lampada =====
class Lampada extends Dispositivo {
	private int intensidade; // 0..100

	public Lampada(String nome) {
		this(nome, 50); // sobrecarga: valor por defeito
	}
	public Lampada(String nome, int intensidade) {
		super(nome);
		setIntensidade(intensidade); // valida/clampa via setter centralizado
	}

	public void ajustarIntensidade(int nova) { setIntensidade(nova); }
	// Sobrecarga por String → mapeia modos para percentagens
	public void ajustarIntensidade(String modo) {
		if (modo == null) return;
		switch (modo.toLowerCase()) {
			case "baixo": setIntensidade(30); break;
			case "medio":
			case "médio": setIntensidade(60); break;
			case "alto": setIntensidade(90); break;
			default: System.out.println("Modo de lâmpada inválido: " + modo);
		}
	}

	private void setIntensidade(int valor) {
		if (valor < 0) valor = 0;
		if (valor > 100) valor = 100;
		this.intensidade = valor;
		if (!isLigado() && intensidade > 0) ligar(); // acende se > 0
	}

	@Override public String toString() {
		return "Lampada{nome='" + getNome() + "', ligado=" + isLigado() + ", intensidade=" + intensidade + "%}";
	}
}