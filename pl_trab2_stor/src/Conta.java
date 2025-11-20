// ===== Classe base: Conta =====
@SuppressWarnings("preview")
class Conta {
	private final String titular;
	private double saldo;

	public Conta(String titular) { this(titular, 0.0); }
	public Conta(String titular, double valor) {
		if (titular == null || titular.isBlank()) throw new IllegalArgumentException
				("Titular inválido");
		if (valor < 0) throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
		this.titular = titular;
		this.saldo = valor;
	}

	public void depositar(double valor) {
		if (valor <= 0) { System.out.println("Depósito inválido"); return; }
		saldo += valor;
		System.out.printf("Depósito de %.2f€ efetuado.%n", valor);
	}
	// Sobrecarga por int → delega
	public void depositar(int valor) { depositar((double) valor); }

	public void levantar(double valor) {
		if (valor <= 0) { System.out.println("Levantamento inválido"); return; }
		if (valor > saldo) { System.out.println("Fundos insuficientes"); return; }
		saldo -= valor;
		System.out.printf("Levantamento de %.2f€ efetuado.%n", valor);
	}

	public double getSaldo() { return saldo; }

	public String getTitular() { return titular; }

	@Override public String toString() {
		return STR."Conta{titular='\{titular}', saldo=\{String.format("%.2f", saldo)}}";
	}
}