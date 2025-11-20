public class Bilhete {
	final private double __base; // preço base

	public Bilhete(double base){
		if (base<=3) this.__base = 3.0; else this.__base = base;
	}

	private double parseOut(double out){
		return Math.max(out, 3.0);
	}

	public double preco(int idade){
		if (idade < 12) return parseOut(this.__base / 2.0);
		else if (idade >= 65) return parseOut(this.__base * 0.8);
		else return parseOut(this.__base);
	}
	public double preco(boolean estudante){
		if (estudante) return parseOut(this.__base * 0.8);
		else return parseOut(this.__base);
	}
	public double preco(int idade, boolean estudante){
		if (estudante) return parseOut(preco(idade) * 0.8);
		else return parseOut(preco(idade));
	}
}
/*
Regras:
<12 anos → −50%
≥65 anos → −30%
Estudante → −20%
Preço mínimo: 3.00€
*/