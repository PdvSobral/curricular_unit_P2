// ===== Subclasse: ContaPoupanca =====
@SuppressWarnings("preview")
class ContaPoupanca extends Conta {
	private double taxaJuro; // ex: 0.03 = 3%/ano

	public ContaPoupanca(String titular) { this(titular, 0.0, 0.02); }
	public ContaPoupanca(String titular, double valor, double taxaJuro) {
		super(titular, valor);
		setTaxaJuro(taxaJuro);
	}

	public void setTaxaJuro(double taxa) {
		if (taxa < 0 || taxa > 0.20) throw new IllegalArgumentException("Taxa fora de [0..0.20]");
		this.taxaJuro = taxa;
	}

	public void renderJuros() {
		double acrescimo = getSaldo() * taxaJuro;
		super.depositar(acrescimo); // reutiliza validação
		System.out.printf("Juros aplicados (%.2f%% ano).%n", taxaJuro * 100);
	}
	public void renderJuros(int meses) {
		if (meses <= 0) { System.out.println("Meses inválidos"); return; }
		double acrescimo = getSaldo() * (taxaJuro * (meses / 12.0));
		super.depositar(acrescimo);
		System.out.printf("Juros aplicados (%d meses, %.2f%% anual).%n", meses, taxaJuro * 100);
	}

	@Override public String toString() {
		return STR."ContaPoupanca{titular='\{getTitular()}', saldo=\{String.format("%.2f", getSaldo())}, taxaJuro=\{taxaJuro}}";
	}
}