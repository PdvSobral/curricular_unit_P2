public class Conta {
	final private String __titular;
	private double __saldo;

	public Conta(String titular){
		this.__titular = titular;
		this.__saldo = 0.0;
	};
	public Conta(String titular, double valor){ // saldo inicial
		this.__titular = titular;
		this.__saldo = valor;
	}

	// Metodos
	public void depositar(double valor){
		this.__saldo+=valor;
	}
	public void depositar(int valor){ // sobrecarga
		depositar((double) valor);
	}

	public void levantar(double valor){
		this.__saldo-=valor;
	}

	public double getSaldo(){
		return this.__saldo;
	}

	public String getTitular(){
		return this.__titular;
	}
}
