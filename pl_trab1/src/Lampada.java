@SuppressWarnings({"unused", "UnnecessaryReturnStatement"})
public class Lampada {
	private int __brilho;
	private String __cor;
	private boolean __estado;

	private void __setDefaults(){
		this.__brilho = 50;
		this.__cor = "#FFFFFF";
		this.__estado = false;
	}

	//// Constructors ////
	public Lampada(){
		this.__setDefaults();
	}
	public Lampada(int brilho) {
		this.__setDefaults();
		this.__brilho = brilho;
	}
	public Lampada(String cor) {
		this.__setDefaults();
		this.__cor = cor;
	}
	public Lampada(boolean estado) {
		this.__setDefaults();
		this.__estado = estado;
	}
	public Lampada(int brilho, String cor) {
		this.__setDefaults();
		this.__brilho = brilho;
		this.__cor = cor;
	}
	public Lampada(int brilho, boolean estado) {
		this.__setDefaults();
		this.__brilho = brilho;
		this.__estado = estado;
	}
	public Lampada(String cor, boolean estado){
		this.__setDefaults();
		this.__cor = cor;
		this.__estado = estado;
	}

	//// METHODS RELATED TO this.__estado ////
	public void ligar(){
		__estado = true;
		return;
	}
	public void desligar(){
		__estado = false;
		return;
	}
	public boolean getEstado(){
		return __estado;
	}

	//// METHODS RELATED TO this.__cor ////
	private void __mudarCor(String nova_cor){
		__cor = nova_cor;
	}
	public void mudarCor(String nova_cor){
		__mudarCor(nova_cor);
		return;
	}
	public void mudarCor(int nova_cor){
		__mudarCor(String.valueOf(nova_cor));
		return;
	}
	public void mudarCor(long nova_cor){
		__mudarCor(String.valueOf(nova_cor));
		return;
	}
	public void mudarCor(char nova_cor){
		__mudarCor(String.valueOf(nova_cor));
		return;
	}
	public void mudarCor(double nova_cor){
		__mudarCor(String.valueOf(nova_cor));
		return;
	}
	public void mudarCor(){
		__mudarCor("#FFFFFF");
		return;
	}
	public String getCor(){
		return __cor;
	}

	//// METHODS RELATED TO this.__estado ////
	private void __setBrilho(int novo_brilho){
		if (novo_brilho >= 0 && novo_brilho <= 100){
			this.__brilho = novo_brilho;
		} else {
			System.out.println("\033[93m[?] Warning (by __setBrilho): Provided value is not within accepted interval [0-100]! Skipping!\033[m");
		}
		return;
	}
	public void setBrilho(String novo_brilho){
		this.__setBrilho(Integer.parseInt(novo_brilho));
		return;
	}
	public void setBrilho(int novo_brilho){
		this.__setBrilho(novo_brilho);
		return;
	}
	public void setBrilho(long novo_brilho){
		this.__setBrilho((int) novo_brilho);
		return;
	}
	public void setBrilho(char novo_brilho){
		this.__setBrilho(novo_brilho);
		return;
	}
	public void setBrilho(double novo_brilho){
		this.__setBrilho((int) novo_brilho);
		return;
	}
	public void setBrilho(){
		this.__setBrilho(50);
		return;
	}
	public int getBrilho(){
		return this.__brilho;
	}
}
