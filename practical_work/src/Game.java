import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable; // to save in binary

@SuppressWarnings({"ClassCanBeRecord", "preview"})
public class Game implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control
	private final int __year;
	private final String __name;
	private final int __allowedPlayers;
	private final String __genre;
	private final String __developer;
	private final String __description;

	// Constructor + checks
	public Game(int year, String name, int allowedPlayers, String genre, String developer, String description) {
		if (year < 1970 || year > DatesTimes.getInstance().getYear()){
			this.__year = 0;
			throw new IllegalArgumentException("Game year must be between 1970 and the current year!");
		}
		this.__year = year;
		this.__name = name;
		this.__allowedPlayers = allowedPlayers;
		this.__genre = genre;
		this.__developer = developer;
		this.__description = description;
	}

	// Getters
	public int getYear() {
		return this.__year;
	}
	public String getName() {
		return this.__name;
	}
	public int getAllowedPlayers() {
		return __allowedPlayers; // Returns a copy for safety
	}
	public String getGenre() {
		return this.__genre;
	}
	public String getDeveloper() {
		return this.__developer;
	}
	public String getDescription() {
		return this.__description;
	}

	// No Setters (NOTE: setters are not here all attributes should be immutable, after creating the game no editing is allowed

	// Method to display game information. Overrides normal function
	@Override
	public String toString() {
		return STR."Game@\{Integer.toHexString(hashCode())}{year=\{__year}, name='\{__name}', allowedPlayers=\{__allowedPlayers}, genre='\{__genre}', developer='\{__developer}', description='\{__description}'}";
	}

	public void save(){
		Database.getInstance().saveGame(this);
	}
	public void save(String file_name){
		Database.getInstance().saveGame(this, file_name);
	}

	public static Game createGameGUI() {
		final int[] exit_mode = {0};
		// Declare the Game object that will be returned
		final Game[] game = new Game[1];  // Using an array to modify within the lambda
		// get GUI handler instance
		InterfaceWrapper interfaceWrapper = InterfaceWrapper.getInstance();
		// content panel
		ContentPanel main_content = interfaceWrapper.getContentSpace();
		main_content.setLayout(null); // Using absolute positioning
		// for button reassignment
		ControlPanel controls = interfaceWrapper.getControlSpace();

		CircularButton return_btn = controls.getButton("Return");
		CircularButton accept_btn = controls.getButton("Accept");
		CircularButton reject_btn = controls.getButton("Reject");

		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			accept_btn.removeActions();
			reject_btn.removeActions();

			// Title label
			JLabel titleLabel = new JLabel("NEW GAME CREATION", SwingConstants.CENTER);
			Font old = titleLabel.getFont();
			titleLabel.setFont(new Font(old.getName(), Font.BOLD, 16));
			int base_center = ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BORDER_WIDTH;
			titleLabel.setBounds(base_center - 250, 10, 500, 30);
			main_content.add(titleLabel);

			// Game Name label and text field
			JLabel nameLabel = new JLabel("Game Name:", SwingConstants.RIGHT);
			nameLabel.setBounds(90, 70, 140, 25);
			main_content.add(nameLabel);
			JTextField nameField = new JTextField();
			nameField.setBounds(150 + 90, 70, 200, 25);
			main_content.add(nameField);

			// Genre label and text field
			JLabel genreLabel = new JLabel("Genre:", SwingConstants.RIGHT);
			genreLabel.setBounds(90, 110, 140, 25);
			main_content.add(genreLabel);
			JTextField genreField = new JTextField();
			genreField.setBounds(150 + 90, 110, 200, 25);
			main_content.add(genreField);

			// Developer label and text field
			JLabel developerLabel = new JLabel("Developer:", SwingConstants.RIGHT);
			developerLabel.setBounds(90, 150, 140, 25);
			main_content.add(developerLabel);
			JTextField developerField = new JTextField();
			developerField.setBounds(150 + 90, 150, 200, 25);
			main_content.add(developerField);

			// Year label and spinner (for selecting the year)
			JLabel yearLabel = new JLabel("Release Year:", SwingConstants.RIGHT);
			yearLabel.setBounds(90, 190, 140, 25);
			main_content.add(yearLabel);
			SpinnerModel yearModel = new SpinnerNumberModel( 2000, 1970, DatesTimes.getInstance().getYear(), 1); // Start, Min, Max, Step
			JSpinner yearSpinner = new JSpinner(yearModel);
			// removes the comma separator in 2025 (was showing 2,025)
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "#");
			yearSpinner.setEditor(editor);
			yearSpinner.setBounds(150 + 90, 190, 100, 25);
			main_content.add(yearSpinner);

			// Allowed players label and combo box
			JLabel playersLabel = new JLabel("Allowed Players:", SwingConstants.RIGHT);
			playersLabel.setBounds(90, 230, 140, 25);
			main_content.add(playersLabel);
			JComboBox<Integer> playersComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
			playersComboBox.setBounds(150 + 90, 230, 100, 25);
			main_content.add(playersComboBox);

			// Description label and text area
			JLabel descriptionLabel = new JLabel("Description:", SwingConstants.RIGHT);
			descriptionLabel.setBounds(90, 270, 140, 25);
			main_content.add(descriptionLabel);
			JTextArea descriptionArea = new JTextArea();
			descriptionArea.setBounds(150 + 90, 270, 200, 100);
			descriptionArea.setLineWrap(true);
			descriptionArea.setWrapStyleWord(true);
			descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			main_content.add(descriptionArea);

			ActionListener _main = _ -> {
				String name = nameField.getText();
				if (name == null || name.isEmpty()) {
					InterfaceWrapper.showErrorWindow("Game name is empty!");
					return;
				}
				String genre = genreField.getText();
				if (genre == null || genre.isEmpty()) {
					InterfaceWrapper.showErrorWindow("Game name is empty!");
					return;
				}
				String developer = developerField.getText();
				if (developer == null || developer.isEmpty()) {
					InterfaceWrapper.showErrorWindow("Game name is empty!");
					return;
				}
				int year = (Integer) yearSpinner.getValue();
				int players = (Integer) playersComboBox.getSelectedItem();
				String description = descriptionArea.getText();

				// Create a new Game object with the data
				game[0] = new Game(year, name, players, genre, developer, description);

				// Handle the game creation (e.g., save to a database, print data, etc.)
				System.out.println(STR."Game Created: \{name}, \{genre}, \{developer}, \{year}, \{players} players, \{description}");

				exit_mode[0] = 1;
			};
			ActionListener _scnd = _ -> {
				exit_mode[0] = 2;
			};

			// Submit button
			JButton submitButton = new JButton("Create Game");
			submitButton.setBounds(310, 400, 150, 30);
			submitButton.addActionListener(_main);
			main_content.add(submitButton);

			accept_btn.addActionListener(_main);

			JButton exitButton = new JButton("Cancel");
			exitButton.setBounds(130, 400, 150, 30);
			exitButton.addActionListener(_scnd);
			main_content.add(exitButton);

			reject_btn.addActionListener(_scnd);
			reject_btn.addActionListener(_scnd);

			main_content.revalidate();
			main_content.repaint();
			interfaceWrapper.getFrame().revalidate();
			interfaceWrapper.getFrame().repaint();
		});

		while (exit_mode[0] == 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// reset buttons, just in case
		SwingUtilities.invokeLater(() -> {
			return_btn.removeActions();
			accept_btn.removeActions();
			reject_btn.removeActions();
		});

		// Return the created Game object after the user submits, if valid
		if (exit_mode[0] == 1) return game[0];
		else return null; // return null if user choose to cancel game input
	}

}
