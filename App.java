// Description:
// This Java program creates the main window for a Pacman game using the Swing library. 
// It initializes a fixed-size game board based on the number of rows, columns, and tile size. 
// The game window (JFrame) is configured and an instance of the Pacman game panel is added to it. 
// The window is then displayed in the center of the screen and made non-resizable.

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // Define the number of rows and columns for the game board grid
        int rowCount = 21;
        int columnCount = 19;

        // Define the size of each tile in pixels
        int tileSize = 32;

        // Calculate the total width and height of the game board
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        // Create a new JFrame (the game window) with the title "Pacman"
        JFrame frame = new JFrame("Pacman");

        // Set the window size to the calculated board dimensions
        frame.setSize(boardWidth, boardHeight);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Prevent the window from being resized
        frame.setResizable(false);

        // Ensure the application exits when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of the Pacman game panel (assumed to be a custom class)
        Pacman pacmangame = new Pacman();

        // Add the Pacman game panel to the JFrame
        frame.add(pacmangame);

        // Pack the components inside the JFrame (adjusts the frame size to fit contents)
        frame.pack();

        // Request focus for the Pacman game panel so it can receive keyboard inputs
        pacmangame.requestFocus();

        // Make the window visible (again — this line is redundant, but doesn't harm)
        frame.setVisible(true);
    }
}
