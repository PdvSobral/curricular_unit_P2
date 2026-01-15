import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMachineCheck {
	// GPTed and then manually made to work. No time to discuss philosophy.
	// THIS WAS NOT VIBE-CODDED! Let's be clear on that.

	private JTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;

	// Filters
	private JTextField gameIdField;
	private JTextField ticketField;
	private JComboBox<String> ticketConditionBox;
	private JComboBox<Controls> controlBox;
	private JComboBox<MACHINE_STATE> statusBox;

	public GameMachineCheck() {
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
							"Machine ID",
							"Machine Name",
							"Game ID",
							"Game Name",
							"Control Scheme",
							"Status",
							"Available Tickets"
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

			// Populate table
			List<Integer> machines = Database.getInstance().listGameMachine(Main.RUNNING_MODE==Main.DEBUG);
			for (int gm_id : machines) {
				GameMachine gm = Database.getInstance().loadGameMachine(gm_id);
				Game gm2 = Database.getInstance().loadGame(gm.getGame());
				model.addRow(new Object[]{
						gm.getId(),
						gm.getName(),
						gm.getGame(),
						gm2.getName(),
						gm.getControlScheme(),
						gm.getAvailabilityStatus(),
						gm.getAvailable_tickets()
				});
			}

			panel.add(new JScrollPane(table), BorderLayout.CENTER);

			/* ===================== FILTER PANEL ===================== */
			JPanel filterPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gameIdField = new JTextField(8);
			ticketField = new JTextField(8);
			ticketConditionBox = new JComboBox<>(new String[]{"=", ">", "<"});

			JComboBox<Controls> controlBox = new JComboBox<>();
			DefaultComboBoxModel<Controls> model = new DefaultComboBoxModel<>();
			model.addElement(null); // represents the "" option
			for (Controls state : Controls.values()) {
				model.addElement(state);
			}
			controlBox.setModel(model);
			controlBox.setSelectedItem(null);

			JComboBox<MACHINE_STATE> statusBox = new JComboBox<>();
			DefaultComboBoxModel<MACHINE_STATE> model2 = new DefaultComboBoxModel<>();
			model2.addElement(null); // represents the "" option
			for (MACHINE_STATE state : MACHINE_STATE.values()) {
				model2.addElement(state);
			}
			statusBox.setModel(model2);
			statusBox.setSelectedItem(null);

			JButton applyButton = new JButton("Apply Filters");
			JButton clearButton = new JButton("Clear Filters");
			// Row 0
			gbc.gridx = 0; gbc.gridy = 0;
			filterPanel.add(new JLabel("Game ID:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(gameIdField, gbc);
			gbc.gridx = 2;
			filterPanel.add(new JLabel("Control Type:"), gbc);
			gbc.gridx = 3;
			filterPanel.add(controlBox, gbc);
			// Row 1
			gbc.gridx = 0; gbc.gridy = 1;
			filterPanel.add(new JLabel("Tickets:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(ticketConditionBox, gbc);
			gbc.gridx = 2;
			filterPanel.add(ticketField, gbc);
			gbc.gridx = 3;
			filterPanel.add(new JLabel("Status:"), gbc);
			gbc.gridx = 4;
			filterPanel.add(statusBox, gbc);
			// Row 2
			gbc.gridx = 4; gbc.gridy = 2;
			filterPanel.add(applyButton, gbc);
			gbc.gridx = 3;
			filterPanel.add(clearButton, gbc);
			panel.add(filterPanel, BorderLayout.NORTH);
			/* ===================== FILTER LOGIC ===================== */
			applyButton.addActionListener(e -> {
				List<RowFilter<Object, Object>> filters = new ArrayList<>();

				// Game ID (column 2)
				if (!gameIdField.getText().trim().isEmpty()) {
					int gameId = Integer.parseInt(gameIdField.getText().trim());
					filters.add(RowFilter.numberFilter(
							RowFilter.ComparisonType.EQUAL, gameId, 2));
				}

				// Control Scheme (column 3)
				Controls control = (Controls) controlBox.getSelectedItem();
				if (control != null){ filters.add(RowFilter.regexFilter(STR."^\{control}$", 4)); }

				// Status (column 4)
				MACHINE_STATE state = (MACHINE_STATE) statusBox.getSelectedItem();
				if (state != null){ filters.add(RowFilter.regexFilter(STR."^\{state}$", 5)); }

				// Tickets (column 5)
				if (!ticketField.getText().trim().isEmpty()) {
					int tickets = Integer.parseInt(ticketField.getText().trim());
					String condition = (String) ticketConditionBox.getSelectedItem();
					RowFilter.ComparisonType type = switch (condition) {
						case ">" -> RowFilter.ComparisonType.AFTER;
						case "<" -> RowFilter.ComparisonType.BEFORE;
						default -> RowFilter.ComparisonType.EQUAL;
					};
					filters.add(RowFilter.numberFilter(type, tickets, 6));
				}

				sorter.setRowFilter(filters.isEmpty()
						? null
						: RowFilter.andFilter(filters));
			});

			clearButton.addActionListener(e -> {
				gameIdField.setText("");
				ticketField.setText("");
				ticketConditionBox.setSelectedIndex(0);
				controlBox.setSelectedItem(null);
				statusBox.setSelectedItem(null);
				sorter.setRowFilter(null);
			});

			return_btn.addActionListener(e -> exit_mode[0] = 2);

			panel.revalidate();
			panel.repaint();
		});

		while (exit_mode[0] == 0) {
			try { Thread.sleep(100); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		SwingUtilities.invokeLater(return_btn::removeActions);
	}
}
