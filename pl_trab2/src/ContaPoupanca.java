public class ContaPoupanca extends Conta{
	final private double __taxaJuro; // taxa = 0.02 default

	public ContaPoupanca(String titular){
		super(titular);
		this.__taxaJuro = 0.02;
	}
	public ContaPoupanca(String titular, double valor){
		super(titular, valor);
		this.__taxaJuro = 0.02;
	}
	public ContaPoupanca(String titular, double valor, double taxa){
		super(titular, valor);
		this.__taxaJuro = taxa;
	}

	public void renderJuros() { // aplica juro anual
		this.depositar(this.getSaldo() * __taxaJuro);
	}

	public void renderJuros(int mes) { // aplica juro proporcional (sobrecarregado)
		this.depositar(this.getSaldo() * __taxaJuro * (mes / 12.0));
	}
}
/*

 */