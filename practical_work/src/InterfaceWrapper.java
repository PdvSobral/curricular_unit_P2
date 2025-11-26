import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class InterfaceWrapper {

	private static InterfaceWrapper instance = new InterfaceWrapper();
	private final JFrame frame;
	private final JPanel contentPanel;
	private final JPanel fullBottom;

	// Private constructor to prevent instantiation from other classes
	private InterfaceWrapper() {
		frame = new JFrame(Main.APPLICATION_TITTLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HIGHT);
		frame.setLocationRelativeTo(null);  // Center window on screen
		frame.setResizable(false);

		// Use BorderLayout to manage the layout of the window
		frame.setLayout(new BorderLayout());

		// Design interface
		fullBottom = new JPanel(new BorderLayout());
		contentPanel = new JPanel(null);
		setUpInterface();

		frame.setVisible(true);
	}

	// Public method to get the singleton instance
	public static InterfaceWrapper getInstance() {
		if (instance == null) {
			instance = new InterfaceWrapper();
		}
		return instance;
	}

	// Adds panels to simulate borders around the window
	private void setUpInterface() {
		// Create border panels
		JPanel topBorder = new JPanel();
		topBorder.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH));
		topBorder.setBackground(Color.MAGENTA); // Set color as desired
		frame.add(topBorder, BorderLayout.NORTH);

		JPanel leftBorder = new JPanel();
		leftBorder.setPreferredSize(new Dimension(Main.BORDER_WIDTH, frame.getHeight())); // 45px width
		leftBorder.setBackground(Color.MAGENTA); // Set color as desired
		frame.add(leftBorder, BorderLayout.WEST);

		JPanel rightBorder = new JPanel();
		rightBorder.setPreferredSize(new Dimension(Main.BORDER_WIDTH, frame.getHeight())); // 45px width
		rightBorder.setBackground(Color.MAGENTA); // Set color as desired
		frame.add(rightBorder, BorderLayout.EAST);

		// Create the central content panel where we will add components
		frame.add(contentPanel, BorderLayout.CENTER);

		// Set up bottom part
		fullBottom.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH+Main.BOTTOM_PANEL_SIZE));
		frame.add(fullBottom, BorderLayout.SOUTH);

		JPanel bottomBorder = new JPanel();
		bottomBorder.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH));
		bottomBorder.setBackground(Color.MAGENTA); // Set color as desired
		fullBottom.add(bottomBorder, BorderLayout.NORTH);

		JPanel commandsPanel = new JPanel(null);
		commandsPanel.setPreferredSize(new Dimension(frame.getWidth(), Main.BOTTOM_PANEL_SIZE));
		commandsPanel.setBackground(Color.CYAN);
		fullBottom.add(commandsPanel, BorderLayout.SOUTH);

		ImageIcon icon = new ImageIcon(STR."\{System.getProperty("java.class.path")}/resources/meme.jpeg"); // Load the image (based on root)

		for (int i = 0; i < 10; i++) {
			CircularButton button = new CircularButton(Main.BUTTON_SIZE);
			button.setBackground(Color.RED); // Set the button color
			int x = switch (i){
				case 0 -> 30;
				case 1 -> 95;
				case 2 -> 60;
				case 3 -> 125;
				case 4 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BUTTON_SIZE - 10; // 10 = 20/2 (20 spacing between buttons)
				case 5 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) + 10;
				case 6, 7 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 2) - 30;
				case 8 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - Main.BUTTON_SIZE - 40; // base: -30
				case 9 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 3) - 20; // base: -30
				default -> 0;
			};
			int y = switch (i){
				case 0, 1 -> 75;
				case 2, 3 -> 135;
				case 4, 5 -> 10;
				case 6 -> 50;
				case 7 -> 149;
				case 8, 9 -> 101;
				default -> 0;
			};
			// Absolute positioning of the buttons
			button.setBounds(x, y, Main.BUTTON_SIZE, Main.BUTTON_SIZE); // Set position and keep size
			button.setImage(icon);
			button.revalidate();
			button.repaint();
			// Add the button to the panel
			commandsPanel.add(button);
		}
		commandsPanel.revalidate();  // Make sure the layout is refreshed
		commandsPanel.repaint();     // Force repaint of the panel
	}

	// Clears the middle content panel (the space inside the borders)
	public void cleanWindow() {
		contentPanel.removeAll();
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	// Returns the content space (the middle JPanel)
	public JPanel getContentSpace() { return contentPanel; }

	public JPanel getControlSpace() { return fullBottom; }

	// Getter for the JFrame, in case you need to access the main window directly
	public JFrame getFrame() {
		return frame;
	}
}

class CircularButton extends JButton {
	private int __size;
	private ImageIcon imageIcon;  // Field to store the image
	private Image scalled_image;

	public CircularButton(int size) {
		super();
		this.__size = size;
		setContentAreaFilled(false); // Remove the default rectangle
		setBorderPainted(false); // No border
	}

	public void setSize(int size) {
		this.__size = size;
		if (imageIcon != null) scalled_image = imageIcon.getImage().getScaledInstance(__size, __size, Image.SCALE_SMOOTH);
		revalidate();
		repaint();
	}

	// Set an image for the button
	public void setImage(ImageIcon icon) {
		this.imageIcon = icon;
		scalled_image = imageIcon.getImage().getScaledInstance(__size, __size, Image.SCALE_SMOOTH);
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed()) {
			g.setColor(Color.LIGHT_GRAY);
		} else if (getModel().isRollover()) {
			g.setColor(Color.GRAY);
		} else {
			g.setColor(getBackground());
		}

		// Draw the circular background
		g.fillOval(0, 0, getWidth(), getHeight());

		// Draw the image inside the circle, ensuring it is resized to fit the button
		if (imageIcon != null) {
			g.drawImage(scalled_image, 0, 0, null);
		}

		// Request a repaint in the event dispatch thread
		SwingUtilities.invokeLater(this::repaint);

		super.paintComponent(g); // Draw
	}

	@Override
	public Dimension getPreferredSize() { return new Dimension(__size, __size); } // Set the size for the button

}
