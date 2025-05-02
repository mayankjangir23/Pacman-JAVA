/*
 * Description:
 * ------------
 * This Java program is a simple clone of the Pacman game built using Java Swing.
 * It creates a graphical Pacman game board with walls, food pellets, ghosts, and Pacman himself.
 * The player controls Pacman using arrow keys to navigate the board, collect food, and avoid ghosts.
 * The game ends when the player loses all lives. It includes collision detection,
 * random ghost movements, scoring, lives tracking, and a "Game Over" screen.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener {

    // Block class represents any entity on the board (wall, pacman, ghost, food)
    class Block {
        int x, y, width, height;
        Image image;

        int startX, startY; // For resetting positions
        char direction = 'U';
        int velocityX = 0, velocityY = 0;

        // Constructor for Block with image and position details
        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        // Updates direction and ensures no collision with walls
        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        // Sets velocity based on direction
        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        // Equality based on position and size (important for HashSet)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Block block = (Block) o;
            return x == block.x && y == block.y && width == block.width && height == block.height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, width, height);
        }

        // Resets Block to starting position
        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    // Game board dimensions and tile sizes
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    // Image assets
    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;

    // The game map layout (characters used to represent walls, ghosts, food, pacman start)
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    // Sets to track different game elements
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;  // Player block

    Timer gameLoop;  // Timer to run the game loop

    // Ghost movement directions and RNG
    char[] direction = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    // Constructor initializes game assets and starts the game loop
    Pacman() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Load image assets
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadmap();

        // Random initial ghost direction
        for (Block ghost : ghosts) {
            char newDirection = direction[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameLoop = new Timer(50, this); // Game loop timer (updates every 50ms)
        gameLoop.start();
    }

    // Loads map elements based on tileMap array
    public void loadmap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char tileMapChar = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {  // Wall
                    walls.add(new Block(wallImage, x, y, tileSize, tileSize));
                } else if (tileMapChar == 'b') {  // Blue ghost
                    ghosts.add(new Block(blueGhostImage, x, y, tileSize, tileSize));
                } else if (tileMapChar == 'o') {  // Orange ghost
                    ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize));
                } else if (tileMapChar == 'p') {  // Pink ghost
                    ghosts.add(new Block(pinkGhostImage, x, y, tileSize, tileSize));
                } else if (tileMapChar == 'r') {  // Red ghost
                    ghosts.add(new Block(redGhostImage, x, y, tileSize, tileSize));
                } else if (tileMapChar == 'P') {  // Pacman start
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') {  // Food
                    foods.add(new Block(null, x + 14, y + 14, 4, 4));
                }
            }
        }
    }

    // Draws game elements
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameOver) {
            // Draw Game Over screen
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, boardWidth, boardHeight);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverMessage = "GAME OVER";
            FontMetrics metrics = g.getFontMetrics();
            int x = (boardWidth - metrics.stringWidth(gameOverMessage)) / 2;
            int y = boardHeight / 2 - metrics.getHeight() / 2;
            g.drawString(gameOverMessage, x, y);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String scoreMessage = "Score: " + score;
            g.drawString(scoreMessage, (boardWidth - metrics.stringWidth(scoreMessage)) / 2, y + metrics.getHeight() + 20);
            return;
        }

        // Draw Pacman
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // Draw ghosts
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        // Draw walls
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        // Draw food
        g.setColor(Color.white);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        // Draw score and lives
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Lives: " + lives + " Score: " + score, tileSize / 2, tileSize / 2);
    }

    // Handles all movement logic
    public void move() {
        // Move Pacman
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // Ghost logic: move + random turns
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');  // Force upward movement in tunnel
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateDirection(direction[random.nextInt(4)]);
                }
            }
        }

        // Handle food collection
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        // Reload map if all food is collected
        if (foods.isEmpty()) {
            loadmap();
            resetPositions();
        }
    }

    // Collision detection between two blocks
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    // Reset positions of pacman and ghosts
    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            ghost.updateDirection(direction[random.nextInt(4)]);
        }
    }

    // Called by game loop timer
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    // Handles player input for movement and restart
    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadmap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        // Update Pacman image based on direction
        if (pacman.direction == 'U') pacman.image = pacmanUpImage;
        if (pacman.direction == 'D') pacman.image = pacmanDownImage;
        if (pacman.direction == 'L') pacman.image = pacmanLeftImage;
        if (pacman.direction == 'R') pacman.image = pacmanRightImage;
    }
}
