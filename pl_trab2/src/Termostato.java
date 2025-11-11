public class Termostato extends Dispositivo {
	private float __temperatura = 0; // 10.0 – 30.0

	public Termostato(String nome){
		super(nome);
	}

	public Termostato(String nome, float temperatura){
		super(nome);
		setTemperatura(temperatura);
	}

	public void setTemperatura(float temperatura_in_c){
		if (temperatura_in_c >= 10.0 && temperatura_in_c <= 30.0){
			this.__temperatura = temperatura_in_c;
		} else {
			System.out.println("[!] setTemperatura (Class Termostato): Valor da temperatura inválido [!]");
		}
	}

	public void setTemperatura(String texto){ // ex: "21.5C" ou "70F"
		float temperature;
		char t = texto.charAt(texto.length() - 1);
		if (texto.endsWith("F") || texto.endsWith("f")){
			temperature = Float.parseFloat(texto.substring(0, texto.length()-1));
			temperature = (temperature - 32) / (float) 1.8;
		} else if (texto.endsWith("C") || texto.endsWith("c")){
			temperature = Float.parseFloat(texto.substring(0, texto.length() - 1));
		} else {
			temperature = Float.parseFloat(texto);
		}
		this.__temperatura = temperature;
	}
}
