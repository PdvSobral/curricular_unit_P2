// ===== Classe base: Bilhete =====
@SuppressWarnings("preview")
class Bilhete {
	private final double base; // preço base (mínimo 3.0)
	public Bilhete(double base) {
		if (base < 3.0) throw new IllegalArgumentException("Preço base mínimoé 3.0€");
		this.base = base;
	}
	public double getBase() { return base; }
	// Sobrecarga 1: só idade
	public double preco(int idade) {
		return max3(calcularBase(idade, false));
	}
	// Sobrecarga 2: só estudante
	public double preco(boolean estudante) {
		return max3(calcularBase(-1, estudante)); // idade desconhecida → ignora descontos de idade
	}
	// Sobrecarga 3: idade + estudante
	public double preco(int idade, boolean estudante) {
		return max3(calcularBase(idade, estudante));
	}
	// Lógica comum centralizada
	protected double calcularBase(int idade, boolean estudante) {
		double preco = base;
		if (idade >= 0) {
			if (idade < 12)
				preco *= 0.5; // -50%
			else if (idade >= 65) preco *= 0.7; // -30%
		}
		if (estudante) preco *= 0.8; // -20%
		return preco;
	}
	protected double max3(double valor) { return (valor < 3.0) ? 3.0 : round2(valor); }
	protected double round2(double v) { return Math.round(v * 100.0) / 100.0; }
	@Override public String toString() {
		return STR."Bilhete{base=\{base}}";
	}
}