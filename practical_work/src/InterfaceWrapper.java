import javax.swing.*;
import java.awt.*;

public class InterfaceWrapper {

	private static InterfaceWrapper instance = new InterfaceWrapper();
	private final JFrame frame;
	private final ContentPanel __contentPanel;
	private final ControlPanel __controlPanel;

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

		__controlPanel = new ControlPanel(null);
		__contentPanel = new ContentPanel(null);
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
		JPanel fullBottom = new JPanel(new BorderLayout());

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
		frame.add(__contentPanel, BorderLayout.CENTER);

		// Set up bottom part
		fullBottom.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH + Main.BOTTOM_PANEL_SIZE));
		frame.add(fullBottom, BorderLayout.SOUTH);

		JPanel bottomBorder = new JPanel();
		bottomBorder.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH));
		bottomBorder.setBackground(Color.MAGENTA); // Set color as desired
		fullBottom.add(bottomBorder, BorderLayout.NORTH);

		ImageIcon icon2 = new ImageIcon(STR."\{System.getProperty("java.class.path")}/resources/background.jpg"); // Load the image (based on root)

		__controlPanel.setPreferredSize(new Dimension(frame.getWidth(), Main.BOTTOM_PANEL_SIZE));
		__controlPanel.setBackground(Color.CYAN);
		__controlPanel.setBackgroundImage(icon2);
		fullBottom.add(__controlPanel, BorderLayout.SOUTH);

		final ImageIcon icon = new ImageIcon(STR."\{System.getProperty("java.class.path")}/resources/meme_c.png"); // Load the image (based on root)

		// Add the buttons to the panel in a thread-safe way (EDT) [Event Dispatch Thread]
		for (int i = 0; i < 10; i++) {
			final int index = i;  // Local variable to use in the lambda, as lambda needs it

			SwingUtilities.invokeLater(() -> {
				CircularButton button = new CircularButton(Main.BUTTON_SIZE);
				int x = switch (index) {
					case 0 -> 30;
					case 1 -> 95;
					case 2 -> 60;
					case 3 -> 125;
					case 4 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BUTTON_SIZE - 10;
					case 5 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) + 10;
					case 6, 7 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 2) - 30;
					case 8 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - Main.BUTTON_SIZE - 40;
					case 9 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 3) - 20;
					default -> 0;
				};
				int y = switch (index) {
					case 0, 1 -> 75;
					case 2, 3 -> 135;
					case 4, 5 -> 10;
					case 6 -> 50;
					case 7 -> 149;
					case 8, 9 -> 101;
					default -> 0;
				};
				// Absolute positioning of the buttons
				button.setBounds(x, y, Main.BUTTON_SIZE, Main.BUTTON_SIZE);
				button.setImage(icon);
				__controlPanel.add(button);
			});
		}
		SwingUtilities.invokeLater(() -> {
			__controlPanel.revalidate();  // Make sure the layout is refreshed
			__controlPanel.repaint();     // Force repaint of the panel, although the buttons kind of do that already
		});
	}
	
	// Returns the content space (the middle JPanel)
	public ContentPanel getContentSpace() { return __contentPanel; }

	public ControlPanel getControlSpace() { return __controlPanel; }

	// Getter for the JFrame, in case you need to access the main window directly
	public JFrame getFrame() { return frame; }
}

class CircularButton extends JButton {
	private int __size;
	private ImageIcon __imageIcon; // stores original image
	private Image __scalled_image;

	public CircularButton(int size) {
		super();
		this.__size = size;
		setContentAreaFilled(false); // Remove the default rectangle
		setBorderPainted(false); // No border
	}

	public void setSize(int size) {
		this.__size = size;
		if (__imageIcon != null) __scalled_image = __imageIcon.getImage().getScaledInstance(__size, __size, Image.SCALE_SMOOTH);
		revalidate();
		repaint();
	}

	// Set an image for the button
	public void setImage(ImageIcon icon) {
		this.__imageIcon = icon;
		__scalled_image = __imageIcon.getImage().getScaledInstance(__size, __size, Image.SCALE_SMOOTH);
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		/*
		if (getModel().isPressed()) {
			// when pressed
			g.setColor(Color.LIGHT_GRAY);
		} else if (getModel().isRollover()) {
			// on hover
		} else {
			// rest of the time
			g.setColor(getBackground());
		}
		*/

		// Draw the image inside the circle, ensuring it is resized to fit the button
		if (__imageIcon != null) g.drawImage(__scalled_image, 0, 0, null);
		else g.fillOval(0, 0, getWidth(), getHeight()); // Draw the circular background if no image

		// repainting happens on the EDT
		repaint();
	}

	@Override
	public void revalidate(){ SwingUtilities.invokeLater(super::revalidate); } // revalidate operated on EDT

	@Override
	public void repaint(){ SwingUtilities.invokeLater(super::repaint); } // repaint operated on EDT

	@Override
	public Dimension getPreferredSize(){ return new Dimension(__size, __size); } // Set the size for the button
}

class ControlPanel extends JPanel {
	private Image __backgroundImage;

	public ControlPanel(BorderLayout borderLayout){ super(borderLayout); }

	public void setBackgroundImage(ImageIcon icon){ this.__backgroundImage = icon.getImage(); }

	@Override
	public void revalidate(){ SwingUtilities.invokeLater(super::revalidate); } // revalidate operated on EDT

	@Override
	public void repaint(){ SwingUtilities.invokeLater(super::repaint); } // repaint operated on EDT

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (__backgroundImage != null) {
			g.drawImage(__backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}

class ContentPanel extends JPanel {
	private Image __backgroundImage;

	public ContentPanel(LayoutManager layout){
		super(layout);
	}

	public void setBackgroundImage(ImageIcon icon){ this.__backgroundImage = icon.getImage(); }

	public void clearPanel() {
		SwingUtilities.invokeLater(this::removeAll);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void revalidate(){ SwingUtilities.invokeLater(super::revalidate); } // revalidate operated on EDT

	@Override
	public void repaint(){ SwingUtilities.invokeLater(super::repaint); } // repaint operated on EDT

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (__backgroundImage != null) {
			g.drawImage(__backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
