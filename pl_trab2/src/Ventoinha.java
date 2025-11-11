public class Ventoinha extends Dispositivo {
	private int __velocidade = 0; // 0–3

	public Ventoinha(String nome){
		super(nome);
	}
	public Ventoinha(String nome, int velocidade){
		super(nome);
		setVelocidade(velocidade);
	}

	public void setVelocidade(int valor){
		if (valor >= 0 && valor <= 3) {
			this.__velocidade = valor;
		} else {
			System.out.println("[!] setVelocidade (Class Ventoinha): Valor da velocidade inválido [!]");
		}
	}
	public void setVelocidade(String modo){ // "off" "baixo", "medio", "alto"
		switch (modo){
			case "off": this.__velocidade = 0; break;
			case "baixo": this.__velocidade = 1; break;
			case "médio": this.__velocidade = 2; break;
			case "alto": this.__velocidade = 3; break;
			default:
				System.out.println("[!] setVelocidade (Class Ventoinha): Valor da velocidade inválido [!]");
		}
	}
}
