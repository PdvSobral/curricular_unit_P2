import java.io.Serial;
import java.util.ArrayList;
import java.io.Serializable; // to save in binary

public class Game implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L; // For serialization version control
	private final int __year;
	private final String __name;
	private final ArrayList<Integer> __allowedPlayers;
	private final String __genre;
	private final String __developer;
	private final String __description;

	// Constructor + checks
	public Game(int year, String name, ArrayList<Integer> allowedPlayers, String genre, String developer, String description) {
		if (year < 1970 && year > DatesTimes.getInstance().getYear()){
			this.__year = 0;
			throw new IllegalArgumentException("Game year must be between 1970 and the current year!");
		}
		this.__year = year;
		this.__name = name;
		this.__allowedPlayers = new ArrayList<>(allowedPlayers); // Copy to prevent external changes
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
	public ArrayList<Integer> getAllowedPlayers() {
		return new ArrayList<>(__allowedPlayers); // Returns a copy for safety
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
		return "Game@" + Integer.toHexString(hashCode())+ "{" +
				"year=" + __year +
				", name='" + __name + '\'' +
				", allowedPlayers=" + __allowedPlayers +
				", genre='" + __genre + '\'' +
				", developer='" + __developer + '\'' +
				", description='" + __description + '\'' +
				'}';
	}

	public void save(){
		Database.getInstance().saveGame(this);
	}
	public void save(String file_name){
		Database.getInstance().saveGame(this, file_name);
	}
}
