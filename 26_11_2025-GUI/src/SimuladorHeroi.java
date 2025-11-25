import javax.swing.*;
import java.awt.*;

public class SimuladorHeroi { // Helper: carregar e redimensionar imagem para um tamanho fixo
    private static ImageIcon loadAndScale(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) {

        // Instância da lógica
        Heroi heroi = new Heroi("Nova Estrela");

        // -------------------------
        // CARREGAR IMAGENS DO HERÓI
        // -------------------------
        int HERO_WIDTH = 260;
        int HERO_HEIGHT = 180;

        // Coloca as imagens numa pasta "img" ao lado do src
        ImageIcon iconNormal = loadAndScale("img/heroi_normal.png", HERO_WIDTH, HERO_HEIGHT);
        ImageIcon iconCansado = loadAndScale("img/heroi_cansado.png", HERO_WIDTH, HERO_HEIGHT);
        ImageIcon iconPower = loadAndScale("img/heroi_power.png", HERO_WIDTH, HERO_HEIGHT);

        // -------------------------
        // JANELA PRINCIPAL
        // -------------------------
        JFrame frame = new JFrame("Simulador de Herói – com Imagens");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 360);
        frame.setLayout(new BorderLayout());

        // Cor de fundo da janela (um cinza ligeiramente azulado)
        frame.getContentPane().setBackground(new Color(235, 240, 245));

        // -------------------------
        // TOPO – NOME DO HERÓI
        // -------------------------
        JLabel labelNome = new JLabel("Herói: " + heroi.getNome(), SwingConstants.CENTER);
        labelNome.setFont(new Font("Arial", Font.BOLD, 26));
        labelNome.setForeground(new Color(30, 30, 60));

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(215, 225, 245)); // barra superior colorida
        painelTopo.add(labelNome);

        // -------------------------
        // CENTRO ESQUERDO – IMAGEM
        // -------------------------
        JLabel labelImagem = new JLabel(iconNormal);
        labelImagem.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagem.setVerticalAlignment(SwingConstants.CENTER);

        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setBackground(new Color(245, 245, 245)); // fundo claro atrás da imagem
        painelImagem.add(labelImagem, BorderLayout.CENTER);
        painelImagem.setPreferredSize(new Dimension(300, 220));

        // -------------------------
        // CENTRO DIREITO – ESTADO
        // -------------------------
        JLabel labelNivel = new JLabel();
        JLabel labelXp = new JLabel();
        JLabel labelEnergia = new JLabel();

        labelNivel.setFont(new Font("Arial", Font.PLAIN, 18));
        labelXp.setFont(new Font("Arial", Font.PLAIN, 18));
        labelEnergia.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel painelEstado = new JPanel(new GridLayout(3, 1, 0, 10));
        painelEstado.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        painelEstado.setBackground(new Color(235, 240, 245));
        painelEstado.add(labelNivel);
        painelEstado.add(labelXp);
        painelEstado.add(labelEnergia);

        // Junta imagem + estado lado a lado
        JPanel painelCentro = new JPanel(new GridLayout(1, 2));
        painelCentro.setBackground(new Color(235, 240, 245));
        painelCentro.add(painelImagem);
        painelCentro.add(painelEstado);

        // -------------------------
        // BAIXO – MENSAGEM + BOTÕES
        // -------------------------
        JLabel labelMensagem = new JLabel("Pronto para começar o treino!", SwingConstants.CENTER);
        labelMensagem.setFont(new Font("Arial", Font.ITALIC, 14));
        labelMensagem.setForeground(new Color(60, 60, 60));

        JButton btnTreinar = new JButton("Treinar");
        JButton btnDescansar = new JButton("Descansar");
        JButton btnMissao = new JButton("Missão");

        // Um pouco de estilo nos botões
        Color btnBg = new Color(230, 230, 240);
        btnTreinar.setBackground(btnBg);
        btnDescansar.setBackground(btnBg);
        btnMissao.setBackground(btnBg);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBackground(new Color(235, 240, 245));
        painelBotoes.add(btnTreinar);
        painelBotoes.add(btnDescansar);
        painelBotoes.add(btnMissao);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBackground(new Color(235, 240, 245));
        painelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        painelInferior.add(labelMensagem, BorderLayout.CENTER);
        painelInferior.add(painelBotoes, BorderLayout.SOUTH);

        // -------------------------
        // FUNÇÃO: atualizar estado visual
        // -------------------------
        Runnable atualizarEstadoVisual = () -> {
            labelNivel.setText("Nível: " + heroi.getNivel());
            labelXp.setText("XP: " + heroi.getXp() + " / 100");
            labelEnergia.setText("Energia: " + heroi.getEnergia() + " / 100");

            int energia = heroi.getEnergia();

            // Cor da energia
            if (energia > 60) {
                labelEnergia.setForeground(new Color(0, 150, 0)); // verde
            } else if (energia > 30) {
                labelEnergia.setForeground(new Color(210, 140, 0)); // laranja
            } else {
                labelEnergia.setForeground(new Color(200, 0, 0)); // vermelho
            }

            // Imagem do herói consoante a energia
            if (energia <= 30) {
                labelImagem.setIcon(iconCansado);
            } else if (energia >= 80) {
                labelImagem.setIcon(iconPower);
            } else {
                labelImagem.setIcon(iconNormal);
            }
        };

        // Atualizar pela primeira vez
        atualizarEstadoVisual.run();

        // -------------------------
        // EVENTOS DOS BOTÕES
        // -------------------------
        btnTreinar.addActionListener(e -> {
            String msg = heroi.treinar();
            labelMensagem.setText(msg);
            atualizarEstadoVisual.run();
        });

        btnDescansar.addActionListener(e -> {
            String msg = heroi.descansar();
            labelMensagem.setText(msg);
            atualizarEstadoVisual.run();
        });

        btnMissao.addActionListener(e -> {
            String msg = heroi.fazerMissao();
            labelMensagem.setText(msg);
            atualizarEstadoVisual.run();
        });

        // -------------------------
        // ADICIONAR TUDO À JANELA
        // -------------------------
        frame.add(painelTopo, BorderLayout.NORTH);
        frame.add(painelCentro, BorderLayout.CENTER);
        frame.add(painelInferior, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // centra a janela no ecrã
        frame.setVisible(true);
    }
}
