// ===== Subclasse: BilheteEstudante =====
class BilheteEstudante extends Bilhete {
	private final double descontoExtra = 0.5; // -0.5€ adicional (mínimo 3€)
	public BilheteEstudante(double base) { super(base); }
	@Override
	protected double calcularBase(int idade, boolean estudante) {
		double p = super.calcularBase(idade, true); // força estudante=true
		p = p - descontoExtra;
		return (p < 3.0) ? 3.0 : round2(p);
	}
}
