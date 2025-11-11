public class Lampada extends Dispositivo {
	private int __intensidade = 0; // 0–100

	public Lampada(String nome){
		super(nome);
	}
	public Lampada(String nome, int intensidade){
		super(nome);
		ajustarIntensidade(intensidade);
	}

	public void ajustarIntensidade(int valor){
		if (valor >= 0 && valor <= 100) {
			this.__intensidade = valor;
		} else if (valor > 100) {
			this.__intensidade = 100;
			System.out.println("[!] ajustarIntensidade (Class Lampada): Valor da intensidade inválido (too big). Set at 100 [!]");
		} else {
			this.__intensidade = 0;
			System.out.println("[!] ajustarIntensidade (Class Lampada): Valor da intensidade inválido (too small). Set at 0 [!]");
		}
	}
	public void ajustarIntensidade(String modo){ // "desligado" "baixo", "medio", "alto"
		switch (modo){
			case "desligado": this.__intensidade = 0; break;
			case "baixo": this.__intensidade = 10; break;
			case "médio": this.__intensidade = 60; break;
			case "alto": this.__intensidade = 100; break;
			default:
				System.out.println("[!] ajustarIntensidade (Class Lampada): Valor da intensidade inválido [!]");
		}
	}
}
