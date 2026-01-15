import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameCheck {
	// GPTed and then manually made to work. No time to discuss philosophy.
	// THIS WAS NOT VIBE-CODDED! Let's be clear on that.

	private JTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;

	// Filters
	private JTextField gameIdField;
	private JTextField playersField;
	private JComboBox<String> playersConditionBox;
	private JTextField genreField;
	private JTextField developerField;

	public GameCheck() {
		final int[] exit_mode = {0};

		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		ContentPanel panel = interfaceWrapper.getContentSpace();
		ControlPanel controls = interfaceWrapper.getControlSpace();
		CircularButton return_btn = controls.getButton("Return");

		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			panel.setLayout(new BorderLayout());

			/* ===================== TABLE ===================== */
			model = new DefaultTableModel(
					new String[]{
							"Game ID",
							"Name",
							"Year",
							"Allowed Players",
							"Genre",
							"Developer",
							"Description"
					}, 0
			) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			table = new JTable(model);
			sorter = new TableRowSorter<>(model);
			table.setRowSorter(sorter);

			// Populate table with all games
			List<Integer> gameIds = Database.getInstance().listGames(Main.RUNNING_MODE == Main.DEBUG);
			for (int id : gameIds) {
				Game game = Database.getInstance().loadGame(id);

				model.addRow(new Object[]{
						game.getGameId(),
						game.getName(),
						game.getYear(),
						game.getAllowedPlayers(),
						game.getGenre(),
						game.getDeveloper(),
						game.getDescription()
				});
			}

			panel.add(new JScrollPane(table), BorderLayout.CENTER);

			/* ===================== FILTER PANEL ===================== */
			JPanel filterPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gameIdField = new JTextField(8);
			playersField = new JTextField(8);
			playersConditionBox = new JComboBox<>(new String[]{"=", ">", "<"});
			genreField = new JTextField(8);
			developerField = new JTextField(8);

			JButton applyButton = new JButton("Apply Filters");
			JButton clearButton = new JButton("Clear Filters");

			// Row 0
			gbc.gridx = 0;
			gbc.gridy = 0;
			filterPanel.add(new JLabel("Game ID:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(gameIdField, gbc);
			gbc.gridx = 2;
			filterPanel.add(new JLabel("Players:"), gbc);
			gbc.gridx = 3;
			filterPanel.add(playersConditionBox, gbc);
			gbc.gridx = 4;
			filterPanel.add(playersField, gbc);

			// Row 1
			gbc.gridx = 0;
			gbc.gridy = 1;
			filterPanel.add(new JLabel("Genre:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(genreField, gbc);
			gbc.gridx = 2;
			filterPanel.add(new JLabel("Developer:"), gbc);
			gbc.gridx = 3;
			filterPanel.add(developerField, gbc);

			// Row 2
			gbc.gridx = 3;
			gbc.gridy = 2;
			filterPanel.add(applyButton, gbc);
			gbc.gridx = 4;
			filterPanel.add(clearButton, gbc);

			panel.add(filterPanel, BorderLayout.NORTH);

			/* ===================== FILTER LOGIC ===================== */
			applyButton.addActionListener(e -> {
				List<RowFilter<Object, Object>> filters = new ArrayList<>();

				// Game ID (column 0)
				if (!gameIdField.getText().trim().isEmpty()) {
					try {
						int gameId = Integer.parseInt(gameIdField.getText().trim());
						filters.add(RowFilter.numberFilter(
								RowFilter.ComparisonType.EQUAL, gameId, 0));
					} catch (NumberFormatException ignored) {}
				}

				// Allowed Players (column 3)
				if (!playersField.getText().trim().isEmpty()) {
					try {
						int players = Integer.parseInt(playersField.getText().trim());
						String condition = (String) playersConditionBox.getSelectedItem();
						RowFilter.ComparisonType type = switch (condition) {
							case ">" -> RowFilter.ComparisonType.AFTER;
							case "<" -> RowFilter.ComparisonType.BEFORE;
							default -> RowFilter.ComparisonType.EQUAL;
						};
						filters.add(RowFilter.numberFilter(type, players, 3));
					} catch (NumberFormatException ignored) {}
				}

				// Genre (column 4)
				if (!genreField.getText().trim().isEmpty()) {
					filters.add(RowFilter.regexFilter("(?i)" + genreField.getText().trim(), 4));
				}

				// Developer (column 5)
				if (!developerField.getText().trim().isEmpty()) {
					filters.add(RowFilter.regexFilter("(?i)" + developerField.getText().trim(), 5));
				}

				sorter.setRowFilter(filters.isEmpty()
						? null
						: RowFilter.andFilter(filters));
			});

			clearButton.addActionListener(e -> {
				gameIdField.setText("");
				playersField.setText("");
				playersConditionBox.setSelectedIndex(0);
				genreField.setText("");
				developerField.setText("");
				sorter.setRowFilter(null);
			});

			return_btn.addActionListener(e -> exit_mode[0] = 2);

			panel.revalidate();
			panel.repaint();
		});

		while (exit_mode[0] == 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		SwingUtilities.invokeLater(return_btn::removeActions);
	}
}
