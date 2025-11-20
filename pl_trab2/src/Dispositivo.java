public class Dispositivo {
	private final String __nome;
	private boolean __ligado;

	public Dispositivo(String nome){
		this.__nome = nome;
		this.__ligado = false;
	}
	public void ligar(){
		this.__ligado = true;
	}
	public void desligar(){
		this.__ligado = false;
	}

	public boolean isLigado(){
		return this.__ligado;
	}

	public String getNome(){
		return this.__nome;
	}
}

