import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

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

		// TODO: Further customize the interface, specially buttons
		Color foreground = new Color(207, 204, 204);
		Color background = new Color(42, 56, 85);

		SwingUtilities.invokeLater(() -> {
			__contentPanel.setBackground(background);
			__controlPanel.setForeground(foreground);
		});

		UIDefaults defaults = UIManager.getDefaults();
		defaults.put("Label.foreground", foreground);
		defaults.put("TextField.background", new Color(255, 255, 255));
		defaults.put("TextField.foreground", new Color(0, 0, 0));
		defaults.put("CheckBox.background", background);
		defaults.put("CheckBox.foreground", foreground);

		setUpInterface();

		frame.setVisible(true);
	}

	public static void showErrorWindow(String message) {
		// Create a simple JOptionPane error dialog
		showErrorWindow(message, "Error");
	}
	public static void showErrorWindow(String message, String tittle) {
		// Create a simple JOptionPane error dialog
		JOptionPane.showMessageDialog(
				instance.frame, // parent component (to center in the middle)
				message,
				tittle,
				JOptionPane.INFORMATION_MESSAGE // message type WARNING, ERROR, INFORMATION
		);
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
		ImagePanel topBorder = new ImagePanel(getClass().getResource("/resources/interface_top_brd.png"));
		topBorder.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH));
		frame.add(topBorder, BorderLayout.NORTH);

		ImagePanel leftBorder = new ImagePanel(getClass().getResource("/resources/interface_left_brd.png"));
		leftBorder.setPreferredSize(new Dimension(Main.BORDER_WIDTH, frame.getHeight())); // 45px width
		frame.add(leftBorder, BorderLayout.WEST);

		ImagePanel rightBorder = new ImagePanel(getClass().getResource("/resources/interface_right_brd.png"));
		rightBorder.setPreferredSize(new Dimension(Main.BORDER_WIDTH, frame.getHeight())); // 45px width
		frame.add(rightBorder, BorderLayout.EAST);

		// Create the central content panel where we will add components
		frame.add(__contentPanel, BorderLayout.CENTER);

		// Set up bottom part
		fullBottom.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH + Main.BOTTOM_PANEL_SIZE));
		frame.add(fullBottom, BorderLayout.SOUTH);

		ImagePanel bottomBorder = new ImagePanel(getClass().getResource("/resources/interface_bottom_brd_sc.png"));
		bottomBorder.setPreferredSize(new Dimension(frame.getWidth(), Main.BORDER_WIDTH));
		fullBottom.add(bottomBorder, BorderLayout.NORTH);

		ImageIcon icon2 = new ImageIcon(getClass().getResource("/resources/interface_controls2_sc.png")); // Load the image (based on root)

		__controlPanel.setPreferredSize(new Dimension(frame.getWidth(), Main.BOTTOM_PANEL_SIZE));
		__controlPanel.setBackground(Color.CYAN);
		__controlPanel.setBackgroundImage(icon2);
		fullBottom.add(__controlPanel, BorderLayout.SOUTH);

		// Add the buttons to the panel in a thread-safe way (EDT) [Event Dispatch Thread]
		for (int i = 0; i < 10; i++) {
			final int index = i;  // Local variable to use in the lambda, as lambda needs it
			// FIXME: It's not updating the button size... it has to be manual in the default value
			CircularButton button = new CircularButton(Main.BUTTON_SIZE);

			SwingUtilities.invokeLater(() -> {
				int x = switch (index) {
					case 0 -> 100; // 2*2 (1;1)
					case 1 -> 150; // 2*2 (1;2)
					case 2 -> 125; // 2*2 (2;1)
					case 3 -> 175; // 2*2 (2;2)
					case 4 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) - Main.BUTTON_SIZE - 10; // return
					case 5 -> ((Main.WINDOW_WIDTH - Main.BORDER_LOSS) / 2) + 10; // power
					case 6, 7 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 2) - 90; // up, down
					case 8 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - Main.BUTTON_SIZE - 100; // right
					case 9 -> (Main.WINDOW_WIDTH - Main.BORDER_LOSS) - (Main.BUTTON_SIZE * 3) - 80; // left
					default -> 0;
				};
				int y = switch (index) {
					case 0, 1 -> 28; // 2*2 (first row)
					case 2, 3 -> 68; // 2*2 (second row)
					case 4, 5 -> 8; // power, return
					case 6 -> 20; // up   16
					case 7 -> 76; // down  83
					case 8, 9 -> 48; // left, right
					default -> 0;
				};
				button.setName(
					switch (index) {
						case 0 -> "Accept";
						case 1 -> "Reject";
						//case 2 -> "";
						//case 3 -> "";
						case 4 -> "Return";
						case 5 -> "PowerButton";
						case 6 -> "Up";
						case 7 -> "Down";
						case 8 -> "Right";
						case 9 -> "Left";
						default -> null;
					}
				);
				// Absolute positioning of the buttons
				button.setBounds(x, y, Main.BUTTON_SIZE, Main.BUTTON_SIZE);
				button.setSize(Main.BUTTON_SIZE); // FIXME: for some reason not working, see FIXME in CircularButtons

				String image_name = switch (index){
					case 0 -> "unpressed_buttons/tick_button.png";
					case 1 -> "unpressed_buttons/cross_button.png";
					case 4 -> "unpressed_buttons/return_button.png";
					case 5 -> "unpressed_buttons/power_button.png";
					case 6 -> "unpressed_buttons/up_button.png";
					case 7 -> "unpressed_buttons/down_button.png";
					case 8 -> "unpressed_buttons/right_button.png";
					case 9 -> "unpressed_buttons/left_button.png";
					default -> "unpressed_buttons/template_button.png";
				};

				// SET UP HERE THE Default ACTIONS.
				button.addActionListener(e -> {
					switch (index) {
						case 5:
							System.exit(0);
							break;
						default:
					}
				});

				button.setImage(new ImageIcon(getClass().getResource(STR."/resources/\{image_name}")));
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
	private int __size = 40; // FIXME: If set here, it works. Else, it uses default, no scaling.
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
		else g.fillOval(0, 0, __size, __size); // Draw the circular background if no image

		// repainting happens on the EDT
		repaint();
	}

	@Override
	public void revalidate(){ SwingUtilities.invokeLater(super::revalidate); } // revalidate operated on EDT

	@Override
	public void repaint(){ SwingUtilities.invokeLater(super::repaint); } // repaint operated on EDT

	@Override
	public Dimension getPreferredSize(){ return new Dimension(__size, __size); } // Set the size for the button

	public void removeActions() {
		// Get the current ActionListeners attached to the button
		ActionListener[] listeners = this.getActionListeners();
		// Remove each ActionListener
		for (ActionListener listener : listeners) {
			this.removeActionListener(listener);
		}
	}
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

	public CircularButton getButton(final String button_name){
		AtomicReference<CircularButton> to_return = new AtomicReference<CircularButton>();
		SwingUtilities.invokeLater(() -> {
			// Iterate through the components in the panel
			for (Component component : this.getComponents()) {
				if (component instanceof CircularButton) {
					CircularButton button = (CircularButton) component;
					// Check if the button has the name we are looking for
					if (button_name.equals(button.getName())) {
						to_return.set(button);
					}
				}
			}
		});
		while (to_return.get() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		return to_return.get();
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

class ImagePanel extends JPanel {
	private Image backgroundImage;

	/**
	 * Loads an image from the classpath (works in IDE and JAR)
	 * @param imagePath the path relative to the classpath, e.g. "/resources/interface_bottom_brd_sc.png"
	 */
	public ImagePanel(String imagePath) {
		// Load the image
		backgroundImage = new ImageIcon(imagePath).getImage();
	}
	public ImagePanel(URL imageUrl) {
		if (imageUrl == null) throw new RuntimeException("Image URL cannot be null");
		try {
			BufferedImage img = ImageIO.read(imageUrl);
			backgroundImage = img;
		} catch (IOException e) {
			e.printStackTrace();
			backgroundImage = null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the background image (stretch it to fit the panel)
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}
