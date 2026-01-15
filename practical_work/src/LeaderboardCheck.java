import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class LeaderboardCheck {
	// GPTed and then manually made to work. No time to discuss philosophy.
	// THIS WAS NOT VIBE-CODDED! Let's be clear on that.

	private JTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;

	// Numeric filters
	private JTextField playerFilterField;
	private JTextField gameFilterField;
	private JTextField scoreFilterField;
	private JComboBox<String> scoreConditionBox;

	public LeaderboardCheck() {
		final int[] exit_mode = {0};
		Map<Integer, ArrayList<Tuple<Integer, Integer>>> leaderboard = Database.getInstance().loadLeaderboardsByGame();

		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		// content panel
		ContentPanel panel = interfaceWrapper.getContentSpace();

		// for button reassignment
		ControlPanel controls = interfaceWrapper.getControlSpace();

		CircularButton return_btn = controls.getButton("Return");


		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			panel.setLayout(new BorderLayout());

			/* ===================== TABLE ===================== */
			model = new DefaultTableModel(
					new String[]{
							"Game ID",
							"Game Name",
							"Player ID",
							"Player Name",
							"Machine Name",
							"Score"
					}, 0
			);

			table = new JTable(model);
			sorter = new TableRowSorter<>(model);
			table.setRowSorter(sorter);

			// Populate rows
			for (Map.Entry<Integer, ArrayList<Tuple<Integer, Integer>>> entry : leaderboard.entrySet()) {
				int gameId = entry.getKey();
				String gameName = Database.getInstance().loadGame(gameId).getName();
				String machineName = Database.getInstance().loadGameMachine(gameId).getName();
				for (Tuple<Integer, Integer> t : entry.getValue()) {
					int playerId = t.getKey();
					int score = t.getValue();
					String playerName = Database.getInstance().loadPlayer(playerId).getName();
					model.addRow(new Object[]{gameId, gameName, playerId, playerName, machineName, score});
				}
			}
			panel.add(new JScrollPane(table), BorderLayout.CENTER);

			/* ===================== FILTER PANEL ===================== */
			JPanel filterPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			playerFilterField = new JTextField(8);
			gameFilterField = new JTextField(8);
			scoreFilterField = new JTextField(8);

			scoreConditionBox = new JComboBox<>(new String[]{"=", ">", "<"});

			JButton applyButton = new JButton("Apply Filters");
			JButton clearButton = new JButton("Clear Filters");

			// Row 0
			gbc.gridx = 0; gbc.gridy = 0;
			filterPanel.add(new JLabel("Player ID:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(playerFilterField, gbc);

			gbc.gridx = 2;
			filterPanel.add(new JLabel("Game ID:"), gbc);
			gbc.gridx = 3;
			filterPanel.add(gameFilterField, gbc);

			// Row 1
			gbc.gridx = 0; gbc.gridy = 1;
			filterPanel.add(new JLabel("Score:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(scoreConditionBox, gbc);
			gbc.gridx = 2;
			filterPanel.add(scoreFilterField, gbc);

			gbc.gridx = 3;
			filterPanel.add(applyButton, gbc);

			// Row 2
			gbc.gridx = 3; gbc.gridy = 2;
			filterPanel.add(clearButton, gbc);

			panel.add(filterPanel, BorderLayout.NORTH);

			// ===== BUTTON ACTIONS =====
			applyButton.addActionListener(e -> {
				List<RowFilter<Object, Object>> filters = new ArrayList<>();

				if (!playerFilterField.getText().trim().isEmpty()) {
					int playerId = Integer.parseInt(playerFilterField.getText().trim());
					filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, playerId, 1));
				}

				if (!gameFilterField.getText().trim().isEmpty()) {
					int gameId = Integer.parseInt(gameFilterField.getText().trim());
					filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, gameId, 0));
				}

				if (!scoreFilterField.getText().trim().isEmpty()) {
					int score = Integer.parseInt(scoreFilterField.getText().trim());
					String condition = (String) scoreConditionBox.getSelectedItem();

					RowFilter.ComparisonType type = switch (condition) {
						case ">" -> RowFilter.ComparisonType.AFTER;
						case "<" -> RowFilter.ComparisonType.BEFORE;
						default -> RowFilter.ComparisonType.EQUAL;
					};

					filters.add(RowFilter.numberFilter(type, score, 2));
				}

				sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
			});
			clearButton.addActionListener(e -> {
				playerFilterField.setText("");
				gameFilterField.setText("");
				scoreFilterField.setText("");
				scoreConditionBox.setSelectedIndex(0);
				sorter.setRowFilter(null);
			});

			return_btn.addActionListener(e -> { exit_mode[0] = 2; });
			panel.revalidate();
			panel.repaint();
		});

		while (exit_mode[0] == 0){
			try { Thread.sleep(100); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		SwingUtilities.invokeLater(return_btn::removeActions);
		return;
	}
}
