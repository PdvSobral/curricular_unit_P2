import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable; // to save in binary
import java.util.ArrayList;

// TODO: save machine to file
// TODO: no checks on inputs in GUI
//NOTE TO SELF, TO DELETE MACHINE OR WTV, JUST RM FILE

public class GameMachine implements Serializable {// the machines in the arcade. NO SLOTS OR GAMBLING, only skill games
    @Serial
    private static final long serialVersionUID = 1L; // For serialization version control


    private int id;
    private String __name;

	private Controls control_scheme;
	private Game game_in_machine;
	private MACHINE_STATE availabilityStatus;
	int available_tickets;
}
