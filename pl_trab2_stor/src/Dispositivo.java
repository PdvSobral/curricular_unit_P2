// ===== Classe base: Dispositivo =====
class Dispositivo {
	private final String nome; // Encapsulado: imutável após construção
	private boolean ligado;

	public Dispositivo(String nome) {
		if (nome == null || nome.isBlank()) throw new IllegalArgumentException
				("Nome inválido");
		this.nome = nome;
		this.ligado = false;
	}

	public void ligar() { this.ligado = true; }

	public void desligar(){ this.ligado = false; }

	public boolean isLigado() { return ligado; }

	public String getNome() { return nome; }

	@Override public String toString() {
		return getClass().getSimpleName() + "{" + "nome='" + nome + "', ligado =" + ligado + "}";
	}
}
