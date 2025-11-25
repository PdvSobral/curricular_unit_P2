import java.awt.*;
import java.io.Serial;
import java.net.URL;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterfaceWrapper {

	public static void basicInterface() {
		// Create and set up the window.
		JFrame frame = new JFrame("Simple Swing App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a button
		JButton button = new JButton("Click me");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Button clicked!");
			}
		});

		// Add the button to the frame
		frame.getContentPane().add(button);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void borderLayout(){
		JFrame frame = new JFrame("Border Layout Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton northButton = new JButton("North");
		JButton southButton = new JButton("South");
		JButton eastButton = new JButton("East");
		JButton westButton = new JButton("West");
		JButton centerButton = new JButton("Center");

		frame.getContentPane().add(northButton, BorderLayout.NORTH);
		frame.getContentPane().add(southButton, BorderLayout.SOUTH);
		frame.getContentPane().add(eastButton, BorderLayout.EAST);
		frame.getContentPane().add(westButton, BorderLayout.WEST);
		frame.getContentPane().add(centerButton, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	// thread safe
	public static void threadSafe() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("EDT Example");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JLabel label = new JLabel("This is a label");
			frame.getContentPane().add(label);
			frame.pack();
			frame.setVisible(true);
		});
	}

	public static void withIcons() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Icon Example");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			System.out.println(System.getProperty("user.dir"));
			ImageIcon icon2 = new ImageIcon("/mnt/dsk_share/Curso/P2/practical_work/resources/meme.jpeg");
			JLabel label = new JLabel(icon2);

			frame.getContentPane().add(label);
			frame.pack();
			frame.setVisible(true);
		});
	}
}
