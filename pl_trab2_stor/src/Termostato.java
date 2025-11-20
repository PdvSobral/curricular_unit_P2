// ===== Subclasse: Termostato =====
@SuppressWarnings("preview")
class Termostato extends Dispositivo {
	private double temperatura; // 10.0..30.0 °C

	public Termostato(String nome) { this(nome, 21.0); }
	public Termostato(String nome, double valor) {
		super(nome);
		setTemperatura(valor);
	}

	public void setTemperatura(double valor) {
		if (valor < 10.0) valor = 10.0;
		if (valor > 30.0) valor = 30.0;
		this.temperatura = Math.round(valor * 10.0) / 10.0; // 1 casa decimal
		ligar(); // ao definir um alvo, assume-se ligado
	}
	// Sobrecarga: aceita "21.5C" ou "70F"
	@SuppressWarnings("DuplicateExpressions")
	public void setTemperatura(String texto) {
		if (texto == null || texto.isBlank()) return;
		String s = texto.trim().toUpperCase();
		try {
			if (s.endsWith("C")) {
				setTemperatura(Double.parseDouble(s.substring(0, s.length() - 1)));
			} else if (s.endsWith("F")) {
				double f = Double.parseDouble(s.substring(0, s.length() - 1));
				double c = (f - 32) * 5.0 / 9.0;
				setTemperatura(c);
			} else {
				setTemperatura(Double.parseDouble(s)); // assume Celsius
			}
		} catch (NumberFormatException e) {
			System.out.println(STR."Temperatura inválida: \{texto}");
		}
	}

	@Override public String toString() {
		return STR."Termostato{nome='\{getNome()}', ligado=\{isLigado()}, temperatura=\{temperatura}°C}";
	}
}