@SuppressWarnings({"unused", "UnnecessaryReturnStatement"})
public class Lampada {
	private int __brilho;
	private String __cor;
	private boolean __estado;

	private void __setDefaults(){
		this.__brilho = 155;
		this.__cor = "#FFFFFF";
		this.__estado = false;
	}

	//// Constructors ////
	public Lampada(){
		this.__setDefaults();
	}
	public Lampada(int __brilho) {
		this.__setDefaults();
		this.__brilho = __brilho;
	}
	public Lampada(String __cor) {
		this.__setDefaults();
		this.__cor = __cor;
	}
	public Lampada(boolean __estado) {
		this.__setDefaults();
		this.__estado = __estado;
	}
	public Lampada(int __brilho, String __cor) {
		this.__setDefaults();
		this.__brilho = __brilho;
		this.__cor = __cor;
	}
	public Lampada(int __brilho, boolean __estado) {
		this.__setDefaults();
		this.__brilho = __brilho;
		this.__estado = __estado;
	}
	public Lampada(String __cor, boolean __estado) {
		this.__setDefaults();
		this.__cor = __cor;
		this.__estado = __estado;
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
		this.__brilho = novo_brilho;
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
		this.__setBrilho(150);
		return;
	}
	public int getBrilho(){
		return this.__brilho;
	}

}
