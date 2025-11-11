public class BilheteEstudante extends Bilhete{
	// Adiciona um desconto fixo extra de 0.5€ (mas nunca abaixo de 3€).
	public BilheteEstudante(double valor){
		super(valor-0.5);
	}
}
