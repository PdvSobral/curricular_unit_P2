import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerCheck {
	// GPTed and then manually made to work. No time to discuss philosophy.
	// THIS WAS NOT VIBE-CODDED! Let's be clear on that.

	private JTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;

	// Filters
	private JTextField idField;
	private JTextField nameField;
	private JTextField ageField;
	private JComboBox<String> ageConditionBox;

	public PlayerCheck() {
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
					new String[]{"ID", "Name", "Age"}, 0
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
			List<Integer> players = Database.getInstance().listPlayers(Main.RUNNING_MODE==Main.DEBUG); // assume this returns List<Player>
			for (Integer p2 : players) {
				Player p = Database.getInstance().loadPlayer(p2);
				model.addRow(new Object[]{
						p.getId(),
						p.getName(),
						p.getAge()
				});
			}

			panel.add(new JScrollPane(table), BorderLayout.CENTER);

			/* ===================== FILTER PANEL ===================== */
			JPanel filterPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			idField = new JTextField(8);
			nameField = new JTextField(8);
			ageField = new JTextField(8);
			ageConditionBox = new JComboBox<>(new String[]{"=", ">", "<"});

			JButton applyButton = new JButton("Apply Filters");
			JButton clearButton = new JButton("Clear Filters");

			// Row 0
			gbc.gridx = 0; gbc.gridy = 0;
			filterPanel.add(new JLabel("ID:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(idField, gbc);
			gbc.gridx = 2;
			filterPanel.add(new JLabel("Name:"), gbc);
			gbc.gridx = 3;
			filterPanel.add(nameField, gbc);

			// Row 1
			gbc.gridx = 0; gbc.gridy = 1;
			filterPanel.add(new JLabel("Age:"), gbc);
			gbc.gridx = 1;
			filterPanel.add(ageConditionBox, gbc);
			gbc.gridx = 2;
			filterPanel.add(ageField, gbc);

			// Row 2
			gbc.gridx = 3; gbc.gridy = 2;
			filterPanel.add(applyButton, gbc);
			gbc.gridx = 2;
			filterPanel.add(clearButton, gbc);

			panel.add(filterPanel, BorderLayout.NORTH);

			/* ===================== FILTER LOGIC ===================== */
			applyButton.addActionListener(e -> {
				List<RowFilter<Object, Object>> filters = new ArrayList<>();

				// ID filter
				if (!idField.getText().trim().isEmpty()) {
					int id = Integer.parseInt(idField.getText().trim());
					filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, id, 0));
				}

				// Name filter (column 1)
				if (!nameField.getText().trim().isEmpty()) {
					filters.add(RowFilter.regexFilter("(?i)" + nameField.getText().trim(), 1));
				}

				// Age filter (column 2)
				if (!ageField.getText().trim().isEmpty()) {
					int age = Integer.parseInt(ageField.getText().trim());
					String condition = (String) ageConditionBox.getSelectedItem();
					RowFilter.ComparisonType type = switch (condition) {
						case ">" -> RowFilter.ComparisonType.AFTER;
						case "<" -> RowFilter.ComparisonType.BEFORE;
						default -> RowFilter.ComparisonType.EQUAL;
					};
					filters.add(RowFilter.numberFilter(type, age, 2));
				}

				sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
			});

			clearButton.addActionListener(e -> {
				idField.setText("");
				nameField.setText("");
				ageField.setText("");
				ageConditionBox.setSelectedIndex(0);
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
