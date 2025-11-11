// ===== Subclasse: Ventoinha =====
class Ventoinha extends Dispositivo {
	private int velocidade; // 0..3

	public Ventoinha(String nome) { this(nome, 0); }
	public Ventoinha(String nome, int velocidade) {
		super(nome);
		setVelocidade(velocidade);
	}

	public void setVelocidade(int valor) {
		if (valor < 0) valor = 0;
		if (valor > 3) valor = 3;
		this.velocidade = valor;
		if (valor == 0) desligar(); else ligar();
	}
	// Sobrecarga por String
	public void setVelocidade(String modo) {
		if (modo == null) return;
		switch (modo.toLowerCase()) {
			case "off": setVelocidade(0); break;
			case "baixo":setVelocidade(1); break;
			case "medio":
			case "médio":setVelocidade(2); break;
			case "alto": setVelocidade(3); break;
			default: System.out.println("Modo de ventoinha inválido: " + modo);
		}
	}

	@Override public String toString() {
		return "Ventoinha{nome='" + getNome() + "', ligado=" + isLigado() + ", velocidade=" + velocidade + "}";
	}
}
