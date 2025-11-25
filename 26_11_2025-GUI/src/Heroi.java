class Heroi {
    private String nome;
    private int nivel;
    private int xp;
    private int energia;

    public Heroi(String nome) {
        this.nome = nome;
        this.nivel = 1;
        this.xp = 0;
        this.energia = 100;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getXp() {
        return xp;
    }

    public int getEnergia() {
        return energia;
    }

    public String treinar() {
        if (energia < 10) {
            return "Energia insuficiente para treinar!";
        }
        energia -= 10;
        ganharXp(15);
        return "Treino concluído! Ganhou 15 XP e gastou 10 de energia.";
    }

    public String descansar() {
        if (energia >= 100) {
            return "O herói já está com energia máxima!";
        }
        energia += 20;
        if (energia > 100)
            energia = 100;
        return "Descanso feito! Recuperou 20 de energia.";
    }

    public String fazerMissao() {
        if (energia < 40) {
            return "Energia insuficiente para ir numa missão!";
        }
        energia -= 40;
        ganharXp(40);
        return "Missão concluída! Ganhou 40 XP e gastou 40 de energia.";
    }

    private void ganharXp(int quantidade) {
        xp += quantidade;
        while (xp >= 100) {
            xp -= 100;
            nivel++;
        }
    }
}